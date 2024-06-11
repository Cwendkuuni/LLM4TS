import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerseTest {
    
    private Verse verse;
    
    @Before
    public void setUp() {
        verse = new Verse(1);
    }
    
    @Test
    public void testNew() {
        Verse newVerse = Verse.New(1);
        Verse cachedVerse = Verse.verseCache.get(1);
        assertNotNull("Newly created verse should not be null", newVerse);
        assertEquals("The id of newly created verse should match the given id", 1, newVerse.getId());
        assertNotNull("Verse retrieved from cache should not be null", cachedVerse);
        assertEquals("The id of verse retrieved from cache should match the given id", 1, cachedVerse.getId());
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertNotNull("Id should not be null", id);
        assertEquals("The returned id should match the expected id", 1, id);
    }
    
    @Test
    public void testGetText() {
        String text = verse.getText();
        assertNotNull("Text should not be null", text);
        assertFalse("Text should not be empty", text.isEmpty());
    }
    
    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertNotNull("Value should not be null", value);
        assertFalse("Value should not be empty", value.isEmpty());
        assertEquals("The value should match the text", verse.getText(), value);
    }
    
    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull("Translation reference should not be null", transRef);
    }
    
    @Test
    public void testGetReference() {
        Reference ref = verse.getReference();
        assertNotNull("Reference should not be null", ref);
    }
    
    @Test
    public void testToString() {
        String str = verse.toString();
        assertNotNull("String representation should not be null", str);
        assertFalse("String representation should not be empty", str.isEmpty());
        assertTrue("String representation should contain the id", str.contains(String.valueOf(verse.getId())));
        assertTrue("String representation should contain the translation reference", str.contains(verse.getTranslationReference().toString()));
        assertTrue("String representation should contain the text", str.contains(verse.getText()));
        assertTrue("String representation should contain the class name", str.contains(Verse.class.getName()));
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation(1);
        translations[1] = new Translation(2);
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        ArrayList verses = Verse.Get(translations, bookId, chapterId, verseId);
        
        assertNotNull("Verses should not be null", verses);
        assertTrue("Verses should not be empty", verses.size() > 0);
        assertTrue("The number of verses returned should match the number of translations", verses.size() == translations.length);
        
        for (Object o : verses) {
            assertTrue("Each verse should be an instance of Verse", o instanceof Verse);
            Verse v = (Verse) o;
            assertEquals("The verse id should match the given id", 1, v.getId());
        }
    }
    
    @Test
    public void testGetRange() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation(1);
        translations[1] = new Translation(2);
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 2, 3);
        
        ArrayList verses = Verse.Get(translations, begin, end);
        
        assertNotNull("Verses should not be null", verses);
        assertTrue("Verses should not be empty", verses.size() > 0);
        
        // Verify that the returned verses have the expected references
        for (Object o : verses) {
            assertTrue("Each verse should be an instance of Verse", o instanceof Verse);
            Verse v = (Verse) o;
            Reference ref = v.getReference();
            assertTrue("The reference should be between the begin and end references", ref.isBetween(begin, end));
        }
    }
}