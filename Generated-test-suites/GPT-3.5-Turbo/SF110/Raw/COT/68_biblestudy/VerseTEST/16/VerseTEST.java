
import org.junit.Test;
import static org.junit.Assert.*;

 public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }
    
    @Test
    public void testGetId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int result = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(1, result);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "Translation 1");
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
        assertEquals(1, verses.size());
        Verse verse = verses.get(0);
        assertNotNull(verse);
        assertEquals("Text", verse.getText());
    }
    
    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        Reference reference = verse.getReference();
        assertNotNull(reference);
        assertEquals(1, reference.getBookId());
        assertEquals(1, reference.getChapterNo());
        assertEquals(1, reference.getVerseNo());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String toString = verse.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Verse"));
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("TranslationReference"));
        assertTrue(toString.contains("Text"));
    }
    
    @Test
    public void testGetId_withInvalidTranslation() {
        int translationId = -1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int result = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(-1, result);
    }
    
    @Test
    public void testGet_withInvalidTranslations() {
        Translation[] translations = null;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        assertNotNull(verses);
        assertTrue(verses.isEmpty());
    }
    
    @Test
    public void testGet_withInvalidReference() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "Translation 1");
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 1, 1);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertTrue(verses.isEmpty());
    }
    
    @Test
    public void testComplete() {
        DbResult result = new DbResult();
        Verse verse = new Verse(result);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
        assertEquals("Text", verse.getText());
        assertNotNull(verse.getTranslationReference());
    }
    
    @Test
    public void testReadId_withInvalidTranslation() {
        int translationId = -1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int result = Verse.ReadId(translationId, bookId, chapterId, verseId);
        assertEquals(-1, result);
    }
}
