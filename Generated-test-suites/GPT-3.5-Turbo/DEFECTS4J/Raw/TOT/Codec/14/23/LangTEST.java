import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage_typicalUseCase() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String word = "hello";
        String expectedResult = "en";
        String actualResult = lang.guessLanguage(word);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGuessLanguage_edgeCase() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String word = "";
        String expectedResult = "und";
        String actualResult = lang.guessLanguage(word);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGuessLanguage_errorScenario() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String word = null;
        
        try {
            lang.guessLanguage(word);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed since exception was thrown
        }
    }

    @Test
    public void testGuessLanguages_typicalUseCase() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String word = "Hello";
        Languages.LanguageSet expectedLanguages = Languages.LanguageSet.from("en");
        Languages.LanguageSet actualLanguages = lang.guessLanguages(word);

        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguages_edgeCase() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String word = "";
        Languages.LanguageSet expectedLanguages = Languages.NO_LANGUAGES;
        Languages.LanguageSet actualLanguages = lang.guessLanguages(word);

        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguages_errorScenario() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String word = null;

        try {
            lang.guessLanguages(word);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Test passed since exception was thrown
        }

    }
}