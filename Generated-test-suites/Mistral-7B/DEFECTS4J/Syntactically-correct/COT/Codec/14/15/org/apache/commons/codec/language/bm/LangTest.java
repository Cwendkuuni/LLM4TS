package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import org.apache.commons.codec.language.bm.NameType;

/**
 * Test class for the Lang class
 */
public class LangTest {

    // Basic test cases for each public method

    @org.junit.Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    @org.junit.Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.getInstance(NameType.ENGLISH));
        assertNotNull(lang);
    }

    @org.junit.Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String word = "hello";
        String expectedLanguage = "en";
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @org.junit.Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String word = "hello";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("en", "xx"));
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    // Edge cases and exception handling test cases

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceWithNullLanguageRulesResourceName() {
        Lang.loadFromResource(null, Languages.getInstance(NameType.ENGLISH));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testInstanceWithNullNameType() {
        Lang.instance(null);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testLoadFromResourceWithNullLanguagesInstance() {
        Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", null);
    }

    @org.junit.Test
    public void testGuessLanguageWithEmptyString() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String word = "";
        String expectedLanguage = Languages.ANY;
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @org.junit.Test
    public void testGuessLanguageWithNullString() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String word = null;
        String expectedLanguage = Languages.ANY;
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @org.junit.Test
    public void testGuessLanguageWithEmptyListOfRules() {
        Lang lang = new Lang(Collections.emptyList(null), Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = Languages.ANY_LANGUAGE;
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithNullRules() {
        Lang lang = new Lang(null, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = Languages.ANY_LANGUAGE;
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithEmptyRulePattern() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(""), new HashSet<String>(), false));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = Languages.ANY_LANGUAGE;
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithEmptyRuleLanguages() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*"), Collections.emptySet(), false));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = Languages.ANY_LANGUAGE;
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithEmptyRuleAcceptOnMatch() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*"), new HashSet<String>(), false));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = Languages.ANY_LANGUAGE;
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithEmptyInput() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        Set<String> expectedLanguages = Languages.NO_LANGUAGES;
        Set<String> actualLanguages = lang.guessLanguages("");
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithNullInput() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        Set<String> expectedLanguages = Languages.NO_LANGUAGES;
        Set<String> actualLanguages = lang.guessLanguages(null);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithInvalidRule() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*"), new HashSet<String>(), false),
                new LangRule(Pattern.compile(".*"), null, false));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = Languages.ANY_LANGUAGE;
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithInvalidRulePattern() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(""), new HashSet<String>(), false),
                new LangRule(Pattern.compile(".*"), new HashSet<String>(), false),
                new LangRule(Pattern.compile(".*"), null, false),
                new LangRule(Pattern.compile(""), new HashSet<String>(), false));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = Languages.ANY_LANGUAGE;
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithInvalidRuleLanguages() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*"), new HashSet<String>(), false),
                new LangRule(Pattern.compile(".*"), new HashSet<String>(new HashSet<String>(Arrays.asList("en", "fr"))), false),
                new LangRule(Pattern.compile(".*"), null, false),
                new LangRule(Pattern.compile(""), new HashSet<String>(), false));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = Languages.ANY_LANGUAGE;
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithInvalidRuleAcceptOnMatch() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*"), new HashSet<String>(), false),
                new LangRule(Pattern.compile(".*"), new HashSet<String>(), true),
                new LangRule(Pattern.compile(".*"), null, false),
                new LangRule(Pattern.compile(""), new HashSet<String>(), false));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = Languages.ANY_LANGUAGE;
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithMultipleMatches() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*[aeiou].*"), new HashSet<String>(Arrays.asList("en", "fr")), false),
                new LangRule(Pattern.compile(".*[èéê].*"), new HashSet<String>(Arrays.asList("fr")), false));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("en", "fr"));
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithMultipleMatchesAndAcceptOnMatch() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*[aeiou].*"), new HashSet<String>(Arrays.asList("en", "fr")), true),
                new LangRule(Pattern.compile(".*[èéê].*"), new HashSet<String>(Arrays.asList("fr")), true));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("en", "fr"));
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithMultipleMatchesAndAcceptOnMatchWithDifferentLanguages() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*[aeiou].*"), new HashSet<String>(Arrays.asList("en", "fr")), true),
                new LangRule(Pattern.compile(".*[èéê].*"), new HashSet<String>(Arrays.asList("it")), true));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("en", "fr", "it"));
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    @org.junit.Test
    public void testGuessLanguageWithMultipleMatchesAndAcceptOnMatchWithSameLanguage() {
        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*[aeiou].*"), new HashSet<String>(Arrays.asList("en", "en")), true),
                new LangRule(Pattern.compile(".*[èéê].*"), new HashSet<String>(Arrays.asList("fr")), true));
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        String word = "hello";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("en"));
        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }
}