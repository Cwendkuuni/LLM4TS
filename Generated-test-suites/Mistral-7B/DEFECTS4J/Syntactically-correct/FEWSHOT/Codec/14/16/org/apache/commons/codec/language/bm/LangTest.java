package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertNotNull(englishLang);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInstanceWithInvalidNameType() {
        Lang invalidLang = Lang.instance(new NameType("INVALID"));
    }

    @Test
    public void testLoadFromResourceAndInstance() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        Lang germanLang = Lang.instance(NameType.GERMAN);
        assertEquals(englishLang, Langs.get(NameType.ENGLISH));
        assertEquals(germanLang, Langs.get(NameType.GERMAN));
    }

    @Test
    public void testGuessLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // English words
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("hello"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("world"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("java"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("programming"));

        // Non-English words
        assertEquals(NameType.GERMAN, englishLang.guessLanguage("guten Tag"));
        assertEquals(NameType.GERMAN, englishLang.guessLanguage("danke schön"));
        assertEquals(NameType.GERMAN, englishLang.guessLanguage(" das ist gut"));
        assertEquals(NameType.GERMAN, englishLang.guessLanguage("Wiesenbach"));

        // Ambiguous words
        assertEquals(Languages.ANY, englishLang.guessLanguage("hello world"));
        assertEquals(Languages.ANY, englishLang.guessLanguage("java programming"));
        assertEquals(Languages.ANY, englishLang.guessLanguage("guten Tag danke schön"));
        assertEquals(Languages.ANY, englishLang.guessLanguage("Wiesenbach Java"));
    }

    @Test
    public void testGuessLanguages() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // English words
        assertEquals(Collections.singletonList(NameType.ENGLISH), englishLang.guessLanguages("hello").getLanguages());
        assertEquals(Collections.singletonList(NameType.ENGLISH), englishLang.guessLanguages("world").getLanguages());
        assertEquals(Collections.singletonList(NameType.ENGLISH), englishLang.guessLanguages("java").getLanguages());
        assertEquals(Collections.singletonList(NameType.ENGLISH), englishLang.guessLanguages("programming").getLanguages());

        // Non-English words
        assertEquals(Collections.singletonList(NameType.GERMAN), englishLang.guessLanguages("guten Tag").getLanguages());
        assertEquals(Collections.singletonList(NameType.GERMAN), englishLang.guessLanguages("danke schön").getLanguages());
        assertEquals(Collections.singletonList(NameType.GERMAN), englishLang.guessLanguages("das ist gut").getLanguages());
        assertEquals(Collections.singletonList(NameType.GERMAN), englishLang.guessLanguages("Wiesenbach").getLanguages());

        // Ambiguous words
        assertEquals(Collections.emptyList(), englishLang.guessLanguages("hello world").getLanguages());
        assertEquals(Collections.emptyList(), englishLang.guessLanguages("java programming").getLanguages());
        assertEquals(Collections.emptyList(), englishLang.guessLanguages("guten Tag danke schön").getLanguages());
        assertEquals(Collections.emptyList(), englishLang.guessLanguages("Wiesenbach Java").getLanguages());
    }
}