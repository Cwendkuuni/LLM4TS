import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

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
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(1, verses.size());
        assertEquals(1, ((Verse)verses.get(0)).getId());
    }
    
    @Test
    public void testGetWithReference() {
        Translation[] translations = {new Translation()};
        ArrayList verses = Verse.Get(translations, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertNotNull(verses);
        assertEquals(1, verses.size());
        assertEquals(1, ((Verse)verses.get(0)).getId());
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        assertEquals("text", verse.getText());
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
        String expected = "Verse:[1 translation reference text]";
        assertEquals(expected, verse.toString());
    }
}