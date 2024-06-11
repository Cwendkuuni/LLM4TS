package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("en", lang.guessLanguage("hello"));
        assertEquals("es", lang.guessLanguage("hola"));
        assertEquals("fr", lang.guessLanguage("bonjour"));
        assertTrue(lang.guessLanguage("123").startsWith("und"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals(1, lang.guessLanguages("hello").size());
        assertEquals(2, lang.guessLanguages("hola").size());
        assertEquals(3, lang.guessLanguages("bonjour").size());
        assertEquals(156, lang.guessLanguages("123").size());
    }

}