import org.junit.*;
import static org.junit.Assert.*;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse1 = Verse.New(1);
        Verse verse2 = Verse.New(1);
        assertEquals(verse1, verse2);
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        assertEquals("Test Text", verse.getText());
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        assertEquals("Test Text", verse.getValue());
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
        assertEquals("Verse:[1 TestReference Test Text]", verse.toString());
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "Test Translation");
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertEquals(1, verses.size());
    }
    
    @Test
    public void testGetWithReferences() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "Test Translation");
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 2, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertEquals(2, verses.size());
    }
}