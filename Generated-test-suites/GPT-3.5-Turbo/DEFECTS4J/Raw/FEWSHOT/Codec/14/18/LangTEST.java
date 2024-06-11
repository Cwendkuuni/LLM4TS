package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test cases with known languages
        assertEquals("en", lang.guessLanguage("hello"));
        assertEquals("fr", lang.guessLanguage("bonjour"));
        assertEquals("es", lang.guessLanguage("hola"));
        assertEquals("de", lang.guessLanguage("hallo"));
        assertEquals("it", lang.guessLanguage("ciao"));
        
        // Test case for an unknown language
        assertNotEquals("en", lang.guessLanguage("こんにちは"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test cases with known languages
        assertEquals("en", lang.guessLanguages("hello").getAny());
        assertEquals("fr", lang.guessLanguages("bonjour").getAny());
        assertEquals("es", lang.guessLanguages("hola").getAny());
        assertEquals("de", lang.guessLanguages("hallo").getAny());
        assertEquals("it", lang.guessLanguages("ciao").getAny());
        
        // Test case for an unknown language
        assertEquals("und", lang.guessLanguages("こんにちは").getAny());
    }
}