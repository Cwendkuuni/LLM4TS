
package bible.obj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class VerseTest {
    private static Verse verse;

    @Before
    public void setUp() {
        verse = new Verse(1);
    }

    @Test
    public void testGetId() {
        assertEquals(1, verse.getId());
    }

    @Test
    public void testGetText() {
        assertNotNull(verse.getText());
    }

    @Test
    public void testGetValue() {
        assertNotNull(verse.getValue());
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
    public void testGetIdWithTranslationAndBookAndChapterAndVerse() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int result = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertTrue(result > 0);
    }

    @Test
    public void testGetWithTranslationsAndBookIdAndChapterIdAndVerseId() {
        Translation[] translations = new Translation[2];
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        assertNotNull(Verse.Get(translations, bookId, chapterId, verseId));
    }

    @Test
    public void testGetWithTranslationsAndBeginAndEnd() {
        Translation[] translations = new Translation[2];
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(1, 1, 1);
        assertNotNull(Verse.Get(translations, begin, end));
    }
}
