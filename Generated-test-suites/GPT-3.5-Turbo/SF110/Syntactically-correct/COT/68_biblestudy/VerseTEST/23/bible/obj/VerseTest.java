
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
        assertEquals(id, 0);
    }

    @Test
    public void testGet() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation();
        ArrayList verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(0, verses.size());
    }

    @Test
    public void testGetWithReferences() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation();
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);
        ArrayList verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertEquals(0, verses.size());
    }

}
