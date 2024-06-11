import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.sql.SQLException;

public class VerseTest {

    @Test
    public void testNew() {
        Verse verse = Verse.New(1);
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetId() {
        int id = Verse.GetId(1, 1, 1, 1);
        assertEquals(1, id);
    }

    @Test
    public void testGetIdTranslationNotFound() {
        int id = Verse.GetId(100, 1, 1, 1);
        assertEquals(-1, id);
    }

    @Test
    public void testGet() {
        Translation[] translations = {new Translation(1), new Translation(2)};
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertEquals(2, verses.size());
    }

    @Test
    public void testGetByReference() {
        Translation[] translations = {new Translation(1), new Translation(2)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 3);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(3, verses.size());
    }

    @Test
    public void testGetByReferenceInvalid() {
        Translation[] translations = {new Translation(1)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 0);
        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        assertEquals(0, verses.size());
    }

    @Test
    public void testComplete() throws SQLException {
        DbResult result = new DbResult(); // Mocked object for testing
        result.setInt("Verse_ID", 1);
        result.setString("Text", "Test");
        result.setInt("Translation_ID", 1);
        result.setInt("Book_ID", 1);
        result.setInt("Chapter", 1);
        result.setInt("Verse_No", 1);

        Verse verse = new Verse(result);
        assertEquals(1, verse.getId());
        assertEquals("Test", verse.getText());
        assertEquals(1, verse.getTranslationReference().getTranslation().getId());
    }

    @Test
    public void testReadId() {
        int id = Verse.ReadId(1, 1, 1, 1);
        assertEquals(1, id);
    }

    @Test
    public void testReadIdNotFound() {
        int id = Verse.ReadId(1, 1, 1, 100);
        assertEquals(-1, id);
    }
}