package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;

/**
 * Test class for Lang class
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}
     */
    @org.junit.Test
    public void testInstance() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final Lang germanLang = Lang.instance(NameType.GERMAN);

        assertEquals(NameType.ENGLISH, englishLang.getNameType());
        assertEquals(NameType.GERMAN, germanLang.getNameType());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}
     */
    @org.junit.Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        assertEquals(Languages.ENGLISH, englishLang.guessLanguage("hello"));
        assertEquals(Languages.ENGLISH, englishLang.guessLanguage("world"));
        assertEquals(Languages.ENGLISH, englishLang.guessLanguage("goodbye"));

        // Test for a word with multiple languages
        assertEquals(Languages.ENGLISH, englishLang.guessLanguage("aloha"));
        assertEquals(Languages.HAWAIIAN, englishLang.guessLanguage("aloha"));

        // Test for a word with no unique match
        assertNotEquals(Languages.ENGLISH, englishLang.guessLanguage("salut"));
        assertNotEquals(Languages.FRENCH, englishLang.guessLanguage("salut"));

        // Test for a word with no match
        assertNotEquals(Languages.ENGLISH, englishLang.guessLanguage("你好"));
        assertNotEquals(Languages.CHINESE, englishLang.guessLanguage("你好"));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}
     */
    @org.junit.Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        final Set<String> expectedLanguages = new HashSet<>(Arrays.asList(Languages.ENGLISH.getName(), Languages.HAWAIIAN.getName()));
        final Set<String> actualLanguages = englishLang.guessLanguages("aloha");

        assertEquals(expectedLanguages, actualLanguages);

        final Set<String> noMatchLanguages = new HashSet<>(Arrays.asList(Languages.FRENCH.getName(), Languages.GERMAN.getName()));
        final Set<String> actualNoMatchLanguages = englishLang.guessLanguages("salut");

        assertEquals(noMatchLanguages, actualNoMatchLanguages);

        final Set<String> multiMatchLanguages = new HashSet<>(Arrays.asList(Languages.ENGLISH.getName(), Languages.GERMAN.getName(), Languages.DUTCH.getName()));
        final Set<String> actualMultiMatchLanguages = englishLang.guessLanguages("koffie");

        assertTrue(multiMatchLanguages.containsAll(actualMultiMatchLanguages));
        assertTrue(actualMultiMatchLanguages.containsAll(multiMatchLanguages));

        // Test for a word with no match
        final Set<String> noMatchLanguages2 = new HashSet<>(Collections.singleton(Languages.CHINESE.getName()));
        final Set<String> actualNoMatchLanguages2 = englishLang.guessLanguages("你好");

        assertEquals(noMatchLanguages2, actualNoMatchLanguages2);
    }
}