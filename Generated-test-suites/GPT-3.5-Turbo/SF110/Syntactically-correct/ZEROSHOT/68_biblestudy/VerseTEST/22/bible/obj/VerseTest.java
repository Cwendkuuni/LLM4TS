
package bible.obj;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

public class VerseTest {

    private Verse verse;

    @Before
    public void setup() {
        verse = new Verse(1);
    }

    @Test
    public void testGetId() {
        int expectedId = 1;
        int actualId = verse.getId();
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetText() {
        String expectedText = "";
        String actualText = verse.getText();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testGetValue() {
        String expectedValue = "";
        String actualValue = verse.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetTranslationReference() {
        TranslationReference expectedTransRef = null;
        TranslationReference actualTransRef = verse.getTranslationReference();
        assertEquals(expectedTransRef, actualTransRef);
    }

    @Test
    public void testGetReference() {
        Reference expectedRef = null;
        Reference actualRef = verse.getReference();
        assertEquals(expectedRef, actualRef);
    }

    @Test
    public void testToString() {
        String expectedString = "bible.obj.Verse:[1 null ]";
        String actualString = verse.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetIdWithTranslation() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = -1;
        int actualId = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetWithTranslationsAndIds() {
        Translation[] translations = new Translation[1];
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        ArrayList<Verse> expectedVerses = new ArrayList<>();
        ArrayList<Verse> actualVerses = Verse.Get(translations, bookId, chapterId, verseId);
        assertEquals(expectedVerses, actualVerses);
    }

    @Test
    public void testGetWithTranslationsAndReferences() {
        Translation[] translations = new Translation[1];
        Reference begin = null;
        Reference end = null;
        ArrayList<Verse> expectedVerses = new ArrayList<>();
        ArrayList<Verse> actualVerses = Verse.Get(translations, begin, end);
        assertEquals(expectedVerses, actualVerses);
    }

    @Test
    public void testGetIdWithTranslationAndIds() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = -1;
        int actualId = Verse.GetId(translationId, bookId, chapterId, verseId);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testReadId() {
        int translationId = 1;
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;
        int expectedId = -1;
        int actualId = Verse.ReadId(translationId, bookId, chapterId, verseId);
        assertEquals(expectedId, actualId);
    }
}

