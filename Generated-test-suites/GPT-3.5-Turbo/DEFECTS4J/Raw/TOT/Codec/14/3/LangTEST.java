import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class LangTest {

    private static Lang lang;

    @BeforeClass
    public static void setUp() {
        lang = Lang.instance(NameType.GENERIC);
    }

    @Test
    public void testGuessLanguage_typicalCase() {
        String input = "hello";
        String expectedLanguage = "en";

        String actualLanguage = lang.guessLanguage(input);

        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguage_emptyInput() {
        String input = "";
        String expectedLanguage = Languages.ANY;

        String actualLanguage = lang.guessLanguage(input);

        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguage_edgeCase_multipleMatches() {
        String input = "hello";
        String expectedLanguage1 = "en";
        String expectedLanguage2 = "pt";

        Languages.LanguageSet expectedLanguages = Languages.LanguageSet.from(expectedLanguage1, expectedLanguage2);

        Languages.LanguageSet actualLanguages = lang.guessLanguages(input);

        assertTrue(actualLanguages.containsAny(expectedLanguages));
    }

    @Test
    public void testGuessLanguage_errorScenario_invalidInput() {
        String input = null;
        String expectedLanguage = Languages.ANY;

        String actualLanguage = lang.guessLanguage(input);

        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguages_typicalCase() {
        String input = "hello";
        String expectedLanguage = "en";

        Languages.LanguageSet actualLanguages = lang.guessLanguages(input);

        assertTrue(actualLanguages.contains(expectedLanguage));
    }

    @Test
    public void testGuessLanguages_emptyInput() {
        String input = "";
        Languages.LanguageSet expectedLanguages = Languages.ANY_LANGUAGE;

        Languages.LanguageSet actualLanguages = lang.guessLanguages(input);

        assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguages_edgeCase_multipleMatches() {
        String input = "hello";
        String expectedLanguage1 = "en";
        String expectedLanguage2 = "pt";

        Languages.LanguageSet expectedLanguages = Languages.LanguageSet.from(expectedLanguage1, expectedLanguage2);

        Languages.LanguageSet actualLanguages = lang.guessLanguages(input);

        assertTrue(actualLanguages.containsAny(expectedLanguages));
    }

    @Test
    public void testGuessLanguages_errorScenario_invalidInput() {
        String input = null;
        Languages.LanguageSet expectedLanguages = Languages.ANY_LANGUAGE;

        Languages.LanguageSet actualLanguages = lang.guessLanguages(input);

        assertEquals(expectedLanguages, actualLanguages);
    }
}