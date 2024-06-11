import static org.junit.Assert.*;
import org.junit.Test;

public class VerseTest {

    @Test
    public void testNew() {
        // Test if verse is fetched from verseCache
        Verse verse = Verse.New(1);
        assertNotNull(verse);
        Verse cachedVerse = Verse.verseCache.get(1);
        assertNotNull(cachedVerse);
        assertSame(verse, cachedVerse);

        // Test if verse is created if not in verseCache
        Verse verse2 = Verse.New(2);
        assertNotNull(verse2);
        Verse cachedVerse2 = Verse.verseCache.get(2);
        assertNotNull(cachedVerse2);
        assertSame(verse2, cachedVerse2);
        assertNotSame(verse, verse2);
    }

    @Test
    public void testGetId() {
        // Test if translation, book, chapter, and verse IDs are correctly normalized and retrieved
        // Test with valid IDs
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = 1111;
        int actualId = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(expectedId, actualId);

        // Test with invalid IDs
        int invalidTranslationId = 999;
        int invalidBookId = 999;
        int invalidChapterId = 999;
        int invalidVerseId = 999;
        int expectedInvalidId = -1;
        int actualInvalidId = Verse.GetId(invalidTranslationId, invalidBookId, invalidChapterId, invalidVerseId);
        assertEquals(expectedInvalidId, actualInvalidId);
    }

    @Test
    public void testGet() {
        // Test if an ArrayList of verses is correctly retrieved based on translation, book, chapter, and verse IDs

        // Test with valid IDs
        Translation[] translations = new Translation[]{Translation.GetInstance(1), Translation.GetInstance(2)};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> expectedVerses = new ArrayList<>();
        expectedVerses.add(Verse.New(1111));
        expectedVerses.add(Verse.New(2222));
        ArrayList<Verse> actualVerses = Verse.Get(translations, bookId, chapterId, verseId);
        assertEquals(expectedVerses, actualVerses);

        // Test with invalid IDs
        Translation[] invalidTranslations = new Translation[]{};
        int invalidBookId = 999;
        int invalidChapterId = 999;
        int invalidVerseId = 999;
        ArrayList<Verse> expectedInvalidVerses = new ArrayList<>();
        ArrayList<Verse> actualInvalidVerses = Verse.Get(invalidTranslations, invalidBookId, invalidChapterId, invalidVerseId);
        assertEquals(expectedInvalidVerses, actualInvalidVerses);
    }

    @Test
    public void testGetRange() {
        // Test if an ArrayList of verses is correctly retrieved based on translation and a range of references

        // Test with valid references
        Translation[] translations = new Translation[]{Translation.GetInstance(1), Translation.GetInstance(2)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 2, 3);
        ArrayList<Verse> expectedVerses = new ArrayList<>();
        expectedVerses.add(Verse.New(1111));
        expectedVerses.add(Verse.New(2222));
        expectedVerses.add(Verse.New(3333));
        ArrayList<Verse> actualVerses = Verse.Get(translations, begin, end);
        assertEquals(expectedVerses, actualVerses);

        // Test with invalid references
        Translation[] invalidTranslations = new Translation[]{};
        Reference invalidBegin = new Reference(999, 1, 1);
        Reference invalidEnd = new Reference(999, 1, 1);
        ArrayList<Verse> expectedInvalidVerses = new ArrayList<>();
        ArrayList<Verse> actualInvalidVerses = Verse.Get(invalidTranslations, invalidBegin, invalidEnd);
        assertEquals(expectedInvalidVerses, actualInvalidVerses);
    }

    @Test
    public void testComplete() {
        // Test if Verse object is correctly completed with values from a DbResult object

        // Test with valid DbResult
        DbResult result = new DbResult();
        result.addResultColumn("Verse_ID", new Integer(1));
        result.addResultColumn("Text", "This is a verse.");
        result.addResultColumn("Translation_ID", new Integer(1));
        result.addResultColumn("Book_ID", new Integer(1));
        result.addResultColumn("Chapter", new Integer(1));
        result.addResultColumn("Verse_No", new Integer(1));
        Verse verse = new Verse(result);
        assertEquals(1, verse.getId());
        assertEquals("This is a verse.", verse.getText());
        assertEquals(Translation.Get(1), verse.getTranslationReference().getTranslation());
        assertEquals(new Reference(1, 1, 1), verse.getTranslationReference().getReference());

        // Test with invalid DbResult
        DbResult invalidResult = new DbResult();
        Verse invalidVerse = new Verse(invalidResult);
        assertEquals(0, invalidVerse.getId());
        assertNull(invalidVerse.getText());
        assertNull(invalidVerse.getTranslationReference());
    }

}