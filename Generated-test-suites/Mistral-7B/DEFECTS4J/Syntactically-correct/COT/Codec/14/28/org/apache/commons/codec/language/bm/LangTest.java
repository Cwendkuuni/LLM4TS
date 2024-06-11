package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;

/**
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)} method.
     */
    @org.junit.Test
    public void testInstance() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        Lang langGerman = Lang.instance(NameType.GERMAN);

        assertEquals(NameType.ENGLISH, langEnglish.getNameType());
        assertEquals(NameType.GERMAN, langGerman.getNameType());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method.
     */
    @org.junit.Test
    public void testGuessLanguage_EnglishWord() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);

        String word = "hello";
        String expectedLanguage = "en";

        String guessedLanguage = langEnglish.guessLanguage(word);
        assertTrue(guessedLanguage.equals(expectedLanguage) || guessedLanguage.equals(Lang.Languages.ANY.getName()));
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method.
     */
    @org.junit.Test
    public void testGuessLanguage_GermanWord() {
        Lang langGerman = Lang.instance(NameType.GERMAN);

        String word = "hallo";
        String expectedLanguage = "de";

        String guessedLanguage = langGerman.guessLanguage(word);
        assertTrue(guessedLanguage.equals(expectedLanguage) || guessedLanguage.equals(Lang.Languages.ANY.getName()));
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method with ambiguous word.
     */
    @org.junit.Test
    public void testGuessLanguage_AmbiguousWord() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);

        String word = "bank";
        List<String> expectedLanguages = Arrays.asList("en", "de");

        Languages.LanguageSet guessedLanguages = langEnglish.guessLanguages(word);
        assertEquals(expectedLanguages, guessedLanguages.getLanguages());
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} method with empty word.
     */
    @org.junit.Test
    public void testGuessLanguages_EmptyWord() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);

        String word = "";
        Languages.LanguageSet expectedLanguages = Languages.NO_LANGUAGES;

        Languages.LanguageSet guessedLanguages = langEnglish.guessLanguages(word);
        assertEquals(expectedLanguages, guessedLanguages);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} method with null word.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testGuessLanguages_NullWord() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);

        String word = null;
        langEnglish.guessLanguages(word);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} method with invalid rule.
     */
    @org.junit.Test
    public void testGuessLanguages_InvalidRule() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);

        String word = "abc123";
        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList("en"));
        expectedLanguages.remove("any");

        Languages.LanguageSet guessedLanguages = langEnglish.guessLanguages(word);
        assertEquals(expectedLanguages, guessedLanguages);

        // Rule with invalid language code
        LangRule ruleWithInvalidLanguageCode = new LangRule(Pattern.compile(".*"), EnumSet.of("unknown"), false);
        List<LangRule> rulesWithInvalidLanguageCode = Arrays.asList(ruleWithInvalidLanguageCode);
        Lang langWithInvalidLanguageCode = new Lang(rulesWithInvalidLanguageCode, Languages.getInstance(NameType.ENGLISH));

        String wordWithInvalidLanguageCode = "hello";
        Languages.LanguageSet guessedLanguagesWithInvalidLanguageCode = langWithInvalidLanguageCode.guessLanguages(wordWithInvalidLanguageCode);
        assertFalse(guessedLanguagesWithInvalidLanguageCode.isSingleton());
    }
}