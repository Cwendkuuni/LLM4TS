package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.Lang.NameType;

/**
 * Test for Lang class
 *
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}
     */
    @org.junit.Test
    public void testInstance() {
        Lang l = Lang.instance(NameType.ENGLISH);
        assertNotNull(l);
        assertEquals(NameType.ENGLISH, l.getNameType());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}
     */
    @org.junit.Test
    public void testGuessLanguage() {
        Lang l = Lang.instance(NameType.ENGLISH);

        // Test English words
        assertEquals(NameType.ENGLISH, l.guessLanguage("hello"));
        assertEquals(NameType.ENGLISH, l.guessLanguage("world"));
        assertEquals(NameType.ENGLISH, l.guessLanguage("goodbye"));

        // Test non-English words
        assertEquals(Languages.CHINESE, l.guessLanguage("你好"));
        assertEquals(Languages.FRENCH, l.guessLanguage("bonjour"));
        assertEquals(Languages.GERMAN, l.guessLanguage("guten"));

        // Test other languages
        assertEquals(Languages.ANY, l.guessLanguage(""));
        assertEquals(Languages.ANY, l.guessLanguage("123456"));
        assertEquals(Languages.ANY, l.guessLanguage("ABCDEF"));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}
     */
    @org.junit.Test
    public void testGuessLanguages() {
        Lang l = Lang.instance(NameType.ENGLISH);

        // Test English words
        Set<String> expected = EnumSet.allOf(NameType.class);
        expected.remove(NameType.ENGLISH);
        Set<String> actual = l.guessLanguages("hello");
        assertTrue(expected.containsAll(actual));
        assertFalse(actual.contains(NameType.ENGLISH.toString()));

        // Test non-English words
        expected = EnumSet.allOf(NameType.class);
        expected.remove(NameType.ENGLISH);
        expected.add(NameType.CHINESE);
        actual = l.guessLanguages("你好");
        assertTrue(expected.containsAll(actual));
        assertFalse(actual.contains(NameType.ENGLISH.toString()));

        // Test empty string
        expected = EnumSet.allOf(NameType.class);
        actual = l.guessLanguages("");
        assertEquals(Collections.singleton(Languages.ANY_LANGUAGE.toString()), actual);

        // Test numbers
        expected = EnumSet.allOf(NameType.class);
        actual = l.guessLanguages("123456");
        assertEquals(Collections.singleton(Languages.ANY_LANGUAGE.toString()), actual);

        // Test alphabets
        expected = EnumSet.allOf(NameType.class);
        actual = l.guessLanguages("ABCDEF");
        assertEquals(Collections.singleton(Languages.ANY_LANGUAGE.toString()), actual);
    }

    /**
     * Test method for {@link Lang#Lang(List, Languages)} constructor
     */
    @org.junit.Test
    public void testLangConstructor() {
        // Load English language rules
        List<LangRule> rules = Arrays.asList(
                new LangRule(Pattern.compile("^[aeiou]"), Collections.singletonList(NameType.ENGLISH.toString()), true),
                new LangRule(Pattern.compile("^[bcdfghjklmnpqrstvwxyz]", Pattern.CASE_INSENSITIVE), Collections.singletonList(NameType.ENGLISH.toString()), true),
                new LangRule(Pattern.compile("^[äöü]"), Collections.singletonList(NameType.GERMAN.toString()), true),
                new LangRule(Pattern.compile("^[àáâãäå]"), Collections.singletonList(NameType.FRENCH.toString()), true),
                new LangRule(Pattern.compile("^[ни]"), Collections.singletonList(NameType.RUSSIAN.toString()), true)
        );
        Languages english = Languages.getInstance(NameType.ENGLISH);
        Lang l = new Lang(rules, english);

        // Test English words
        assertEquals(NameType.ENGLISH, l.guessLanguage("hello"));
        assertEquals(NameType.ENGLISH, l.guessLanguage("world"));
        assertEquals(NameType.ENGLISH, l.guessLanguage("goodbye"));

        // Test non-English words
        assertEquals(Languages.CHINESE, l.guessLanguage("你好"));
        assertEquals(Languages.FRENCH, l.guessLanguage("bonjour"));
        assertEquals(Languages.GERMAN, l.guessLanguage("guten"));
        assertEquals(Languages.RUSSIAN, l.guessLanguage("никогда"));

        // Test other languages
        assertEquals(Languages.ANY, l.guessLanguage(""));
        assertEquals(Languages.ANY, l.guessLanguage("123456"));
        assertEquals(Languages.ANY, l.guessLanguage("ABCDEF"));

        // Test guessLanguages method
        Set<String> expected = Arrays.asList(NameType.ENGLISH.toString(), NameType.GERMAN.toString(), NameType.FRENCH.toString(), NameType.RUSSIAN.toString());
        Set<String> actual = l.guessLanguages("你好ni");
        assertTrue(expected.containsAll(actual));
    }
}