package com.example.jacek.notepad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jacek.notepad.data.NoteDAO;
import com.example.jacek.notepad.pojo.Note;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText newNoteText;
    private ListView noteList;
    private NoteDAO noteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inicjalizujemy pola klasy
        newNoteText =  findViewById(R.id.new_note_text);
        noteList =  findViewById(R.id.note_list);
        // tworzymy obiekt DAO
        noteDAO = new NoteDAO(this);
        // wyświetlamy listę notatek
        reloadNotesList();
    }

    // dodaje nową notatkę do bazy danych i odświeża listę
    public void addNewNote(View view) {
        Note note = new Note();
        String text = newNoteText.getText().toString();
        if (text.length() > 0) {
            note.setNoteText(text);
        }

        noteDAO.insertNote(note);
        reloadNotesList();
    }

    // usuwa notatkę z bazy i odświeża listę
    public void removeNote(Note note) {
        noteDAO.deleteNoteById(note.getId());
        reloadNotesList();
    }

    // pokazuje listę notatek
    private void reloadNotesList() {
        // pobieramy z bazy danych listę notatek
        List<Note> allNotes = noteDAO.getAllNotes();

        // ustawiamy adapter listy
        noteList.setAdapter(new ArrayAdapter<Note>(this, R.layout.note_layout, allNotes) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final View noteView = super.getView(position, convertView, parent);

                // po kliknięciu na notatkę zostanie ona usunięta
                noteView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeNote(getItem(position));
                    }
                });

                return noteView;
            }
        });
    }
}