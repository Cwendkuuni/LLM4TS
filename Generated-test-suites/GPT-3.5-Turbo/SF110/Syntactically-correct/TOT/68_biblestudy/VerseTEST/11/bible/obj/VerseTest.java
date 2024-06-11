
import org.junit.*;
import static org.junit.Assert.*;
import bible.util.Reference;
import bible.util.SampleTranslations;
import bible.util.Translation;
import bible.util.Util;
import bible.util.DbResult;

public class VerseTest {
    private Translation translation;
    private Verse verse;
    
    @Before
    public void setUp() {
        translation = SampleTranslations.getTranslation();
        verse = new Verse(1);
    }
    
    @Test
    public void testNew() {
        Verse newVerse = Verse.New(1);
        
        assertSame(verse, newVerse);
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        
        assertEquals(1, id);
    }
    
    @Test
    public void testGetId_invalidTranslationId() {
        int id = Verse.GetId(-1, 1, 1, 1);
        
        assertEquals(-1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = {translation};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        ArrayList verseList = Verse.Get(translations, bookId, chapterId, verseId);
        
        assertEquals(1, verseList.size());
    }
    
    @Test
    public void testGet_invalidTranslation() {
        Translation[] translations = {translation};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        ArrayList verseList = Verse.Get(translations, bookId, chapterId, verseId);
        
        assertEquals(0, verseList.size());
    }
    
    @Test
    public void testGetReference() {
        Reference reference = verse.getReference();
        
        assertNotNull(reference);
    }
    
    @Test
    public void testToString() {
        String text = "This is a sample text.";
        verse.text = text;
        
        String verseString = verse.toString();
        
        assertEquals("Verse:[" + verse.id + " " + verse.transRef + " " + text + "]", verseString);
    }
    
    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        
        assertNotNull(transRef);
    }
}

