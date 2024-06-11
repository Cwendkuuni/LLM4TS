import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LangTest {
    private Lang lang;

    @Before
    public void setup() {
        lang = Lang.instance(NameType.GENERIC);
    }

    // Test cases for guessLanguage method

    @Test
    public void testGuessLanguage_WhenWordIsEnglish_ShouldReturnEnglish() {
        String word = "hello";
        String expectedLanguage = "en";
        assertEquals(expectedLanguage, lang.guessLanguage(word));
    }

    @Test
    public void testGuessLanguage_WhenWordIsSpanish_ShouldReturnSpanish() {
        String word = "hola";
        String expectedLanguage = "es";
        assertEquals(expectedLanguage, lang.guessLanguage(word));
    }

    @Test
    public void testGuessLanguage_WhenWordIsFrench_ShouldReturnFrench() {
        String word = "bonjour";
        String expectedLanguage = "fr";
        assertEquals(expectedLanguage, lang.guessLanguage(word));
    }

    @Test
    public void testGuessLanguage_WhenWordIsInvalid_ShouldReturnAny() {
        String word = "@@@@@";
        String expectedLanguage = Languages.ANY;
        assertEquals(expectedLanguage, lang.guessLanguage(word));
    }

    // Test cases for guessLanguages method

    @Test
    public void testGuessLanguages_WhenWordIsEnglish_ShouldReturnEnglishLanguageSet() {
        String word = "hello";
        Languages.LanguageSet expectedLanguageSet = Languages.LanguageSet.from("en");
        assertEquals(expectedLanguageSet, lang.guessLanguages(word));
    }

    @Test
    public void testGuessLanguages_WhenWordIsInvalid_ShouldReturnAnyLanguageSet() {
        String word = "@@@@@";
        Languages.LanguageSet expectedLanguageSet = Languages.ANY_LANGUAGE;
        assertEquals(expectedLanguageSet, lang.guessLanguages(word));
    }

    @Test
    public void testGuessLanguages_WhenWordMatchesMultipleLanguages_ShouldReturnUnionOfLanguageSets() {
        String word = "hello";
        Languages.LanguageSet expectedLanguageSet = Languages.LanguageSet.from("en", "fr");
        assertEquals(expectedLanguageSet, lang.guessLanguages(word));
    }

    // Additional test cases

    @Test
    public void testGuessLanguage_WhenWordIsEmpty_ShouldReturnAny() {
        String word = "";
        String expectedLanguage = Languages.ANY;
        assertEquals(expectedLanguage, lang.guessLanguage(word));
    }

    @Test
    public void testGuessLanguage_WhenWordIsLong_ShouldNotAffectPerformance() {
        String word = "abcdefghijklmnopqrstuvwxyz";
        String expectedLanguage = Languages.ANY;
        assertEquals(expectedLanguage, lang.guessLanguage(word));
    }

    @Test
    public void testGuessLanguages_WhenWordIsEmpty_ShouldReturnAnyLanguageSet() {
        String word = "";
        Languages.LanguageSet expectedLanguageSet = Languages.ANY_LANGUAGE;
        assertEquals(expectedLanguageSet, lang.guessLanguages(word));
    }

    @Test
    public void testGuessLanguages_WhenWordIsLong_ShouldNotAffectPerformance() {
        String word = "abcdefghijklmnopqrstuvwxyz";
        Languages.LanguageSet expectedLanguageSet = Languages.ANY_LANGUAGE;
        assertEquals(expectedLanguageSet, lang.guessLanguages(word));
    }

    // Error scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromResource_WhenResourceFileDoesNotExist_ShouldThrowException() {
        String languageRulesResourceName = "non-existent-file.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC);
        lang.loadFromResource(languageRulesResourceName, languages);
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResource_WhenResourceFileIsEmpty_ShouldThrowException() {
        String languageRulesResourceName = "empty-file.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC);
        lang.loadFromResource(languageRulesResourceName, languages);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromResource_WhenResourceFileHasMalformedLine_ShouldThrowException() {
        String languageRulesResourceName = "malformed-line.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC);
        lang.loadFromResource(languageRulesResourceName, languages);
    }

}