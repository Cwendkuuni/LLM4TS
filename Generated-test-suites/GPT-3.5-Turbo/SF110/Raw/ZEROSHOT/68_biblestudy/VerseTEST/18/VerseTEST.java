import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {

    private Verse verse;
    private TranslationReference transRef;
    
    @Before
    public void setUp() {
        verse = Verse.New(1);
        transRef = new TranslationReference(new Translation(1), new Reference(1, 1, 1));
    }
    
    @Test
    public void testGetId() {
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetText() {
        assertEquals("text", verse.getText());
    }
    
    @Test
    public void testGetValue() {
        assertEquals("text", verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        assertEquals(transRef, verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference() {
        assertEquals(transRef.getReference(), verse.getReference());
    }
    
    @Test
    public void testToString() {
        assertEquals("Verse:[1 " + transRef + " text]", verse.toString());
    }
    
    @Test
    public void testGetId_withValidParameters_returnsId() {
        assertEquals(1, Verse.GetId(new Translation(1), new Book(1), 1, 1));
    }
    
    @Test
    public void testGet_withValidParameters_returnsArrayListOfVerses() {
        Translation[] translations = { new Translation(1) };
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(1, verses.size());
        assertEquals(1, verses.get(0).getId());
    }
    
    @Test
    public void testGet_withReferences_returnsArrayListOfVerses() {
        Translation[] translations = { new Translation(1) };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 2);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertEquals(2, verses.size());
    }
    
    @Test
    public void testGetId_withInvalidTranslationId_returnsNegativeId() {
        assertEquals(-1, Verse.GetId(-1, 1, 1, 1));
    }
    
    @Test
    public void testGetId_withNonexistentTranslation_returnsIdFromDatabase() {
        assertEquals(1, Verse.GetId(1, 1, 1, 1));
    }
    
    @Test
    public void testComplete_withValidResult_setsVerseAttributes() {
        DbResult result = new DbResult();
        result.setInt("Verse_ID", 1);
        result.setString("Text", "text");
        result.setInt("Translation_ID", 1);
        result.setInt("Book_ID", 1);
        result.setInt("Chapter", 1);
        result.setInt("Verse_No", 1);
        Verse verse = new Verse(result);
        assertEquals(1, verse.getId());
        assertEquals("text", verse.getText());
        assertEquals(transRef, verse.getTranslationReference());
    }

}