package bible.obj;

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
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }

    @Test
    public void testGet() {
        Translation[] translations = { new Translation(1), new Translation(2) };
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(2, verses.size());
    }

    @Test
    public void testGetWithReference() {
        Translation[] translations = { new Translation(1), new Translation(2) };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 4);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertEquals(4, verses.size());
    }

    @Test
    public void testGetText() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals("text", verse.getText());
    }

    @Test
    public void testGetTranslationReference() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertNotNull(verse.getTranslationReference());
    }

    @Test
    public void testGetReference() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertNotNull(verse.getReference());
    }

    @Test
    public void testToString() {
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        assertEquals("bible.obj.Verse:[1 translationReference text]", verse.toString());
    }
}