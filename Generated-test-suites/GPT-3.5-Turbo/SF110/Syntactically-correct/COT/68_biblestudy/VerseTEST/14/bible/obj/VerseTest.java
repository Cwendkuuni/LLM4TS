import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;

public class VerseTest {

    // Test New method
    @Test
    public void testNew() {
        Verse verse1 = Verse.New(1);
        Verse verse2 = Verse.New(2);
        
        Assert.assertNotSame(verse1, verse2);
        Assert.assertSame(verse1, Verse.New(1));
    }

    // Test getId method
    @Test
    public void testGetId() {
        Verse verse = Verse.New(1);
        Assert.assertEquals(1, verse.getId());
    }

    // Test getText method
    @Test
    public void testGetText() {
        Verse verse = Verse.New(1);
        
        Assert.assertNotNull(verse.getText());
        Assert.assertNotEquals("", verse.getText().trim());
    }

    // Test getValue method
    @Test
    public void testGetValue() {
        Verse verse = Verse.New(1);
        Assert.assertEquals(verse.getText(), verse.getValue());
    }

    // Test getTranslationReference method
    @Test
    public void testGetTranslationReference() {
        Verse verse = Verse.New(1);
        Assert.assertNotNull(verse.getTranslationReference());
    }

    // Test getReference method
    @Test
    public void testGetReference() {
        Verse verse = Verse.New(1);
        Assert.assertNotNull(verse.getReference());
    }

    // Test toString method
    @Test
    public void testToString() {
        Verse verse = Verse.New(1);
        Assert.assertNotNull(verse.toString());
    }

    // Test GetId method
    @Test
    public void testGetIdWithTranslation() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        int verseIdResult = Verse.GetId(translationId, bookId, chapterId, verseId);
        Assert.assertEquals(1, verseIdResult);
    }

    // Test GetId method with invalid translation
    @Test
    public void testGetIdWithInvalidTranslation() {
        int translationId = -1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        
        int verseIdResult = Verse.GetId(translationId, bookId, chapterId, verseId);
        Assert.assertEquals(-1, verseIdResult);
    }

    // Test GetId method with invalid book
    @Test
    public void testGetIdWithInvalidBook() {
        int translationId = 1;
        int bookId = -1;
        int chapterId = 1;
        int verseId = 1;
        
        int verseIdResult = Verse.GetId(translationId, bookId, chapterId, verseId);
        Assert.assertEquals(-1, verseIdResult);
    }

    // Test GetId method with invalid chapter
    @Test
    public void testGetIdWithInvalidChapter() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = -1;
        int verseId = 1;
        
        int verseIdResult = Verse.GetId(translationId, bookId, chapterId, verseId);
        Assert.assertEquals(-1, verseIdResult);
    }

    // Test GetId method with invalid verse
    @Test
    public void testGetIdWithInvalidVerse() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = -1;
        
        int verseIdResult = Verse.GetId(translationId, bookId, chapterId, verseId);
        Assert.assertEquals(-1, verseIdResult);
    }

    // Test Get method
    @Test
    public void testGet() {
        Translation[] translations = { new Translation(1) };
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        
        Assert.assertNotNull(verses);
        Assert.assertFalse(verses.isEmpty());
    }

    // Test Get method with empty translations
    @Test
    public void testGetWithEmptyTranslations() {
        Translation[] translations = {};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        
        Assert.assertNotNull(verses);
        Assert.assertTrue(verses.isEmpty());
    }

    // Test Get method with invalid book
    @Test
    public void testGetWithInvalidBook() {
        Translation[] translations = { new Translation(1) };
        int bookId = -1;
        int chapterId = 1;
        int verseId = 1;

        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        
        Assert.assertNotNull(verses);
        Assert.assertTrue(verses.isEmpty());
    }

    // Test Get method with invalid chapter
    @Test
    public void testGetWithInvalidChapter() {
        Translation[] translations = { new Translation(1) };
        int bookId = 1;
        int chapterId = -1;
        int verseId = 1;

        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        
        Assert.assertNotNull(verses);
        Assert.assertTrue(verses.isEmpty());
    }

    // Test Get method with invalid verse
    @Test
    public void testGetWithInvalidVerse() {
        Translation[] translations = { new Translation(1) };
        int bookId = 1;
        int chapterId = 1;
        int verseId = -1;

        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        
        Assert.assertNotNull(verses);
        Assert.assertTrue(verses.isEmpty());
    }

    // Test Get method with null translations
    @Test
    public void testGetWithNullTranslations() {
        Translation[] translations = null;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        ArrayList<Verse> verses = Verse.Get(translations, bookId, chapterId, verseId);
        
        Assert.assertNotNull(verses);
        Assert.assertTrue(verses.isEmpty());
    }

    // Test Get method with valid references
    @Test
    public void testGetWithValidReferences() {
        Translation[] translations = { new Translation(1) };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 2, 2);

        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        
        Assert.assertNotNull(verses);
        Assert.assertFalse(verses.isEmpty());
    }

    // Test Get method with invalid begin reference
    @Test
    public void testGetWithInvalidBeginReference() {
        Translation[] translations = { new Translation(1) };
        Reference begin = new Reference(-1, 1, 1);
        Reference end = new Reference(1, 2, 2);

        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        
        Assert.assertNotNull(verses);
        Assert.assertTrue(verses.isEmpty());
    }

    // Test Get method with invalid end reference
    @Test
    public void testGetWithInvalidEndReference() {
        Translation[] translations = { new Translation(1) };
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(-1, 2, 2);

        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        
        Assert.assertNotNull(verses);
        Assert.assertTrue(verses.isEmpty());
    }

    // Test Get method with null translations and valid references
    @Test
    public void testGetWithNullTranslationsAndValidReferences() {
        Translation[] translations = null;
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 2, 2);

        ArrayList<Verse> verses = Verse.Get(translations, begin, end);
        
        Assert.assertNotNull(verses);
        Assert.assertTrue(verses.isEmpty());
    }

    // Test GetId method
    @Test
    public void testReadId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        int verseIdResult = Verse.ReadId(translationId, bookId, chapterId, verseId);
        Assert.assertEquals(1, verseIdResult);
    }

    // Test GetId method with invalid translation
    @Test
    public void testReadIdWithInvalidTranslation() {
        int translationId = -1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        int verseIdResult = Verse.ReadId(translationId, bookId, chapterId, verseId);
        Assert.assertEquals(-1, verseIdResult);
    }

    // Test GetId method with invalid book
    @Test
    public void testReadIdWithInvalidBook() {
        int translationId = 1;
        int bookId = -1;
        int chapterId = 1;
        int verseId = 1;

        int verseIdResult = Verse.ReadId(translationId, bookId, chapterId, verseId);
        Assert.assertEquals(-1, verseIdResult);
    }

    // Test GetId method with invalid chapter
    @Test
    public void testReadIdWithInvalidChapter() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = -1;
        int verseId = 1;

        int verseIdResult = Verse.ReadId(translationId, bookId, chapterId, verseId);
        Assert.assertEquals(-1, verseIdResult);
    }

    // Test GetId method with invalid verse
    @Test
    public void testReadIdWithInvalidVerse() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = -1;

        int verseIdResult = Verse.ReadId(translationId, bookId, chapterId, verseId);
        Assert.assertEquals(-1, verseIdResult);
    }

}