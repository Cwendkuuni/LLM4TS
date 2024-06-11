import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage_typical() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Typical use case - single language match
        String word = "hello";
        String expectedLanguage = "en";
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguage_edge() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Edge case - empty input
        String word = "";
        String expectedLanguage = "und";
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguage_error() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Error scenario - unsupported language
        String word = "こんにちは";
        String expectedLanguage = "und";
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguages_typical() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Typical use case - multiple language matches
        String word = "hello";
        Languages.LanguageSet expectedLanguages = Languages.LanguageSet.from("en");
        Languages.LanguageSet actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguages_edge() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Edge case - empty input
        String word = "";
        Languages.LanguageSet expectedLanguages = Languages.NO_LANGUAGES;
        Languages.LanguageSet actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguages_error() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Error scenario - unsupported language
        String word = "こんにちは";
        Languages.LanguageSet expectedLanguages = Languages.NO_LANGUAGES;
        Languages.LanguageSet actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }
}