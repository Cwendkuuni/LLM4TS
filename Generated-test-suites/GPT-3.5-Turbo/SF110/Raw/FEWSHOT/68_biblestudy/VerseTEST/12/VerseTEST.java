import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class VerseTest {
  
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 2, 3, 4);
        assertEquals(1, id);
        
        id = Verse.GetId(9, 8, 7, 6);
        assertEquals(-1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = {new Translation(1, "Translation1"), new Translation(2, "Translation2")};
        ArrayList<Verse> verses = Verse.Get(translations, 1, 2, 3);
        assertEquals(2, verses.size());
        assertEquals(1, verses.get(0).getId());
        assertEquals(2, verses.get(1).getId());
        
        Reference begin = new Reference(1, 2, 3);
        Reference end = new Reference(4, 5, 6);
        verses = Verse.Get(translations, begin, end);
        assertEquals(2, verses.size());
        assertEquals(3, verses.get(0).getId());
        assertEquals(4, verses.get(1).getId());
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        assertEquals("text", verse.getText());
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        assertEquals("text", verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getReference());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        assertNotNull(verse.toString());
    }
}