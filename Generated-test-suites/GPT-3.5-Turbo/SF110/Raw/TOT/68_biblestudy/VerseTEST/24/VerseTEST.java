
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.*;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = {new Translation(1), new Translation(2)};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
        assertEquals(translations.length, verses.size());
        for (Verse verse : verses) {
            assertNotNull(verse);
            assertEquals(verseId, verse.getReference().getVerseNo());
        }
    }
    
    @Test
    public void testGetRange() {
        Translation[] translations = {new Translation(1), new Translation(2)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 2, 2);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertEquals(translations.length * 4, verses.size());
        for (Verse verse : verses) {
            assertNotNull(verse);
            assertTrue(verse.getReference().compareTo(begin) >= 0);
            assertTrue(verse.getReference().compareTo(end) <= 0);
        }
    }
    
    @Test
    public void testToString() {
        Verse verse = Verse.New(1);
        String expected = "Verse:[" + verse.getId() + " " + verse.getTranslationReference() + " " + verse.getText() + "]";
        assertEquals(expected, verse.toString());
    }
    
    // more test cases can be added for the remaining methods
    
}

