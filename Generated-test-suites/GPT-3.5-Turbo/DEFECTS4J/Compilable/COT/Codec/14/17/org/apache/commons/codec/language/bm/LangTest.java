package org.apache.commons.codec.language.bm;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
    
    /**
     * Test the guessLanguage method with a word that has a unique match
     */
    @Test
    public void testGuessLanguageWithUniqueMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        assertEquals("en", result);
    }
    
    /**
     * Test the guessLanguage method with a word that has multiple potential matches
     */
    @Test
    public void testGuessLanguageWithMultipleMatches() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("present");
        assertEquals(Languages.ANY, result);
    }
    
    /**
     * Test the guessLanguage method with an empty word
     */
    @Test
    public void testGuessLanguageWithEmptyWord() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("");
        assertEquals(Languages.ANY, result);
    }
    
    /**
     * Test the guessLanguage method with a word that contains special characters
     */
    @Test
    public void testGuessLanguageWithSpecialCharacters() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("!@#$");
        assertEquals(Languages.ANY, result);
    }
    
    /**
     * Test the guessLanguages method with a word that has a unique match
     */
    @Test
    public void testGuessLanguagesWithUniqueMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertEquals("en", result.getAny());
    }
    
    /**
     * Test the guessLanguages method with a word that has multiple potential matches
     */
    @Test
    public void testGuessLanguagesWithMultipleMatches() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("present");
        assertEquals(Languages.ANY_LANGUAGE, result);
    }
    
    /**
     * Test the guessLanguages method with an empty word
     */
    @Test
    public void testGuessLanguagesWithEmptyWord() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("");
        assertEquals(Languages.ANY_LANGUAGE, result);
    }
    
    /**
     * Test the guessLanguages method with a word that contains special characters
     */
    @Test
    public void testGuessLanguagesWithSpecialCharacters() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("!@#$");
        assertEquals(Languages.ANY_LANGUAGE, result);
    }
}