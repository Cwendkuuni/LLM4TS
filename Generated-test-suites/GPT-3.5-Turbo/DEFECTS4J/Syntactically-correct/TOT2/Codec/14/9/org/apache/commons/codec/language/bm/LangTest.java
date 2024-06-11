import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    private Lang lang;

    @Before
    public void setUp() {
        lang = Lang.instance(NameType.ENGLISH);
    }

    @Test
    public void testGuessLanguage() {
        String text = "hello";
        String expectedLanguage = "en";
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }

    @Test
    public void testGuessLanguages() {
        String text = "hello";
        Languages.LanguageSet expectedLanguages = Languages.LanguageSet.from("en");
        assertEquals(expectedLanguages, lang.guessLanguages(text));
    }

    // Edge case: Empty input
    @Test
    public void testGuessLanguageEmptyInput() {
        String text = "";
        String expectedLanguage = Languages.ANY;
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }

    // Error scenario: Non-existent language
    @Test
    public void testGuessLanguageNonExistentLanguage() {
        String text = "hola";
        String expectedLanguage = Languages.ANY;
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }

    // Edge case: Extremely long input
    @Test
    public void testGuessLanguageLongInput() {
        String text = "This is a very long sentence that exceeds the typical input length";
        String expectedLanguage = "en";
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }

    // Error scenario: null input
    @Test
    public void testGuessLanguageNullInput() {
        String text = null;
        String expectedLanguage = Languages.ANY;
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }

}