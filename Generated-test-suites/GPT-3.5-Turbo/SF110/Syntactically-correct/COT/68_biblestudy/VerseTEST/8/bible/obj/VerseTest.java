
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VerseTest {
    private Verse verse;

    @Before
    public void setUp() {
        verse = new Verse(1);
    }

    @Test
    public void testNew() {
        Verse newVerse = Verse.New(1);
        assertNotNull(newVerse);
        assertEquals(1, newVerse.getId());
    }

    @Test
    public void testGetId() {
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetText() {
        String text = verse.getText();
        assertNotNull(text);
        assertTrue(text.length() > 0);
    }

    @Test
    public void testGetValue() {
        String value = verse.getValue();
        assertNotNull(value);
        assertEquals(verse.getText(), value);
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
        String str = verse.toString();
        assertNotNull(str);
        assertTrue(str.contains(verse.getClass().getName()));
        assertTrue(str.contains(Integer.toString(verse.getId())));
        assertTrue(str.contains(verse.getTranslationReference().toString()));
        assertTrue(str.contains(verse.getText()));
    }

    @Test
    public void testGetIdWithTranslationBookChapterVerse() {
        int id = Verse.GetId(new Translation(), new Book(), 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGetWithTranslationsBookChapterVerse() {
        ArrayList<Verse> verses = Verse.Get(new Translation[]{new Translation()}, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(1, verses.size());
        assertEquals(1, verses.get(0).getId());
    }

    @Test
    public void testGetWithTranslationsBeginEnd() {
        ArrayList<Verse> verses = Verse.Get(new Translation[]{new Translation()}, new Reference(1, 1, 1), new Reference(1, 1, 1));
        assertNotNull(verses);
        assertEquals(1, verses.size());
        assertEquals(1, verses.get(0).getId());
    }
}
