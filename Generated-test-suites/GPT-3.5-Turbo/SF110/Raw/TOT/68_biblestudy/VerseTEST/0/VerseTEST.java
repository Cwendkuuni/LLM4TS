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
    public void testGetText() {
        Verse verse = new Verse(1);
        assertEquals("Sample Verse Text", verse.getText());
    }

    @Test
    public void testGetValue() {
        Verse verse = new Verse(1);
        assertEquals("Sample Verse Text", verse.getValue());
    }

    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getTranslationReference());
        assertEquals(1, verse.getTranslationReference().getId());
    }

    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        assertNotNull(verse.getReference());
        assertEquals(1, verse.getReference().getBookId());
        assertEquals(1, verse.getReference().getChapterNo());
        assertEquals(1, verse.getReference().getVerseNo());
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        assertNotNull(verse.toString());
        assertEquals("bible.obj.Verse:[1 Sample Translation Reference Sample Verse Text]", verse.toString());
    }

    @Test
    public void testGetTranslations() {
        Translation[] translations = {new Translation(1, "Translation 1"), new Translation(2, "Translation 2")};
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(2, verses.size());
        Verse verse1 = (Verse) verses.get(0);
        assertEquals(1, verse1.getId());
        Verse verse2 = (Verse) verses.get(1);
        assertEquals(1, verse2.getId());
    }

    @Test
    public void testGetReferenceRange() {
        Translation[] translations = {new Translation(1, "Translation 1"), new Translation(2, "Translation 2")};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertEquals(4, verses.size());
        Verse verse1 = (Verse) verses.get(0);
        assertEquals(1, verse1.getId());
        Verse verse2 = (Verse) verses.get(1);
        assertEquals(1, verse2.getId());
        Verse verse3 = (Verse) verses.get(2);
        assertEquals(2, verse3.getId());
        Verse verse4 = (Verse) verses.get(3);
        assertEquals(2, verse4.getId());
    }
}