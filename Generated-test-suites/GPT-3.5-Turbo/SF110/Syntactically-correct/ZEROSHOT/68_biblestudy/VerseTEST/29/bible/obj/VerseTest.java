import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import bible.util.Translation;
import bible.util.Reference;

public class VerseTest {
    private static Verse verse;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        verse = new Verse(1);
    }
    
    @Test
    public void testGetId() {
        int id = verse.getId();
        assertEquals(1, id);
    }
    
    @Test
    public void testGetText() {
        String text = verse.getText();
        assertEquals("Test text", text);
    }
    
    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertEquals("Test text", value);
    }
    
    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
    }
    
    @Test
    public void testGetReference() {
        Reference ref = verse.getReference();
        assertNotNull(transRef);
    }
    
    @Test
    public void testToString() {
        String str = verse.toString();
        assertNotNull(str);
    }
    
    @Test
    public void testGetIdWithParams() {
        int id = Verse.GetId(new Translation(), new Book(), 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation();
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertEquals(1, verses.size());
        assertEquals(1, verses.get(0).getId());
    }
    
    @Test
    public void testGetWithReferences() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation();
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(2, verses.size());
        assertEquals(1, verses.get(0).getId());
        assertEquals(2, verses.get(1).getId());
    }
    
    @Test
    public void testGetIdWithParams() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
}