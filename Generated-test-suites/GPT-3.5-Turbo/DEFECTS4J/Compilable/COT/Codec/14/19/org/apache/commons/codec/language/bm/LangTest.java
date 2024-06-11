package org.apache.commons.codec.language.bm;

import org.junit.Test;

import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Basic functionality test
        String text = "hello";
        String result = lang.guessLanguage(text);
        assertEquals("eng", result);

        // Edge case: empty input
        text = "";
        result = lang.guessLanguage(text);
        assertEquals(Languages.ANY, result);

        // Edge case: input with multiple potential languages
        text = "hola";
        result = lang.guessLanguage(text);
        assertEquals("spa", result);

        // Exception handling: null input
        try {
            lang.guessLanguage(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Basic functionality test
        String text = "hello";
        Languages.LanguageSet result = lang.guessLanguages(text);
        assertTrue(result.contains("eng"));

        // Edge case: empty input
        text = "";
        result = lang.guessLanguages(text);
        assertEquals(Languages.ANY_LANGUAGE, result);

        // Edge case: input with multiple potential languages
        text = "hola";
        result = lang.guessLanguages(text);
        assertTrue(result.contains("spa"));
        assertTrue(result.contains("ita"));
        assertTrue(result.contains("por"));

        // Exception handling: null input
        try {
            lang.guessLanguages(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }
}
