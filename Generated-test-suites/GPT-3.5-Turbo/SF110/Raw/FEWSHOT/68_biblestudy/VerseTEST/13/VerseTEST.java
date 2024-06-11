
package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class VerseTest {

    private static Verse verse;

    @BeforeClass
    public static void setUp() {
        verse = Verse.New(1);
        verse.getTranslationReference().setReference(new Reference(1, 1, 1));
    }

    @Test
    public void testNew() {
        Verse newVerse = Verse.New(2);
        assertNotNull(newVerse);
        assertTrue(newVerse instanceof Verse);
        assertEquals(newVerse, Verse.New(2));
    }

    @Test
    public void testGetId() {
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetText() {
        assertNotNull(verse.getText());
    }

    @Test
    public void testGetValue() {
        assertEquals(verse.getText(), verse.getValue());
    }

    @Test
    public void testGetTranslationReference() {
        assertNotNull(verse.getTranslationReference());
    }

    @Test
    public void testGetReference() {
        assertNotNull(verse.getReference());
    }

    @Test
    public void testToString() {
        String expectedString = "bible.obj.Verse:[1 null null]";
        assertEquals(expectedString, verse.toString());
    }

    @Test
    public void testGetIdWithParameters() {
        assertEquals(1, Verse.GetId(null, 1, 1, 1));
        assertEquals(1, Verse.GetId(1, 1, 1, 1));
        assertEquals(1, Verse.GetId(1, 1, 1, 2));
    }

    @Test
    public void testGetWithTranslationsArray() {
        ArrayList verses = Verse.Get(null, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(0, verses.size());
    }

    @Test
    public void testGetWithTranslationsArrayAndReference() {
        ArrayList verses = Verse.Get(null, null, null);
        assertNotNull(verses);
        assertEquals(0, verses.size());
    }

    @Test
    public void testGetIdWithoutTranslationId() {
        assertEquals(1, Verse.GetId(0, 1, 1, 1));
    }

    @Test
    public void testGetIdWithoutBookId() {
        assertEquals(1, Verse.GetId(1, 0, 1, 1));
    }

    @Test
    public void testGetIdWithoutChapterId() {
        assertEquals(1, Verse.GetId(1, 1, 0, 1));
    }

    @Test
    public void testGetIdWithoutVerseId() {
        assertEquals(1, Verse.GetId(1, 1, 1, 0));
    }

    @Test
    public void testReadId() {
        int id = Verse.ReadId(1, 1, 1, 1);
        assertEquals(1, id);
        id = Verse.ReadId(0, 1, 1, 1);
        assertEquals(-1, id);
    }
}
