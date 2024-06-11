import org.junit.*;
import static org.junit.Assert.*;

public class VerseTest {
    
    @Test
    public void testGetId() {
        Translation translation = new Translation();
        Book book = new Book();
        int chapterId = 1;
        int verseId = 1;
        
        int result = Verse.GetId(translation, book, chapterId, verseId);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation();
        translations[1] = new Translation();
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        ArrayList result = Verse.Get(translations, bookId, chapterId, verseId);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testGet2() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation();
        translations[1] = new Translation();
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 2, 1);
        
        ArrayList result = Verse.Get(translations, begin, end);
        
        assertEquals(0, result.size());
    }
    
    @Test
    public void testGetId2() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        int result = Verse.GetId(translationId, bookId, chapterId, verseId);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testNew() {
        int id = 1;
        
        Verse result = Verse.New(id);
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse();
        
        String result = verse.getText();
        
        assertNull(result);
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse();
        
        String result = verse.getValue();
        
        assertNull(result);
    }
    
    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse();
        
        TranslationReference result = verse.getTranslationReference();
        
        assertNull(result);
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse();
        
        Reference result = verse.getReference();
        
        assertNull(result);
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse();
        
        String result = verse.toString();
        
        assertNotNull(result);
    }
}