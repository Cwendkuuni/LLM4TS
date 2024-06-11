package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.SIMPLIFIED);
        
        assertEquals("english", lang.guessLanguage("hello"));
        assertEquals("spanish", lang.guessLanguage("hola"));
        assertEquals("french", lang.guessLanguage("bonjour"));
        assertEquals("german", lang.guessLanguage("hallo"));
        assertEquals("portuguese", lang.guessLanguage("olá"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SIMPLIFIED);
        
        assertEquals("english", lang.guessLanguages("hello").getAny());
        assertEquals("spanish", lang.guessLanguages("hola").getAny());
        assertEquals("french", lang.guessLanguages("bonjour").getAny());
        assertEquals("german", lang.guessLanguages("hallo").getAny());
        assertEquals("portuguese", lang.guessLanguages("olá").getAny());
    }
}