package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;

/**
 * JUnit test cases for the Lang class.
 */
public class LangTest {

    /**
     * Tests the instance() method.
     */
    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    /**
     * Tests the guessLanguage() method with a simple English word.
     */
    @Test
    public void testGuessLanguageSimpleEnglish() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String result = lang.guessLanguage("cat");
        assertEquals(Languages.ENGLISH, result);
    }

    /**
     * Tests the guessLanguage() method with a simple word that is not English.
     */
    @Test
    public void testGuessLanguageSimpleNonEnglish() {
        Lang lang = Lang.instance(NameType.FRENCH);
        String result = lang.guessLanguage("chat");
        assertEquals(Languages.FRENCH, result);
    }

    /**
     * Tests the guessLanguages() method with a simple English word.
     */
    @Test
    public void testGuessLanguagesSimpleEnglish() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        Set<String> result = lang.guessLanguages("cat");
        assertTrue(result.contains(Languages.ENGLISH.getName()));
        assertEquals(1, result.size());
    }

    /**
     * Tests the guessLanguages() method with a simple word that is not English.
     */
    @Test
    public void testGuessLanguagesSimpleNonEnglish() {
        Lang lang = Lang.instance(NameType.FRENCH);
        Set<String> result = lang.guessLanguages("chat");
        assertTrue(result.contains(Languages.FRENCH.getName()));
        assertEquals(1, result.size());
    }

    /**
     * Tests the guessLanguages() method with a word that matches multiple languages.
     */
    @Test
    public void testGuessLanguagesMultiple() {
        Lang lang = Lang.instance(NameType.GERMAN);
        Set<String> result = lang.guessLanguages("hund");
        assertTrue(result.contains(Languages.GERMAN.getName()));
        assertTrue(result.contains(Languages.SWEDISH.getName()));
        assertEquals(2, result.size());
    }

    /**
     * Tests the guessLanguage() method with a word that matches no language.
     */
    @Test
    public void testGuessLanguageNoMatch() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String result = lang.guessLanguage("xkjgkdhsjg");
        assertEquals(Languages.ANY, result);
    }

    /**
     * Tests the guessLanguages() method with a word that matches no language.
     */
    @Test
    public void testGuessLanguagesNoMatch() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        Set<String> result = lang.guessLanguages("xkjgkdhsjg");
        assertTrue(result.isEmpty());
    }

    /**
     * Tests the guessLanguage() method with an empty string.
     */
    @Test
    public void testGuessLanguageEmpty() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String result = lang.guessLanguage("");
        assertEquals(Languages.ANY, result);
    }

    /**
     * Tests the guessLanguages() method with an empty string.
     */
    @Test
    public void testGuessLanguagesEmpty() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        Set<String> result = lang.guessLanguages("");
        assertTrue(result.isEmpty());
    }

    /**
     * Tests the guessLanguage() method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testGuessLanguageNull() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        lang.guessLanguage(null);
    }

    /**
     * Tests the guessLanguages() method with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testGuessLanguagesNull() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        lang.guessLanguages(null);
    }

    /**
     * Tests the guessLanguage() method with an input containing special characters.
     */
    @Test
    public void testGuessLanguageSpecialChars() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String result = lang.guessLanguage("Mëtàl Hëad");
        assertEquals(Languages.ENGLISH, result);
    }

    /**
     * Tests the guessLanguages() method with an input containing special characters.
     */
    @Test
    public void testGuessLanguagesSpecialChars() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        Set<String> result = lang.guessLanguages("Mëtàl Hëad");
        assertTrue(result.contains(Languages.ENGLISH.getName()));
        assertEquals(1, result.size());
    }

    /**
     * Tests the guessLanguage() method with an input containing only digits.
     */
    @Test
    public void testGuessLanguageDigits() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String result = lang.guessLanguage("12345");
        assertEquals(Languages.ANY, result);
    }

    /**
     * Tests the guessLanguages() method with an input containing only digits.
     */
    @Test
    public void testGuessLanguagesDigits() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        Set<String> result = lang.guessLanguages("12345");
        assertTrue(result.isEmpty());
    }

    /**
     * Tests the guessLanguage() method with an empty rule set.
     */
    @Test
    public void testGuessLanguageEmptyRuleSet() {
        Lang lang = new Lang(Collections.emptyList(null), Languages.instance(EnumSet.allOf(NameType.class)));
        String result = lang.guessLanguage("cat");
        assertEquals(Languages.ANY, result);
    }

    /**
     * Tests the guessLanguages() method with an empty rule set.
     */
    @Test
    public void testGuessLanguagesEmptyRuleSet() {
        Lang lang = new Lang(Collections.emptyList(null), Languages.instance(EnumSet.allOf(NameType.class)));
        Set<String> result = lang.guessLanguages("cat");
        assertTrue(result.isEmpty());
    }

    /**
     * Tests the guessLanguage() method with a rule that matches and accepts all languages.
     */
    @Test
    public void testGuessLanguageAcceptAll() {
        Lang lang = new Lang(Arrays.asList(new LangRule(Pattern.compile(".*"), new HashSet<String>(Arrays.asList(Languages.ENGLISH.getName(), Languages.FRENCH.getName())), true)), Languages.instance(EnumSet.allOf(NameType.class)));
        String result = lang.guessLanguage("cat");
        assertEquals(Languages.ENGLISH, result);

        result = lang.guessLanguage("chat");
        assertEquals(Languages.FRENCH, result);
    }

    /**
     * Tests the guessLanguages() method with a rule that matches and accepts all languages.
     */
    @Test
    public void testGuessLanguagesAcceptAll() {
        Lang lang = new Lang(Arrays.asList(new LangRule(Pattern.compile(".*"), new HashSet<String>(Arrays.asList(Languages.ENGLISH.getName(), Languages.FRENCH.getName())), true)), Languages.instance(EnumSet.allOf(NameType.class)));
        Set<String> result = lang.guessLanguages("cat");
        assertTrue(result.containsAll(Arrays.asList(Languages.ENGLISH.getName(), Languages.FRENCH.getName())));

        result = lang.guessLanguages("chat");
        assertTrue(result.containsAll(Arrays.asList(Languages.ENGLISH.getName(), Languages.FRENCH.getName())));
    }

    /**
     * Tests the guessLanguage() method with a rule that matches but excludes all languages.
     */
    @Test
    public void testGuessLanguageExcludeAll() {
        Lang lang = new Lang(Arrays.asList(new LangRule(Pattern.compile(".*"), new HashSet<String>(Collections.singletonList(Languages.ENGLISH.getName())), false)), Languages.instance(EnumSet.allOf(NameType.class)));
        String result = lang.guessLanguage("cat");
        assertEquals(Languages.ANY, result);

        result = lang.guessLanguage("chat");
        assertEquals(Languages.ANY, result);
    }

    /**
     * Tests the guessLanguages() method with a rule that matches but excludes all languages.
     */
    @Test
    public void testGuessLanguagesExcludeAll() {
        Lang lang = new Lang(Arrays.asList(new LangRule(Pattern.compile(".*"), new HashSet<String>(Collections.singletonList(Languages.ENGLISH.getName())), false)), Languages.instance(EnumSet.allOf(NameType.class)));
        Set<String> result = lang.guessLanguages("cat");
        assertTrue(result.isEmpty());

        result = lang.guessLanguages("chat");
        assertTrue(result.isEmpty());
    }
}