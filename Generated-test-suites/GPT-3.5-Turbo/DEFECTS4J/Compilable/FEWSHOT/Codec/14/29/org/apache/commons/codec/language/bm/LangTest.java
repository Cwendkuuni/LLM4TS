package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LangTest {

    // Test the guessLanguage method

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        assertEquals("en", lang.guessLanguage("hello")); // English
        assertEquals("es", lang.guessLanguage("hola")); // Spanish
        assertEquals("fr", lang.guessLanguage("bonjour")); // French
    }

    @Test
    public void testGuessLanguage_Japanese() {
        Lang lang = Lang.instance(NameType.GENERIC);

        assertEquals("ja", lang.guessLanguage("こんにちは")); // Japanese
    }

    // Test the guessLanguages method

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);

        Languages.LanguageSet ls = lang.guessLanguages("hello");
        assertTrue(ls.contains("en")); // English
        assertTrue(ls.contains("fr")); // French

        ls = lang.guessLanguages("hola");
        assertTrue(ls.contains("es")); // Spanish
        assertTrue(ls.contains("pt")); // Portuguese

        ls = lang.guessLanguages("bonjour");
        assertTrue(ls.contains("fr")); // French
        assertTrue(ls.contains("it")); // Italian
    }

    @Test
    public void testGuessLanguages_Japanese() {
        Lang lang = Lang.instance(NameType.GENERIC);

        Languages.LanguageSet ls = lang.guessLanguages("こんにちは");
        assertTrue(ls.contains("ja")); // Japanese
    }

}