package bible.obj;

import static org.junit.Assert.*;
import org.junit.Test;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse1 = Verse.New(1);
        assertEquals(1, verse1.getId());
        
        Verse verse2 = Verse.New(1); // Test for caching
        assertSame(verse1, verse2);
    }

    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
        
        id = Verse.GetId(2, 1, 1, 1);
        assertEquals(-1, id); // Test for id not found
    }

    @Test
    public void testGet() {
        Translation[] translations = { new Translation(1), new Translation(2) };
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertFalse(verses.isEmpty());
    }

    @Test
    public void testGetWithReferences() {
        Translation[] translations = { new Translation(1), new Translation(2) };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 5);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertFalse(verses.isEmpty());
    }

    @Test
    public void testGetNegativeCase() {
        Translation[] translations = {};
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1); // Test for empty translations
        assertTrue(verses.isEmpty());
    }

    // Add more test cases for other methods here
    
}