package org.apache.commons.codec.language.bm;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
    
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        assertEquals("eng", result);
        
        result = lang.guessLanguage("hola");
        assertEquals("spa", result);
        
        result = lang.guessLanguage("hallo");
        assertEquals("deu", result);
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertTrue(result.contains("eng"));
        
        result = lang.guessLanguages("hola");
        assertTrue(result.contains("spa"));
        
        result = lang.guessLanguages("hallo");
        assertTrue(result.contains("deu"));
    }
}