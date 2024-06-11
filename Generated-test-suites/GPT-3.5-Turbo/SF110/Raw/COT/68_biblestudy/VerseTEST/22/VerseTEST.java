
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class VerseTest {

    private static Verse verse;

    @BeforeClass
    public static void setUp() {
        verse = new Verse(1);
    }

    @Test
    public void testNew() {
        Verse newVerse = Verse.New(1);
        assertNotNull(newVerse);
        assertSame(verse, newVerse);
    }

    @Test
    public void testGetId() {
        int id = verse.getId();
        assertEquals(1, id);
    }

    @Test
    public void testGetText() {
        String text = verse.getText();
        assertNotNull(text);
    }

    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertNotNull(value);
    }

    @Test
    public void testGetTranslationReference() {
        TranslationReference transRef = verse.getTranslationReference();
        assertNotNull(transRef);
    }

    @Test
    public void testGetReference() {
        Reference reference = verse.getReference();
        assertNotNull(reference);
    }

    @Test
    public void testToString() {
        String string = verse.toString();
        assertNotNull(string);
    }

    @Test
    public void testGetIdWithArgs() {
        int id = Verse.GetId(new Translation(), new Book(), 1, 1);
        assertEquals(0, id);
    }

    @Test
    public void testGet() {
        Translation[] translations = { new Translation() };
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetWithReferences() {
        Translation[] translations = { new Translation() };
        Reference begin = new Reference();
        Reference end = new Reference();
        ArrayList verses = Verse.Get(translations, begin, end);
        assertTrue(verses.isEmpty());
    }

    @Test
    public void testGetIdWithArgsAndTranslations() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(0, id);
    }
}
