import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class VerseTest {

    private Verse verse;

    @Before
    public void setUp() {
        verse = new Verse(1);
    }

    @Test
    public void testNew() {
        Verse newVerse = Verse.New(1);
        assertThat(newVerse.getId(), is(verse.getId()));
    }

    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 2, 3, 4);
        assertThat(id, is(verse.getId()));
    }

    @Test
    public void testGetText() {
        String text = verse.getText();
        assertThat(text, is(notNullValue()));
    }

    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertThat(value, is(notNullValue()));
    }

    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertThat(transRef, is(notNullValue()));
    }

    @Test
    public void testGetReference() {
        Reference reference = verse.getReference();
        assertThat(reference, is(notNullValue()));
    }

    @Test
    public void testToString() {
        String str = verse.toString();
        assertThat(str, is(notNullValue()));
    }

    @Test
    public void testGetIdWithTranslation() {
        int translationId = 1;
        int bookId = 2;
        int chapterId = 3;
        int verseId = 4;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertThat(id, is(verse.getId()));
    }

    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        int bookId = 2;
        int chapterId = 3;
        int verseId = 4;
        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertThat(verses, is(notNullValue()));
    }

    @Test
    public void testGetWithReference() {
        Translation[] translations = new Translation[1];
        Reference begin = new Reference(2, 3, 4);
        Reference end = new Reference(5, 6, 7);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertThat(verses, is(notNullValue()));
    }
}