import static org.junit.Assert.*;
import org.junit.*;

public class VerseTest {

    private Verse verse;
    
    @Before
    public void setUp() {
        verse = Verse.New(1);
    }
    
    @Test
    public void testNew() {
        assertNotNull(verse);
    }
    
    @Test
    public void testGetId() {
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetText() {
        assertNotNull(verse.getText());
    }
    
    @Test
    public void testGetValue() {
        assertEquals(verse.getText(), verse.getValue());
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
        assertNotNull(verse.toString());
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterAndVerse() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = 1;
        
        assertEquals(expectedId, Verse.GetId(translationId, bookId, chapterId, verseId));
    }
    
    @Test
    public void testGetWithTranslationsBookChapterAndVerse() {
        Translation[] translations = {new Translation(1, "Test Translation")};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        assertFalse(Verse.Get(translations, bookId, chapterId, verseId).isEmpty());
    }
    
    @Test
    public void testGetWithTranslationsBeginAndEnd() {
        Translation[] translations = {new Translation(1, "Test Translation")};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        
        assertFalse(Verse.Get(translations, begin, end).isEmpty());
    }
    
    @Test
    public void testGetIdWithTranslationBookChapterAndVerseNotFound() {
        int translationId = 1;
        int bookId = 100;
        int chapterId = 100;
        int verseId = 100;
        int expectedId = -1;
        
        assertEquals(expectedId, Verse.GetId(translationId, bookId, chapterId, verseId));
    }
    
    @After
    public void tearDown() {
        verse = null;
    }
}