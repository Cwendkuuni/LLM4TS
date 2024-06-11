import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
    }
    
    @Test
    public void testGetId() {
        assertEquals(1, Verse.GetId(1, 1, 1, 1));
    }
    
    @Test
    public void testGet() {
        Translation[] translations = { new Translation("Test") };
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertEquals(1, verses.size());
    }
    
    @Test
    public void testGetWithReferences() {
        Translation[] translations = { new Translation("Test") };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(2, verses.size());
    }
    
    @Test
    public void testGetIdWithInvalidTranslationId() {
        assertEquals(-1, Verse.GetId(-1, 1, 1, 1));
    }
    
    @Test
    public void testGetIdWithNonExistentVerseId() {
        assertEquals(0, Verse.GetId(1, 1, 1, 1000));
    }
    
    @Test
    public void testGetIdWithSQLException() {
        assertEquals(-1, Verse.GetId(1, -1, 1, 1));
    }
    
    @Test
    public void testVerseCache() {
        Verse verse = Verse.New(1);
        Verse cachedVerse = Verse.verseCache.get(1);
        assertEquals(verse, cachedVerse);
    }
}