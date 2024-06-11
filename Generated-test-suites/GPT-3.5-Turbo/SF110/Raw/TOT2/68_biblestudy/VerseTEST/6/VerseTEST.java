import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertTrue(id > 0);
    }

    @Test
    public void testGet() {
        Translation[] translations = { new Translation(), new Translation() };
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
        assertEquals(2, verses.size());
    }

    @Test
    public void testGetWithReferences() {
        Translation[] translations = { new Translation(), new Translation() };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 2, 5);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertTrue(verses.size() > 0);
    }

    // Additional test cases for edge cases and exceptions can be added here

}