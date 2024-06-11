package org.apache.commons.codec.language.bm;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("eng", lang.guessLanguage("Hello"));
        assertEquals("fra", lang.guessLanguage("Bonjour"));
        assertEquals("deu", lang.guessLanguage("Guten Tag"));
        assertEquals("ita", lang.guessLanguage("Ciao"));
        assertEquals("spa", lang.guessLanguage("Hola"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("eng", lang.guessLanguages("Hello").getAny());
        assertEquals("fra", lang.guessLanguages("Bonjour").getAny());
        assertEquals("deu", lang.guessLanguages("Guten Tag").getAny());
        assertEquals("ita", lang.guessLanguages("Ciao").getAny());
        assertEquals("spa", lang.guessLanguages("Hola").getAny());
    }
}