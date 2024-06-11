import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VerseTest {
    private Verse verse;

    @Before
    public void setUp() {
        verse = Verse.New(1);
    }

    @Test
    public void testGetId() {
        int expectedId = 1;
        int actualId = verse.getId();
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetText() {
        String expectedText = "Test Verse";
        String actualText = verse.getText();
        assertEquals(expectedText, actualText);
    }

    @Test
    public void testGetValue() {
        String expectedValue = verse.getText();
        String actualValue = verse.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetTranslationReference() {
        TranslationReference expectedTransRef = new TranslationReference(Translation.Get(1), new Reference(1, 1, 1));
        TranslationReference actualTransRef = verse.getTranslationReference();
        assertEquals(expectedTransRef, actualTransRef);
    }

    @Test
    public void testGetReference() {
        Reference expectedRef = verse.getTranslationReference().getReference();
        Reference actualRef = verse.getReference();
        assertEquals(expectedRef, actualRef);
    }

    @Test
    public void testToString() {
        String expectedString = "Verse:[1 " + verse.getTranslationReference() + " " + verse.getText() + "]";
        String actualString = verse.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetIdWithValidParams() {
        int expectedId = 1;
        int actualId = Verse.GetId(1, 1, 1, 1);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetIdWithInvalidParams() {
        int expectedId = -1;
        int actualId = Verse.GetId(1, 2, 2, 2);
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetWithTranslationsAndParams() {
        Translation[] translations = {Translation.Get(1)};
        int bookId = 1;
        int chapterId = 1;
        int verseId = 1;

        Verse expectedVerse = Verse.New(1);
        ArrayList<Verse> expectedVerses = new ArrayList<>();
        expectedVerses.add(expectedVerse);

        ArrayList<Verse> actualVerses = Verse.Get(translations, bookId, chapterId, verseId);
        assertEquals(expectedVerses, actualVerses);
    }

    @Test
    public void testGetWithTranslationsAndReferences() {
        Translation[] translations = {Translation.Get(1)};
        Reference begin = new Reference(1, 1, 1);
        Reference end = new Reference(2, 2, 2);

        Verse expectedVerse = Verse.New(1);
        ArrayList<Verse> expectedVerses = new ArrayList<>();
        expectedVerses.add(expectedVerse);

        ArrayList<Verse> actualVerses = Verse.Get(translations, begin, end);
        assertEquals(expectedVerses, actualVerses);
    }
}