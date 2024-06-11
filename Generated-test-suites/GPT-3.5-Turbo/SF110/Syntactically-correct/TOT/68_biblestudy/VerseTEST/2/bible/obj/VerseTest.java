import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import bible.obj.*;
import java.util.ArrayList;

public class VerseTest {

    private Verse verse;

    @Before
    public void setUp() {
        verse = new Verse(1); // Initializing Class Object with ID 1
    }

    @Test
    public void testNew() {
        Verse newVerse = Verse.New(2);
        assertNotNull(newVerse);
    }

    @Test
    public void testGetId() {
        int id = verse.getId();
        assertEquals(1, id);
    }

    @Test
    public void testGetText() {
        String text = verse.getText();
        assertNotNull(text);
    }

    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertNotNull(value);
    }

    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
    }

    @Test
    public void testGetReference() {
        Reference reference = verse.getReference();
        assertNotNull(reference);
    }

    @Test
    public void testToString() {
        String string = verse.toString();
        assertNotNull(string);
    }

    @Test
    public void testGetIdWithTranslationBookIdChapterIdVerseId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(1, id);
    }

    @Test
    public void testGetWithTranslationsBookIdChapterIdVerseId() {
        Translation[] translations = { new Translation(1) };
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
    }

    @Test
    public void testGetWithTranslationsBeginEnd() {
        Translation[] translations = { new Translation(1) };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
    }

    @Test
    public void testReadId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
        assertEquals(1, id);
    }
}