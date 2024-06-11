import static org.junit.Assert.*;
import org.junit.Test;
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
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = {new Translation()};
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(1, verses.size());
    }
    
    @Test
    public void testGetWithReferences() {
        Translation[] translations = {new Translation()};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertTrue(verses.size() > 0);
    }
    
    @Test
    public void testGetIdInvalidTranslation() {
        int id = Verse.GetId(-1, 1, 1, 1);
        assertEquals(-1, id);
    }
    
    @Test
    public void testGetIdInvalidBook() {
        int id = Verse.GetId(1, -1, 1, 1);
        assertEquals(-1, id);
    }
    
    @Test
    public void testGetIdInvalidChapter() {
        int id = Verse.GetId(1, 1, -1, 1);
        assertEquals(-1, id);
    }
    
    @Test
    public void testGetIdInvalidVerse() {
        int id = Verse.GetId(1, 1, 1, -1);
        assertEquals(-1, id);
    }
}