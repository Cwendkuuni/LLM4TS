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
import org.apache.commons.codec.language.bm.NameType;

/**
 * Test class for Lang class
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}
     */
    @Test
    public void testInstance() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertNotNull(englishLang);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}
     */
    @Test
    public void testGuessLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        String englishWord = "hello";
        String englishLanguage = englishLang.guessLanguage(englishWord);
        assertEquals(Languages.ENGLISH.getName(), englishLanguage);

        String frenchWord = "bonjour";
        String frenchLanguage = englishLang.guessLanguage(frenchWord);
        assertNotEquals(Languages.ENGLISH.getName(), frenchLanguage);
        assertEquals(Languages.FRENCH.getName(), frenchLanguage);

        String germanWord = "hallo";
        String germanLanguage = englishLang.guessLanguage(germanWord);
        assertNotEquals(Languages.ENGLISH.getName(), germanLanguage);
        assertEquals(Languages.GERMAN.getName(), germanLanguage);

        String anyWord = "12345";
        String anyLanguage = englishLang.guessLanguage(anyWord);
        assertEquals(Languages.ANY.getName(), anyLanguage);

        String emptyWord = "";
        String emptyLanguage = englishLang.guessLanguage(emptyWord);
        assertEquals(Languages.ANY.getName(), emptyLanguage);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}
     */
    @Test
    public void testGuessLanguages() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        String englishWord = "hello";
        Set<String> expectedLanguages = Collections.singleton(Languages.ENGLISH.getName());
        Set<String> languages = englishLang.guessLanguages(englishWord);
        assertEquals(expectedLanguages, languages);

        String frenchWord = "bonjour";
        Set<String> languages1 = englishLang.guessLanguages(frenchWord);
        Set<String> expectedLanguages1 = Arrays.asList(Languages.ENGLISH.getName(), Languages.FRENCH.getName());
        assertEquals(expectedLanguages1, languages1);

        String germanWord = "hallo";
        Set<String> languages2 = englishLang.guessLanguages(germanWord);
        Set<String> expectedLanguages2 = Arrays.asList(Languages.ENGLISH.getName(), Languages.GERMAN.getName());
        assertEquals(expectedLanguages2, languages2);

        String anyWord = "12345";
        Set<String> expectedLanguages3 = Collections.singleton(Languages.ANY.getName());
        Set<String> languages3 = englishLang.guessLanguages(anyWord);
        assertEquals(expectedLanguages3, languages3);

        String emptyWord = "";
        Set<String> expectedLanguages4 = Collections.singleton(Languages.ANY.getName());
        Set<String> languages4 = englishLang.guessLanguages(emptyWord);
        assertEquals(expectedLanguages4, languages4);
    }

    /**
     * Test method for {@link Lang#Lang(List, Languages)} constructor
     */
    @Test
    public void testLangConstruction() {
        List<LangRule> rules = Arrays.asList(
                new LangRule(Pattern.compile("^[\\p{L}]+.*"), new HashSet<>(Arrays.asList("ru")), true),
                new LangRule(Pattern.compile("^[\\p{L}]+.*"), new HashSet<>(Arrays.asList("de")), false)
        );
        Languages languages = new Languages(EnumSet.allOf(NameType.class), Arrays.asList("ru", "de", "en"));

        Lang lang = new Lang(rules, languages);

        assertEquals(rules, lang.getRules());
        assertEquals(languages, lang.getLanguages());
    }

    /**
     * Test method for {@link Lang#matchRules(String)}
     */
    @Test
    public void testMatchRules() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        List<LangRule> rules = englishLang.getRules();

        String englishWord = "hello";
        boolean isMatch = false;
        for (LangRule rule : rules) {
            if (rule.matches(englishWord)) {
                isMatch = true;
                break;
            }
        }
        assertTrue(isMatch);

        String frenchWord = "bonjour";
        isMatch = false;
        for (LangRule rule : rules) {
            if (rule.matches(frenchWord)) {
                isMatch = true;
                break;
            }
        }
        assertFalse(isMatch);

        String germanWord = "hallo";
        isMatch = false;
        for (LangRule rule : rules) {
            if (rule.matches(germanWord)) {
                isMatch = true;
                break;
            }
        }
        assertFalse(isMatch);
    }

    /**
     * Test method for {@link Lang#acceptOnMatchRules(String)}
     */
    @Test
    public void testAcceptOnMatchRules() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        List<LangRule> rules = englishLang.getRules();

        String englishWord = "hello";
        boolean accept = false;
        for (LangRule rule : rules) {
            if (rule.acceptOnMatch() && rule.matches(englishWord)) {
                accept = true;
                break;
            }
        }
        assertTrue(accept);

        String frenchWord = "bonjour";
        accept = false;
        for (LangRule rule : rules) {
            if (!rule.acceptOnMatch() && rule.matches(frenchWord)) {
                accept = true;
                break;
            }
        }
        assertFalse(accept);

        String germanWord = "hallo";
        accept = false;
        for (LangRule rule : rules) {
            if (rule.acceptOnMatch() && rule.matches(germanWord)) {
                accept = true;
                break;
            }
        }
        assertFalse(accept);

        String russianWord = "привет";
        accept = false;
        for (LangRule rule : rules) {
            if (!rule.acceptOnMatch() && rule.matches(russianWord)) {
                accept = true;
                break;
            }
        }
        assertFalse(accept);

        String anyWord = "12345";
        accept = false;
        for (LangRule rule : rules) {
            if (rule.acceptOnMatch() && rule.matches(anyWord)) {
                accept = true;
                break;
            }
        }
        assertFalse(accept);
    }
}