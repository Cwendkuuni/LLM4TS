import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = { Mockito.mock(Translation.class) };
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertEquals(1, verses.size());
    }
    
    @Test
    public void testGet_withReference() {
        Translation[] translations = { Mockito.mock(Translation.class) };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertEquals(1, verses.size());
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        String text = verse.getText();
        assertNotNull(text);
    }
    
    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        Reference reference = verse.getReference();
        assertNotNull(reference);
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String str = verse.toString();
        assertNotNull(str);
    }
    
    @Test
    public void testGetId_withTranslation() {
        Translation translation = Mockito.mock(Translation.class);
        Book book = Mockito.mock(Book.class);
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translation, book, chapterId, verseId);
        assertEquals(1, id);
    }
    
    @Test
    public void testGetId_withTranslationId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(1, id);
    }
    
}