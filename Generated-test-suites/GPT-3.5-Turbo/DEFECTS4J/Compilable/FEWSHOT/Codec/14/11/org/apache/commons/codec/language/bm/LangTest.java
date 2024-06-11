package org.apache.commons.codec.language.bm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test cases for English language
        assertEquals("eng", lang.guessLanguage("hello"));
        assertEquals("eng", lang.guessLanguage("world"));
        assertEquals("eng", lang.guessLanguage("goodbye"));

        // Test cases for French language
        assertEquals("fra", lang.guessLanguage("bonjour"));
        assertEquals("fra", lang.guessLanguage("au revoir"));
        assertEquals("fra", lang.guessLanguage("merci"));

        // Test cases for Spanish language
        assertEquals("spa", lang.guessLanguage("hola"));
        assertEquals("spa", lang.guessLanguage("adios"));
        assertEquals("spa", lang.guessLanguage("gracias"));

        // Test cases for German language
        assertEquals("deu", lang.guessLanguage("hallo"));
        assertEquals("deu", lang.guessLanguage("auf wiedersehen"));
        assertEquals("deu", lang.guessLanguage("danke"));

        // Test cases for Italian language
        assertEquals("ita", lang.guessLanguage("ciao"));
        assertEquals("ita", lang.guessLanguage("arrivederci"));
        assertEquals("ita", lang.guessLanguage("grazie"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test cases for English language
        assertEquals("[eng]", lang.guessLanguages("hello").toString());
        assertEquals("[eng]", lang.guessLanguages("world").toString());
        assertEquals("[eng]", lang.guessLanguages("goodbye").toString());

        // Test cases for French language
        assertEquals("[fra]", lang.guessLanguages("bonjour").toString());
        assertEquals("[fra]", lang.guessLanguages("au revoir").toString());
        assertEquals("[fra]", lang.guessLanguages("merci").toString());

        // Test cases for Spanish language
        assertEquals("[spa]", lang.guessLanguages("hola").toString());
        assertEquals("[spa]", lang.guessLanguages("adios").toString());
        assertEquals("[spa]", lang.guessLanguages("gracias").toString());

        // Test cases for German language
        assertEquals("[deu]", lang.guessLanguages("hallo").toString());
        assertEquals("[deu]", lang.guessLanguages("auf wiedersehen").toString());
        assertEquals("[deu]", lang.guessLanguages("danke").toString());

        // Test cases for Italian language
        assertEquals("[ita]", lang.guessLanguages("ciao").toString());
        assertEquals("[ita]", lang.guessLanguages("arrivederci").toString());
        assertEquals("[ita]", lang.guessLanguages("grazie").toString());
    }
}