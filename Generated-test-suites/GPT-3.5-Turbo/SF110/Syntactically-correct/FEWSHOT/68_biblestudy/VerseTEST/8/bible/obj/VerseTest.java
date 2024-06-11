import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class VerseTest {
    
    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        Verse cachedVerse = Verse.verseCache.get(new Integer(1));
        
        assertEquals(verse, cachedVerse);
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation("Translation1");
        translations[1] = new Translation("Translation2");
        
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        
        assertEquals(2, verses.size());
    }
    
    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        
        String expected = "Verse:[1 TranslationReference Text]";
        String actual = verse.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComplete() {
        DbResult result = new DbResult();
        result.setInt("Verse_ID", 1);
        result.setString("Text", "VerseText");
        result.setInt("Translation_ID", 1);
        result.setInt("Book_ID", 1);
        result.setInt("Chapter", 1);
        result.setInt("Verse_No", 1);
        
        Verse verse = new Verse(result);
        
        assertEquals(1, verse.getId());
        assertEquals("VerseText", verse.getText());
        assertEquals(1, verse.getTranslationReference().getTranslation().getId());
        assertEquals(1, verse.getTranslationReference().getReference().getBookId());
        assertEquals(1, verse.getTranslationReference().getReference().getChapterNo());
        assertEquals(1, verse.getTranslationReference().getReference().getVerseNo());
    }

}