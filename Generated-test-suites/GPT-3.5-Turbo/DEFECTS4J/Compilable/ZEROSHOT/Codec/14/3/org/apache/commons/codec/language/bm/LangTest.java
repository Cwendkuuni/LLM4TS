package org.apache.commons.codec.language.bm;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test with known words and languages
        assertEquals("eng", lang.guessLanguage("hello"));
        assertEquals("fra", lang.guessLanguage("bonjour"));
        assertEquals("deu", lang.guessLanguage("guten tag"));
        assertEquals("ita", lang.guessLanguage("ciao"));
        assertEquals("nld", lang.guessLanguage("hallo"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test with known words and languages
        assertEquals("[eng]", lang.guessLanguages("hello").toString());
        assertEquals("[fra]", lang.guessLanguages("bonjour").toString());
        assertEquals("[deu]", lang.guessLanguages("guten tag").toString());
        assertEquals("[ita]", lang.guessLanguages("ciao").toString());
        assertEquals("[nld]", lang.guessLanguages("hallo").toString());
    }

}