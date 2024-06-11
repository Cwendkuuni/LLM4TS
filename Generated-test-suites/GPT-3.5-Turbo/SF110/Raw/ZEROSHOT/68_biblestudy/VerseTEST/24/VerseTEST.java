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
    public void testGet() {
        Translation[] translations = {new Translation(), new Translation()};
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(2, verses.size());
    }

    @Test
    public void testGet2() {
        Translation[] translations = {new Translation(), new Translation()};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertEquals(4, verses.size());
    }

    @Test
    public void testGetId2() {
        int id = Verse.GetId(new Translation(), new Book(), 1, 1);
        assertEquals(1, id);
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String expected = "Verse:[" + verse.getId() + " " + verse.getTranslationReference() + " " + verse.getText() + "]";
        assertEquals(expected, verse.toString());
    }

    @Test
    public void testGetTranslationReference() {
        Verse verse = new Verse(1);
        TranslationReference translationReference = verse.getTranslationReference();
        assertNotNull(translationReference);
    }

    @Test
    public void testGetReference() {
        Verse verse = new Verse(1);
        Reference reference = verse.getReference();
        assertNotNull(reference);
    }

    @Test
    public void testGetId3() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }

    @Test
    public void testGetId4() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }
}