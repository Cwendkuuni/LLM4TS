package org.apache.commons.codec.language.bm;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
    
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        assertEquals("eng", result);
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertTrue(result.contains("eng"));
    }
    
    @Test
    public void testGuessLanguage_NoMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("xyz");
        assertEquals("und", result);
    }
    
    @Test
    public void testGuessLanguages_NoMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("xyz");
        assertTrue(result.isEmpty());
    }
}