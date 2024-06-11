import org.junit.*;
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
        int verseId = Verse.GetId(1, 1, 1, 1);
        assertNotNull(verseId);
        assertEquals(1, verseId);
    }

    @Test
    public void testGetText() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getText());
        assertEquals("Sample text", verse.getText());
    }

    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getValue());
        assertEquals("Sample text", verse.getValue());
    }

    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getTranslationReference());
        assertEquals("Sample translation reference", verse.getTranslationReference());
    }

    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getReference());
        assertEquals("Sample reference", verse.getReference());
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        assertNotNull(verse.toString());
        assertEquals("Verse:[1 Sample translation reference Sample text]", verse.toString());
    }

    @Test
    public void testGet() {
        Translation[] translations = new Translation[2];
        translations[0] = new Translation(1, "Translation 1");
        translations[1] = new Translation(2, "Translation 2");
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(2, verses.size());
    }
}