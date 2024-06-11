import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import bible.obj.*;
import bible.util.*;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
    }
    
    @Test
    public void testGetId() {
        Translation translation = new Translation(1, "Test");
        Book book = new Book(1, "Genesis");
        int chapterId = 1;
        int verseId = 1;
        
        int id = Verse.GetId(translation, book, chapterId, verseId);
        assertEquals(id, 1);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = {new Translation(1, "Test1"), new Translation(2, "Test2")};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
    }
    
    @Test
    public void testGetWithReference() {
        Translation[] translations = {new Translation(1, "Test1"), new Translation(2, "Test2")};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 5);
        
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
    }
    
    @Test
    public void testGetIdWithInvalidTranslation() {
        int translationId = -1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(id, -1);
    }
    
    @Test
    public void testExceptionHandling() {
        try {
            Verse.GetId(null, 1, 1, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}