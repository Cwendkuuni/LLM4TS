import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LangTest {
    private Lang lang;

    @Before
    public void setUp() {
        lang = Lang.instance(NameType.GENERIC);
    }

    @Test
    public void testGuessLanguage() {
        String result = lang.guessLanguage("hello");
        assertEquals("eng", result);
    }

    @Test
    public void testGuessLanguages() {
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertEquals(Languages.LanguageSet.from("eng"), result);
    }

    // Edge cases and exception handling scenarios

    // Test with an empty string
    @Test
    public void testGuessLanguageEmpty() {
        String result = lang.guessLanguage("");
        assertEquals(Languages.ANY, result);
    }

    // Test with special characters
    @Test
    public void testGuessLanguageSpecialCharacters() {
        String result = lang.guessLanguage("!@#$%^&*");
        assertEquals(Languages.ANY, result);
    }

    // Test with a word that matches multiple languages
    @Test
    public void testGuessLanguageMultipleMatches() {
        String result = lang.guessLanguage("arm");
        assertEquals(Languages.ANY, result);
    }

    // Test with a non-existent language
    @Test
    public void testGuessLanguageNonExistentLanguage() {
        String result = lang.guessLanguage("nonexistent");
        assertEquals(Languages.ANY, result);
    }

    // Test with a null input
    @Test(expected = NullPointerException.class)
    public void testGuessLanguageNull() {
        lang.guessLanguage(null);
    }

    // Test with a long input string
    @Test
    public void testGuessLanguageLongInput() {
        String result = lang.guessLanguage("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz");
        assertEquals(Languages.ANY, result);
    }
}