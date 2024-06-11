package org.apache.commons.codec.language.bm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test cases for Lang class.
 *
 * @author <a href="mailto:juan.fernandez-reero@gmail.com">Juan Fernandez-Reero</a>
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @org.junit.Test
    public void testInstance() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        assertNotNull(instance);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @org.junit.Test
    public void testGuessLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test with English word
        String englishWord = "hello";
        String expectedEnglishLanguage = Languages.ENGLISH.getLanguageName();
        String actualEnglishLanguage = englishLang.guessLanguage(englishWord);
        assertEquals(expectedEnglishLanguage, actualEnglishLanguage);

        // Test with non-English word
        String nonEnglishWord = "hola";
        String expectedAnyLanguage = Languages.ANY.getLanguageName();
        String actualAnyLanguage = englishLang.guessLanguage(nonEnglishWord);
        assertEquals(expectedAnyLanguage, actualAnyLanguage);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @org.junit.Test
    public void testGuessLanguages() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test with English word
        String englishWord = "hello";
        List<String> expectedEnglishLanguages = Arrays.asList(Languages.ENGLISH.getLanguageName());
        List<String> actualEnglishLanguages = englishLang.guessLanguages(englishWord);
        assertEquals(expectedEnglishLanguages, actualEnglishLanguages);

        // Test with non-English word
        String nonEnglishWord = "hola";
        List<String> expectedAnyLanguages = Arrays.asList(Languages.ENGLISH.getLanguageName(), Languages.SPANISH.getLanguageName());
        List<String> actualAnyLanguages = englishLang.guessLanguages(nonEnglishWord);
        assertEquals(expectedAnyLanguages, actualAnyLanguages);
    }

    /**
     * Test method for {@link Lang#Lang(List, Languages)}.
     */
    @org.junit.Test
    public void testLangConstructor() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang.txt";
        Languages languages = Languages.getInstance(NameType.ENGLISH);
        Lang lang = new Lang(Arrays.asList(new LangRule(Pattern.compile(".*"), new HashSet<String>(Arrays.asList(Languages.ENGLISH.getLanguageName())), true)), languages);
        assertNotNull(lang);
    }

    /**
     * Test method for {@link LangRule#matches(String)}.
     */
    @org.junit.Test
    public void testLangRuleMatches() {
        String text = "hello";
        LangRule rule = new LangRule(Pattern.compile(".*hello.*"), new HashSet<String>(Arrays.asList(Languages.ENGLISH.getLanguageName())), true);
        boolean expectedResult = true;
        boolean actualResult = rule.matches(text);
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Test method for {@link LangRule#matches(String)}.
     */
    @org.junit.Test
    public void testLangRuleDoesNotMatch() {
        String text = "hola";
        LangRule rule = new LangRule(Pattern.compile(".*hello.*"), new HashSet<String>(Arrays.asList(Languages.ENGLISH.getLanguageName())), true);
        boolean expectedResult = false;
        boolean actualResult = rule.matches(text);
        assertEquals(expectedResult, actualResult);
    }
}