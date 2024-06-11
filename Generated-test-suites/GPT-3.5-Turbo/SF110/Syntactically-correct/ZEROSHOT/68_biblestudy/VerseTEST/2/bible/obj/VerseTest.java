import org.junit.*;
import static org.junit.Assert.*;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
    }
    
    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(0, id);
    }
    
    @Test
    public void testGet() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation();
        translations[1] = new Translation();
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
    }
    
    @Test
    public void testGetWithReferences() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation();
        translations[1] = new Translation();
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
    }
    
    @Test
    public void testGetIdWithTranslation() {
        int id = Verse.GetId(new Translation(), new Book(), 1, 1);
        assertEquals(0, id);
    }
    
    @Test
    public void testGetIdWithDifferentTranslation() {
        int id = Verse.GetId(new Translation(), new Book(), 2, 2);
        assertEquals(0, id);
    }
    
    @Test
    public void testComplete() {
        DbResult result = new DbResult();
        result.setInt("Verse_ID", 1);
        result.setString("Text", "Verse 1");
        result.setInt("Translation_ID", 1);
        result.setInt("Book_ID", 1);
        result.setInt("Chapter", 1);
        result.setInt("Verse_No", 1);
        Verse verse = new Verse(result);
        assertNotNull(verse);
        assertEquals("Verse 1", verse.getText());
        assertNotNull(verse.getTranslationReference());
        assertEquals(1, verse.getReference().getBookId());
    }
}