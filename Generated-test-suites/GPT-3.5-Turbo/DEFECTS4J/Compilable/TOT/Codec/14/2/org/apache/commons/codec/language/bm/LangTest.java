package org.apache.commons.codec.language.bm;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage_TypicalUseCase() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        assertEquals("en", result); // The word "hello" is most likely in English language
    }

    @Test
    public void testGuessLanguage_EdgeCase_NoUniqueMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("");
        assertEquals("und", result); // Empty string does not correspond to a unique language, so "und" (undetermined) is returned
    }

    @Test
    public void testGuessLanguage_ErrorScenario_NonExistentLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hola");
        assertEquals("und", result); // The word "hola" does not belong to any of the supported languages, so "und" is returned
    }

    @Test
    public void testGuessLanguages_TypicalUseCase() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertTrue(result.contains("en")); // The word "hello" is most likely in English language
    }

    @Test
    public void testGuessLanguages_EdgeCase_NoMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("");
        assertTrue(result.isEmpty()); // Empty string does not match any language
    }

    @Test
    public void testGuessLanguages_ErrorScenario_MultipleMatches() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("common");
        assertTrue(result.contains("en")); // The word "common" matches both English and Latin languages
        assertTrue(result.contains("la"));
    }
}