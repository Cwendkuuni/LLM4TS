package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;

/**
 * Test class for Lang.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @org.junit.Test
    public void testGuessLanguage() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        // Test with empty string
        assertEquals(Languages.ANY, lang.guessLanguage(""));

        // Test with English words
        assertEquals(Languages.ENGLISH, lang.guessLanguage("hello"));
        assertEquals(Languages.ENGLISH, lang.guessLanguage("apple"));

        // Test with non-English words
        assertEquals(Languages.FRENCH, lang.guessLanguage("bonjour"));
        assertEquals(Languages.GERMAN, lang.guessLanguage("guten tag"));
        assertEquals(Languages.CHINESE, lang.guessLanguage("你好"));

        // Test with ambiguous words
        Set<String> expected = new HashSet<String>(Arrays.asList(Languages.ENGLISH.getName(), Languages.GERMAN.getName()));
        Set<String> result = lang.guessLanguages("kindergarten").getLanguages();
        assertTrue(expected.containsAll(result));
        assertEquals(expected.size(), result.size());

        // Test with wrong language
        assertFalse(Languages.JAPANESE.getName().equals(lang.guessLanguage("apple")));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguages() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        // Test with empty string
        assertEquals(Collections.emptySet(), lang.guessLanguages(""));

        // Test with English words
        Set<String> expected = EnumSet.of(Languages.ENGLISH.getName());
        Set<String> result = lang.guessLanguages("hello").getLanguages();
        assertEquals(expected, result);

        // Test with non-English words
        expected = EnumSet.of(Languages.FRENCH.getName(), Languages.GERMAN.getName(), Languages.CHINESE.getName());
        result = lang.guessLanguages("bonjour").getLanguages();
        assertEquals(expected, result);

        // Test with ambiguous words
        expected = EnumSet.of(Languages.ENGLISH.getName(), Languages.GERMAN.getName());
        result = lang.guessLanguages("kindergarten").getLanguages();
        assertEquals(expected, result);
    }
}