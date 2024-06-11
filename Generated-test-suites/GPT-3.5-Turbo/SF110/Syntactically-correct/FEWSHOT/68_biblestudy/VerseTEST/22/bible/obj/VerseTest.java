package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 2, 3, 4);
        assertEquals(1, id);
    }

    @Test
    public void testGet() {
        Translation[] translations = {new Translation("English", 1)};
        ArrayList verses = Verse.Get(translations, 1, 2, 3);
        assertNotNull(verses);
    }

    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getReference());
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String expected = "bible.obj.Verse:[1 Translation:[] 123]";
        assertEquals(expected, verse.toString());
    }

    @Test
    public void testComplete() {
        DbResult result = new DbResult();
        result.setInt("Verse_ID", 1);
        result.setString("Text", "Test Verse");
        result.setInt("Translation_ID", 1);
        result.setInt("Book_ID", 1);
        result.setInt("Chapter", 2);
        result.setInt("Verse_No", 3);
        Verse verse = new Verse(result);
        assertEquals(1, verse.getId());
        assertEquals("Test Verse", verse.getText());
        assertNotNull(verse.getTranslationReference());
    }

    @Test
    public void testReadId() {
        int id = Verse.ReadId(1, 1, 2, 3);
        assertEquals(-1, id);
    }
}