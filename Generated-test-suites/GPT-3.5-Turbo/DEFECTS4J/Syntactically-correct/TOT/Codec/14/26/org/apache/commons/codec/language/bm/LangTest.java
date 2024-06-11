import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Typical use case
        String word = "hello";
        String expectedLanguage = "en";
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
        
        // Edge case: empty word
        word = "";
        expectedLanguage = "und";
        actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
        
        // Error scenario: null word
        word = null;
        expectedLanguage = "und";
        actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Typical use case
        String word = "hello";
        Languages.LanguageSet expectedLanguages = Languages.LanguageSet.from("en");
        Languages.LanguageSet actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
        
        // Edge case: empty word
        word = "";
        expectedLanguages = Languages.ANY_LANGUAGE;
        actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
        
        // Error scenario: null word
        word = null;
        expectedLanguages = Languages.ANY_LANGUAGE;
        actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }
}