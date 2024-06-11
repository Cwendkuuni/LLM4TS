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
        assertEquals(1, id);
    }
    
    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        String text = verse.getText();
        assertNotNull(text);
    }
    
    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        String value = verse.getValue();
        assertNotNull(value);
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
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation();
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
    }
    
    @Test
    public void testComplete() {
        DbResult result = null;
        try {
            result = new DbResult();
            result.setInt("Verse_ID", 1);
            result.setString("Text", "Test verse");
            result.setInt("Translation_ID", 1);
            result.setInt("Book_ID", 1);
            result.setInt("Chapter", 1);
            result.setInt("Verse_No", 1);
            Verse verse = new Verse(result);
            assertNotNull(verse);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadId() {
        int id = Verse.ReadId(1, 1, 1, 1);
        assertEquals(1, id);
    }
}