package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;

/**
 * Test class for Lang class.
 *
 * @authorJohn Douglas
 */
public class LangTest {

    /**
     * Test constructor.
     */
    @Test
    public void testLangConstructor() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        assertNotNull(instance);
    }

    /**
     * Test guessLanguage method with English Lang.
     */
    @Test
    public void testGuessLanguageEnglish() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        String result = instance.guessLanguage("hello");
        assertEquals(Languages.ENGLISH, result);
    }

    /**
     * Test guessLanguage method with non-English Lang.
     */
    @Test
    public void testGuessLanguageNonEnglish() {
        Lang instance = Lang.instance(NameType.SPANISH);
        String result = instance.guessLanguage("hola");
        assertEquals(Languages.SPANISH, result);
    }

    /**
     * Test guessLanguages method with English Lang.
     */
    @Test
    public void testGuessLanguagesEnglish() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        Set<String> result = instance.guessLanguages("hello");
        assertTrue(result.contains(Languages.ENGLISH.toString()));
        assertFalse(result.contains(Languages.SPANISH.toString()));
    }

    /**
     * Test guessLanguages method with non-English Lang.
     */
    @Test
    public void testGuessLanguagesNonEnglish() {
        Lang instance = Lang.instance(NameType.SPANISH);
        Set<String> result = instance.guessLanguages("hola");
        assertTrue(result.contains(Languages.SPANISH.toString()));
        assertFalse(result.contains(Languages.ENGLISH.toString()));
    }

    /**
     * Test guessLanguages method with empty String.
     */
    @Test
    public void testGuessLanguagesEmptyString() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        Set<String> result = instance.guessLanguages("");
        assertEquals(Languages.ANY, result.getAny());
    }

    /**
     * Test guessLanguages method with null String.
     */
    @Test(expected = NullPointerException.class)
    public void testGuessLanguagesNullString() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        instance.guessLanguages(null);
    }

    /**
     * Test edge case: String with only spaces.
     */
    @Test
    public void testGuessLanguagesStringWithOnlySpaces() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        Set<String> result = instance.guessLanguages("   ");
        assertEquals(Languages.ANY, result.getAny());
    }

    /**
     * Test edge case: String with only uppercase letters.
     */
    @Test
    public void testGuessLanguagesStringWithOnlyUppercaseLetters() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        Set<String> result = instance.guessLanguages("HELLO");
        assertEquals(Languages.ENGLISH, result.getAny());
    }

    /**
     * Test edge case: String with only digits.
     */
    @Test
    public void testGuessLanguagesStringWithOnlyDigits() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        Set<String> result = instance.guessLanguages("12345");
        assertEquals(Languages.ANY, result.getAny());
    }

    /**
     * Test edge case: String with only special characters.
     */
    @Test
    public void testGuessLanguagesStringWithOnlySpecialCharacters() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        Set<String> result = instance.guessLanguages("!@#$%^&*()_+-=[]{}|\\:;\"'<>,.?/~`");
        assertEquals(Languages.ANY, result.getAny());
    }

    /**
     * Test edge case: String with only lowercase letters.
     */
    @Test
    public void testGuessLanguagesStringWithOnlyLowercaseLetters() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        Set<String> result = instance.guessLanguages("abcdefghijklmnopqrstuvwxyz");
        assertEquals(Languages.ENGLISH, result.getAny());
    }

    /**
     * Test edge case: String with mixed case letters.
     */
    @Test
    public void testGuessLanguagesStringWithMixedCaseLetters() {
        Lang instance = Lang.instance(NameType.ENGLISH);
        Set<String> result = instance.guessLanguages("HeLlO WoRlD");
        assertEquals(Languages.ENGLISH, result.getAny());
    }
}