package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class LangTest {
    
    @Test
    public void testGuessLanguage_returnsLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        assertEquals("en", result);
    }
    
    @Test
    public void testGuessLanguages_returnsLanguageSet() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertTrue(result.contains("en"));
    }
    
    @Test
    public void testGuessLanguages_returnsMultipleLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hola");
        assertTrue(result.contains("es"));
        assertTrue(result.contains("eu"));
    }
    
    @Test
    public void testGuessLanguages_returnsAnyLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("");
        assertTrue(result.contains(""));
    }
    
    @Test
    public void testGuessLanguages_returnsNoLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("123");
        assertFalse(result.contains("en"));
        assertFalse(result.contains("es"));
        assertFalse(result.contains("eu"));
    }
}