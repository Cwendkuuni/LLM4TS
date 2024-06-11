import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetId() {
        Translation translation = new Translation(1, "English");
        Book book = new Book(1, "Genesis");
        int chapterId = 1;
        int verseId = 1;
        int id = Verse.GetId(translation, book, chapterId, verseId);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = { new Translation(1, "English") };
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertEquals(1, verses.size());
    }
    
    @Test
    public void testGetWithBeginAndEnd() {
        Translation[] translations = { new Translation(1, "English") };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(1, verses.size());
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        assertEquals("test", verse.getText());
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        assertEquals("test", verse.getValue());
    }
    
    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        TranslationReference transRef = verse.getTranslationReference();
        assertEquals(1, transRef.getTranslation());
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        Reference reference = verse.getReference();
        assertEquals(1, reference.getBookId());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String str = verse.toString();
        assertEquals("Verse:[1 TranslationReference[1 Book[1] 1 1] test]", str);
    }
}