package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * JUnit Test file for Lang class.
 */
public class LangTest {

    /**
     * Test instance method with valid arguments.
     */
    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    /**
     * Test loadFromResource method with valid arguments.
     */
    @Test
    public void testLoadFromResource() throws IOException {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_en.txt";
        Languages languages = Languages.getInstance(NameType.ENGLISH);
        Lang lang = Lang.loadFromResource(languageRulesResourceName, languages);
        assertNotNull(lang);
    }

    /**
     * Test guessLanguage method with valid arguments.
     */
    @Test
    public void testGuessLanguage() {
        String input = "hello world";
        Lang lang = Lang.instance(NameType.ENGLISH);
        String expectedLanguage = "en";
        String actualLanguage = lang.guessLanguage(input);
        assertEquals(expectedLanguage, actualLanguage);
    }

    /**
     * Test guessLanguages method with valid arguments.
     */
    @Test
    public void testGuessLanguages() {
        String input = "bonjour le monde";
        Lang lang = Lang.instance(NameType.FRENCH);
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("fr", "fr_BE"));
        Set<String> actualLanguages = lang.guessLanguages(input);
        assertEquals(expectedLanguages, actualLanguages);
    }

    /**
     * Test guessLanguages method with invalid arguments.
     */
    @Test
    public void testGuessLanguagesInvalidArguments() {
        String input = "";
        Lang lang = Lang.instance(NameType.ENGLISH);
        Set<String> actualLanguages = lang.guessLanguages(input);
        assertTrue(actualLanguages.isEmpty());

        input = "hello";
        expectedLanguages = Collections.singleton("en");
        actualLanguages = lang.guessLanguages(input);
        assertEquals(expectedLanguages, actualLanguages);
    }

    /**
     * Test LangRule class with valid arguments.
     */
    @Test
    public void testLangRule() {
        String patternString = "[aeiou]";
        Set<String> languages = new HashSet<>(Arrays.asList("fr", "de"));
        boolean acceptOnMatch = true;
        Pattern pattern = Pattern.compile(patternString);
        LangRule langRule = new LangRule(pattern, languages, acceptOnMatch);
        String input = "apple";

        assertTrue(langRule.matches(input));
        assertFalse(langRule.acceptsLanguage(input, Lang.instance(NameType.ENGLISH)));

        input = "hello";
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("en", "fr"));
        Set<String> actualLanguages = langRule.guessLanguages(input, Lang.instance(NameType.ENGLISH));
        assertEquals(expectedLanguages, actualLanguages);
    }

    /**
     * Test LangRule class with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLangRuleInvalidArguments() {
        String patternString = "[aeiou]";
        Set<String> languages = new HashSet<>(Arrays.asList("fr", "de"));
        boolean acceptOnMatch = true;
        Pattern pattern = null;
        new LangRule(pattern, languages, acceptOnMatch);
    }

    /**
     * Test Lang class with valid arguments.
     */
    @Test
    public void testLang() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_en.txt";
        Languages languages = Languages.getInstance(NameType.ENGLISH);
        Lang lang = Lang.loadFromResource(languageRulesResourceName, languages);

        String input = "hello world";
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("en"));
        Set<String> actualLanguages = lang.guessLanguages(input);
        assertEquals(expectedLanguages, actualLanguages);
    }

    /**
     * Test Lang class with invalid arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLangInvalidArguments() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_non_existent.txt";
        Languages languages = Languages.getInstance(NameType.ENGLISH);
        Lang lang = Lang.loadFromResource(languageRulesResourceName, languages);
    }

    /**
     * Test Lang class with different language rules for different NameTypes.
     */
    @Test
    public void testLangsWithDifferentLanguageRules() {
        String languageRulesResourceName_en = "org/apache/commons/codec/language/bm/lang_en.txt";
        Languages languages_en = Languages.getInstance(NameType.ENGLISH);
        Lang lang_en = Lang.loadFromResource(languageRulesResourceName_en, languages_en);

        String languageRulesResourceName_fr = "org/apache/commons/codec/language/bm/lang_fr.txt";
        Languages languages_fr = Languages.getInstance(NameType.FRENCH);
        Lang lang_fr = Lang.loadFromResource(languageRulesResourceName_fr, languages_fr);

        String input = "hello world";

        String expectedLanguage_en = "en";
        String actualLanguage_en = lang_en.guessLanguage(input);
        assertEquals(expectedLanguage_en, actualLanguage_en);

        Set<String> expectedLanguages_fr = new HashSet<>(Arrays.asList("fr", "fr_BE"));
        Set<String> actualLanguages_fr = lang_fr.guessLanguages(input);
        assertEquals(expectedLanguages_fr, actualLanguages_fr);
    }

    /**
     * Test Lang class with empty rule set.
     */
    @Test
    public void testLangEmptyRuleSet() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_empty.txt";
        Languages languages = Languages.getInstance(NameType.ENGLISH);
        Lang lang = Lang.loadFromResource(languageRulesResourceName, languages);

        String input = "hello world";
        Set<String> expectedLanguages = Collections.singleton("en");
        Set<String> actualLanguages = Lang.instance(NameType.ENGLISH).guessLanguages(input);
        assertEquals(expectedLanguages, actualLanguages);

        String expectedLanguage = "en";
        String actualLanguage = lang.guessLanguage(input);
        assertEquals(expectedLanguage, actualLanguage);
    }

    /**
     * Test Lang class with invalid rule set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLangInvalidRuleSet() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_invalid.txt";
        Languages languages = Languages.getInstance(NameType.ENGLISH);
        Lang lang = Lang.loadFromResource(languageRulesResourceName, languages);
    }

    /**
     * Test Lang class with malformed rule set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLangMalformedRuleSet() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_malformed.txt";
        Languages languages = Languages.getInstance(NameType.ENGLISH);
        Lang lang = Lang.loadFromResource(languageRulesResourceName, languages);
    }

    /**
     * Test Lang class with commented out rule set.
     */
    @Test
    public void testLangCommentedOutRuleSet() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_commented_out.txt";
        Languages languages = Languages.getInstance(NameType.ENGLISH);
        Lang lang = Lang.loadFromResource(languageRulesResourceName, languages);

        String input = "hello world";
        Set<String> expectedLanguages = Collections.singleton("en");
        Set<String> actualLanguages = Lang.instance(NameType.ENGLISH).guessLanguages(input);
        assertEquals(expectedLanguages, actualLanguages);

        String expectedLanguage = "en";
        String actualLanguage = lang.guessLanguage(input);
        assertEquals(expectedLanguage, actualLanguage);
    }

    private static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Expected object to not be null");
        }
    }
}