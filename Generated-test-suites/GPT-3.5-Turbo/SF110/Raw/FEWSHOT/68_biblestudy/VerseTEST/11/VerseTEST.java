package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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
        id = Verse.GetId(1, 2, 2, 2);
        assertEquals(-1, id);
    }

    @Test
    public void testGet() {
        Translation[] translations = { new Translation(1, "translation1"), new Translation(2, "translation2") };
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertEquals(2, verses.size());
        assertTrue(verses.get(0) instanceof Verse);
    }

    @Test
    public void testGetWithReference() {
        Translation[] translations = { new Translation(1, "translation1"), new Translation(2, "translation2") };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertTrue(verses.isEmpty());
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
}