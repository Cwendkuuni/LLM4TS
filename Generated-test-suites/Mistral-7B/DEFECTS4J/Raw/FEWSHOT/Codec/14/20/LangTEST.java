package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);

        // Test English words
        assertEquals(NameType.ENGLISH, lang.guessLanguage("hello"));
        assertEquals(NameType.ENGLISH, lang.guessLanguage("world"));
        assertEquals(NameType.ENGLISH, lang.guessLanguage("apple"));

        // Test non-English words
        assertEquals(NameType.FRENCH, lang.guessLanguage("salut"));
        assertEquals(NameType.GERMAN, lang.guessLanguage("hallo"));
        assertEquals(NameType.SPANISH, lang.guessLanguage("hola"));

        // Test ambiguous words
        assertEquals(NameType.ANY, lang.guessLanguage("hello")); // Could be English or other languages
        assertEquals(NameType.ANY, lang.guessLanguage("hallo")); // Could be German or other languages

        // Test empty string
        assertNull(lang.guessLanguage(""));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.ENGLISH);

        // Test English words
        assertEquals(Collections.singletonList(NameType.ENGLISH), lang.guessLanguages("hello").getLanguages());
        assertEquals(Collections.singletonList(NameType.ENGLISH), lang.guessLanguages("world").getLanguages());
        assertEquals(Arrays.asList(NameType.ENGLISH, NameType.GERMAN), lang.guessLanguages("applestrudel").getLanguages());

        // Test non-English words
        assertEquals(Collections.singletonList(NameType.FRENCH), lang.guessLanguages("salut").getLanguages());
        assertEquals(Collections.singletonList(NameType.GERMAN), lang.guessLanguages("hallo").getLanguages());
        assertEquals(Collections.singletonList(NameType.SPANISH), lang.guessLanguages("hola").getLanguages());

        // Test ambiguous words
        assertEquals(Collections.singletonList(NameType.ANY), lang.guessLanguages("hello").getLanguages()); // Could be English or other languages
        assertEquals(Collections.singletonList(NameType.ANY), lang.guessLanguages("hallo").getLanguages()); // Could be German or other languages

        // Test empty string
        assertEquals(Collections.emptyList(), lang.guessLanguages("").getLanguages());
    }
}