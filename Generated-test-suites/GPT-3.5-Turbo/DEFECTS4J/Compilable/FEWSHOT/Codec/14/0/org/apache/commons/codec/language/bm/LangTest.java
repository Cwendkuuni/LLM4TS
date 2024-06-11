package org.apache.commons.codec.language.bm;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        assertEquals("english", lang.guessLanguage("hello"));
        assertEquals("spanish", lang.guessLanguage("hola"));
        assertEquals("german", lang.guessLanguage("hallo"));
        assertEquals("french", lang.guessLanguage("bonjour"));
        assertEquals("italian", lang.guessLanguage("ciao"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);

        assertEquals("english", lang.guessLanguages("hello").getAny());
        assertEquals("spanish", lang.guessLanguages("hola").getAny());
        assertEquals("german", lang.guessLanguages("hallo").getAny());
        assertEquals("french", lang.guessLanguages("bonjour").getAny());
        assertEquals("italian", lang.guessLanguages("ciao").getAny());
    }
}