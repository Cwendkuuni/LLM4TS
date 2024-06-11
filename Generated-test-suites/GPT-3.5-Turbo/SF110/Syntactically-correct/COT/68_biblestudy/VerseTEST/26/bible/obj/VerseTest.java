
package bible.obj;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerseTest {
    private Verse verse;

    @Before
    public void setUp() {
        verse = new Verse(1);
    }

    @Test
    public void testNew() {
        assertEquals(verse, Verse.New(1));
    }

    @Test
    public void testGetId() {
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetText() {
        assertEquals("example text", verse.getText());
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
        assertNotNull(verse.toString());
    }

    @Test
    public void testGetIdWithTranslationBookChapterVerse() {
        assertEquals(1, Verse.GetId(new Translation(), new Book(), 1, 1));
    }

    @Test
    public void testGetTranslationArrayBookChapterVerse() {
        Translation[] translations = { new Translation() };
        assertFalse(Verse.Get(translations, 1, 1, 1).isEmpty());
    }

    @Test
    public void testGetTranslationArrayBeginEnd() {
        Translation[] translations = { new Translation() };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        assertFalse(Verse.Get(translations, begin, end).isEmpty());
    }

    @Test
    public void testGetIdWithTranslationIdBookChapterVerse() {
        assertEquals(1, Verse.GetId(1, 1, 1, 1));
    }
}