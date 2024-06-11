package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JUnit test cases for the Lang class.
 */
public class LangTest {

    private static Lang englishLang;
    private static Lang frenchLang;

    @BeforeClass
    public static void setUp() {
        englishLang = Lang.instance(NameType.ENGLISH);
        frenchLang = Lang.instance(NameType.FRENCH);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with English language rules.
     */
    @Test
    public void testGuessLanguageEnglish() {
        assertEquals("en", englishLang.guessLanguage("hello"));
        assertEquals("en", englishLang.guessLanguage("goodbye"));
        assertEquals("en", englishLang.guessLanguage("apple"));
        assertEquals("en", englishLang.guessLanguage("banana"));
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with French language rules.
     */
    @Test
    public void testGuessLanguageFrench() {
        assertEquals("fr", frenchLang.guessLanguage("bonjour"));
        assertEquals("fr", frenchLang.guessLanguage("au_revoir"));
        assertEquals("fr", frenchLang.guessLanguage("pomme"));
        assertEquals("fr", frenchLang.guessLanguage("banane"));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with English language rules.
     */
    @Test
    public void testGuessLanguagesEnglish() {
        Set<String> expected = new HashSet<String>(Arrays.asList("en", "de", "es"));
        Set<String> result = englishLang.guessLanguages("apple");
        assertEquals(expected, result);

        Set<String> emptyResult = englishLang.guessLanguages("asdfasdf");
        assertEquals(Languages.NO_LANGUAGES, emptyResult);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with French language rules.
     */
    @Test
    public void testGuessLanguagesFrench() {
        Set<String> expected = new HashSet<String>(Arrays.asList("fr", "es"));
        Set<String> result = frenchLang.guessLanguages("bonjour");
        assertEquals(expected, result);

        Set<String> emptyResult = frenchLang.guessLanguages("asdfasdf");
        assertEquals(Languages.NO_LANGUAGES, emptyResult);
    }

    /**
     * Test edge cases and exception handling for {@link Lang#guessLanguage(String)} method.
     */
    @Test(expected = IllegalStateException.class)
    public void testGuessLanguageException() {
        Lang nonExistentLang = new Lang(new ArrayList<>(), Languages.getInstance(NameType.NO_NAME));
        nonExistentLang.guessLanguage("test");
    }

    /**
     * Test edge cases and exception handling for {@link Lang#guessLanguages(String)} method.
     */
    @Test(expected = IllegalStateException.class)
    public void testGuessLanguagesException() {
        Lang nonExistentLang = new Lang(new ArrayList<>(), Languages.getInstance(NameType.NO_NAME));
        nonExistentLang.guessLanguages("test");
    }
}