import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

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
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "Translation1");
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertFalse(verses.isEmpty());
    }

    @Test
    public void testGetByRange() {
        Translation[] translations = new Translation[1];
        translations[0] = new Translation(1, "Translation1");
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 5);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertFalse(verses.isEmpty());
    }

    @Test
    public void testGetIdWithInvalidInput() {
        int id = Verse.GetId(-1, -1, -1, -1);
        assertEquals(-1, id);
    }

    @Test
    public void testNewWithNonExistingId() {
        Verse verse = Verse.New(-1);
        assertNotNull(verse);
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}