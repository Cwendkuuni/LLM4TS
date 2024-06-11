import org.junit.*;
import static org.junit.Assert.*;

public class VerseTest {

    @Test
    public void testNew() {
        // Test when verse is not already in verseCache
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        
        // Test when verse is already in verseCache
        Verse.verseCache.put(1, verse);
        Verse verse2 = Verse.New(1);
        assertSame(verse, verse2);
    }

    @Test
    public void testGetId() {
        // Test when translation is not in translations map
        int id = Verse.GetId(1, 1, 1, 1);
        assertTrue(id > 0);

        // Test when translation is already in translations map
        HashMap<Integer, HashMap<Integer, HashMap<Integer, Integer>>> translations = new HashMap<>();
        Verse.translations.put(1, translations);
        int id2 = Verse.GetId(1, 1, 1, 1);
        assertEquals(id, id2);
    }

    @Test
    public void testGet() {
        // Test with translations array and bookId, chapterId, verseId
        Translation[] translations = new Translation[2];
        translations[0] = new Translation(1, "Translation 1");
        translations[1] = new Translation(2, "Translation 2");
        ArrayList<Verse> verses = Verse.Get(translations, 1, 1, 1);
        assertNotNull(verses);
        assertEquals(2, verses.size());

        // Test with translations array and begin Reference, end Reference
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 5);
        verses = Verse.Get(translations, begin, end);
        assertNotNull(verses);
        assertEquals(5, verses.size());
    }

    @Test
    public void testComplete() {
        // Test with DbResult containing valid data
        DbResult result = new DbResult();
        result.setInt("Verse_ID", 1);
        result.setString("Text", "Verse 1");
        result.setInt("Translation_ID", 1);
        result.setInt("Book_ID", 1);
        result.setInt("Chapter", 1);
        result.setInt("Verse_No", 1);
        try {
            Verse verse = new Verse(result);
            assertNotNull(verse);
            assertEquals(1, verse.getId());
            assertEquals("Verse 1", verse.getText());
            assertNotNull(verse.getTranslationReference());
        } catch(SQLException e) {
            fail("Should not throw SQLException");
        }

        // Test with DbResult containing invalid data
        result = new DbResult();
        try {
            Verse verse = new Verse(result);
            assertNotNull(verse);
            assertEquals(0, verse.getId());
            assertNull(verse.getText());
            assertNull(verse.getTranslationReference());
        } catch(SQLException e) {
            fail("Should not throw SQLException");
        }
    }

    @Test
    public void testToString() {
        Verse verse = new Verse(1);
        String str = verse.toString();
        assertNotNull(str);
        assertFalse(str.isEmpty());
    }
}