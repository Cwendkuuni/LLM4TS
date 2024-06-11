import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import bible.util.*;

public class VerseTest {
    
    private Verse verse;
    private static final int VALID_ID = 1;
    private static final int INVALID_ID = -1;
    
    @Before
    public void setUp() {
        verse = Verse.New(VALID_ID);
    }
    
    @Test
    public void testNew_ValidId_ReturnsVerseObject() {
        assertNotNull(verse);
        assertEquals(VALID_ID, verse.getId());
    }
    
    @Test
    public void testNew_InvalidId_ReturnsNewVerseObject() {
        Verse invalidVerse = Verse.New(INVALID_ID);
        assertNotNull(invalidVerse);
        assertNotEquals(invalidVerse, verse);
        assertEquals(INVALID_ID, invalidVerse.getId());
    }
    
    @Test
    public void testGetId_ReturnsVerseId() {
        int id = verse.getId();
        assertEquals(VALID_ID, id);
    }
    
    @Test
    public void testGetText_ReturnsVerseText() {
        String text = verse.getText();
        assertNotNull(text);
        assertTrue(text.length() > 0);
    }
    
    @Test
    public void testGetValue_ReturnsVerseText() {
        String value = verse.getValue();
        assertNotNull(value);
        assertTrue(value.length() > 0);
        assertEquals(verse.getText(), value);
    }
    
    @Test
    public void testGetTranslationReference_ReturnsTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
        assertEquals(transRef, verse.getTranslationReference());
    }
    
    @Test
    public void testGetReference_ReturnsReference() {
        Reference ref = verse.getReference();
        assertNotNull(ref);
        assertEquals(ref, verse.getTranslationReference().getReference());
    }
    
    @Test
    public void testToString_ReturnsStringRepresentation() {
        String str = verse.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
        assertEquals(verse.getClass().getName() + ":[" + verse.getId() + " " + verse.getTranslationReference() + " " + verse.getText() + "]", str);
    }
    
    @Test
    public void testGetId_TranslationBookChapterVerse_ReturnsValidId() {
        Translation translation = new Translation();
        Book book = new Book();
        int chapterId = 1;
        int verseId = 1;
        
        int id = Verse.GetId(translation, book, chapterId, verseId);
        assertTrue(id > 0);
    }
    
    @Test
    public void testGet_TranslationsBookChapterVerse_ReturnsArrayListOfVerses() {
        Translation[] translations = {new Translation(), new Translation()};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        ArrayList verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
        assertTrue(verses.size() > 0);
        assertTrue(verses.get(0) instanceof Verse);
    }
    
    @Test
    public void testGet_TranslationsBeginEnd_ReturnsArrayListOfVerses() {
        Translation[] translations = {new Translation(), new Translation()};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 5, 5);
        
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertTrue(verses.size() > 0);
        assertTrue(verses.get(0) instanceof Verse);
    }
    
    @Test
    public void testGetId_TranslationIdBookIdChapterIdVerseId_ReturnsValidId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertTrue(id > 0);
    }
    
    @Test
    public void testComplete_ResultObject_ReturnsValidVerseObject() {
        DbResult result = new DbResult();
        result.setInt("Verse_ID", VALID_ID);
        result.setString("Text", "Sample verse text");
        result.setInt("Translation_ID", 1);
        result.setInt("Book_ID", 1);
        result.setInt("Chapter", 1);
        result.setInt("Verse_No", 1);
        
        try {
            verse.complete(result);
        } catch (Exception e) {
            fail();
        }
        
        assertEquals(VALID_ID, verse.getId());
        assertEquals("Sample verse text", verse.getText());
        assertNotNull(verse.getTranslationReference());
        assertEquals(1, verse.getTranslationReference().getTranslation().getId());
        assertEquals(1, verse.getTranslationReference().getReference().getBookId());
        assertEquals(1, verse.getTranslationReference().getReference().getChapterNo());
        assertEquals(1, verse.getTranslationReference().getReference().getVerseNo());
    }
    
    @Test
    public void testReadId_TranslationIdBookIdChapterIdVerseId_ReturnsValidId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        int id = Verse.ReadId(translationId, bookId, chapterId, verseId);
        assertTrue(id > 0);
    }
}