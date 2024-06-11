import org.junit.Test;
import static org.junit.Assert.*;

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
        
        id = Verse.GetId(0, 1, 2, 3);
        assertEquals(-1, id);
        
        id = Verse.GetId(1, 0, 2, 3);
        assertEquals(-1, id);
        
        id = Verse.GetId(1, 1, 0, 3);
        assertEquals(-1, id);
        
        id = Verse.GetId(1, 1, 2, 0);
        assertEquals(-1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation(1, "Translation 1");
        translations[1] = new Translation(2, "Translation 2");
        
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(2, verses.size());
        
        verses = Verse.Get(translations, 0, 1, 1);
        assertNotNull(verses);
        assertEquals(0, verses.size());
        
        verses = Verse.Get(null, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(0, verses.size());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String expected = "Verse:[1 TranslationReference: [1 1 1] Text]";
        assertEquals(expected, verse.toString());
    }
    
    @Test
    public void testGetIdFromReference() {
        int id = Verse.GetId(new Translation(1, "Translation 1"), new Book(1, "Book 1"), 1, 1);
        assertEquals(1, id);
        
        id = Verse.GetId(new Translation(0, "Translation 0"), new Book(1, "Book 1"), 1, 1);
        assertEquals(-1, id);
        
        id = Verse.GetId(new Translation(1, "Translation 1"), new Book(0, "Book 0"), 1, 1);
        assertEquals(-1, id);
        
        id = Verse.GetId(new Translation(1, "Translation 1"), new Book(1, "Book 1"), 0, 1);
        assertEquals(-1, id);
        
        id = Verse.GetId(new Translation(1, "Translation 1"), new Book(1, "Book 1"), 1, 0);
        assertEquals(-1, id);
    }
}