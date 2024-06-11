import org.junit.*;
import static org.junit.Assert.*;

public class VerseTest {

    private Verse verse;
    
    @Before
    public void setUp() {
        verse = new Verse(1);
    }
    
    @After
    public void tearDown() {
        verse = null;
    }
    
    @Test
    public void testNew() {
        Verse newVerse = Verse.New(1);
        assertNotNull(newVerse);
        assertEquals(1, newVerse.getId());
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(1, verses.size());
        assertEquals(1, verses.get(0).getId());
    }
    
    @Test
    public void testGetWithReferences() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1);
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 10, 10);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertEquals(100, verses.size());
        // Test first and last verses
        assertEquals(1, verses.get(0).getId());
        assertEquals(100, verses.get(99).getId());
    }
    
    @Test
    public void testGetText() {
        String text = verse.getText();
        assertNotNull(text);
    }
    
    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertNotNull(value);
        assertEquals(verse.getText(), value);
    }
    
    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
    }
    
    @Test
    public void testGetReference() {
        Reference reference = verse.getReference();
        assertNotNull(reference);
    }
    
    @Test
    public void testToString() {
        String expected = "Verse:[" + verse.getId() + " " + verse.getTranslationReference() + " " + verse.getText() + "]";
        String result = verse.toString();
        assertNotNull(result);
        assertEquals(expected, result);
    }
}