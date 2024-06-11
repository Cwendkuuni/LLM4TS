import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.BeforeClass;

public class VerseTest {
    private static Verse verse;
    
    @BeforeClass
    public static void setUp() {
        verse = Verse.New(1);
    }
    
    @Test
    public void testGetId() {
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetText() {
        assertEquals("Verse 1 text", verse.getText());
    }
    
    @Test
    public void testGetValue() {
        assertEquals("Verse 1 text", verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        assertNotNull(verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        assertNotNull(verse.getReference());
    }
    
    @Test
    public void testToString() {
        String expected = "Verse:[1 Reference Text]";
        assertEquals(expected, verse.toString());
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterVerse() {
        int id = Verse.GetId(1, 2, 3, 4);
        assertEquals(5, id);
    }
    
    @Test
    public void testGetWithTranslationsBookChapterVerse() {
        Translation[] translations = { new Translation(1, "Translation1"), new Translation(2, "Translation2") };
        ArrayList verses = Verse.Get(translations, 2, 3, 4);
        assertEquals(2, verses.size());
    }
    
    @Test
    public void testGetWithTranslationsBeginEnd() {
        Translation[] translations = { new Translation(1, "Translation1"), new Translation(2, "Translation2") };
        Reference begin = new Reference(2, 3, 4);
        Reference end = new Reference(4, 5, 6);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertEquals(3, verses.size());
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterVerseNormalize() {
        int id = Verse.GetId(1001, 2002, 3003, 4004);
        assertEquals(5005, id);
    }
}