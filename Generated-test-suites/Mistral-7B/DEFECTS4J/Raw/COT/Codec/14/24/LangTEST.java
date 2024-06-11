package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

/**
 * JUnit 4 test cases for the Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        final NameType nameType = NameType.DETECT;
        final Lang lang = Lang.instance(nameType);
        assertNotNull(lang);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        final String input = "Hola";
        final NameType nameType = NameType.DETECT;
        final Lang lang = Lang.instance(nameType);
        final String result = lang.guessLanguage(input);
        assertEquals(Languages.SPANISH, result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final String input = "Hola";
        final NameType nameType = NameType.DETECT;
        final Lang lang = Lang.instance(nameType);
        final Set<String> result = lang.guessLanguages(input);
        assertTrue(result.contains(Languages.SPANISH.getName()));
        assertFalse(result.contains(Languages.ENGLISH.getName()));
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with an empty String.
     */
    @Test
    public void testGuessLanguageEmptyString() {
        final String input = "";
        final NameType nameType = NameType.DETECT;
        final Lang lang = Lang.instance(nameType);
        final String result = lang.guessLanguage(input);
        assertEquals(Languages.ANY, result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with an empty String.
     */
    @Test
    public void testGuessLanguagesEmptyString() {
        final String input = "";
        final NameType nameType = NameType.DETECT;
        final Lang lang = Lang.instance(nameType);
        final Set<String> result = lang.guessLanguages(input);
        assertTrue(result.isEmpty());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testGuessLanguageNullInput() {
        final NameType nameType = NameType.DETECT;
        final Lang lang = Lang.instance(nameType);
        lang.guessLanguage(null);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with null input.
     */
    @Test(expected = NullPointerException.class)
    public void testGuessLanguagesNullInput() {
        final NameType nameType = NameType.DETECT;
        final Lang lang = Lang.instance(nameType);
        lang.guessLanguages(null);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with an input containing a pattern that matches multiple languages.
     */
    @Test
    public void testGuessLanguageAmbiguous() {
        final String input = "Apple";
        final NameType nameType = NameType.DETECT;
        final Lang lang = Lang.instance(nameType);
        final Set<String> result = lang.guessLanguages(input);
        assertTrue(result.contains(Languages.ENGLISH.getName()));
        assertTrue(result.contains(Languages.RUSSIAN.getName()));
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with an input containing a pattern that doesn't match any language.
     */
    @Test
    public void testGuessLanguageNoMatch() {
        final String input = "Xyzzy";
        final NameType nameType = NameType.DETECT;
        final Lang lang = Lang.instance(nameType);
        final String result = lang.guessLanguage(input);
        assertEquals(Languages.ANY, result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with an input containing a pattern that doesn't match any language.
     */
    @Test
    public void testGuessLanguagesNoMatch() {
        final String input = "Xyzzy";
        final NameType nameType = NameType.DETECT;
        final Lang lang = Lang.instance(nameType);
        final Set<String> result = lang.guessLanguages(input);
        assertTrue(result.isEmpty());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with an input that matches an explicit accept rule.
     */
    @Test
    public void testGuessLanguageAccept() {
        final String input = "Monday";
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);
        final String result = lang.guessLanguage(input);
        assertEquals(Languages.ENGLISH, result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with an input that matches an explicit accept rule.
     */
    @Test
    public void testGuessLanguagesAccept() {
        final String input = "Monday";
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);
        final Set<String> result = lang.guessLanguages(input);
        assertEquals(Collections.singleton(Languages.ENGLISH.getName()), result);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with an input that matches an explicit reject rule.
     */
    @Test
    public void testGuessLanguageReject() {
        final String input = "Monday";
        final NameType nameType = NameType.FRENCH;
        final Lang lang = Lang.instance(nameType);
        final String result = lang.guessLanguage(input);
        assertEquals(Languages.ANY, result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with an input that matches an explicit reject rule.
     */
    @Test
    public void testGuessLanguagesReject() {
        final String input = "Monday";
        final NameType nameType = NameType.FRENCH;
        final Lang lang = Lang.instance(nameType);
        final Set<String> result = lang.guessLanguages(input);
        assertTrue(result.isEmpty());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with an input that contains a comment.
     */
    @Test
    public void testGuessLanguageComment() {
        final String input = "/* This is a comment */Apple";
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);
        final String result = lang.guessLanguage(input);
        assertEquals(Languages.ENGLISH, result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with an input that contains a comment.
     */
    @Test
    public void testGuessLanguagesComment() {
        final String input = "/* This is a comment */Apple";
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);
        final Set<String> result = lang.guessLanguages(input);
        assertEquals(Collections.singleton(Languages.ENGLISH.getName()), result);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with an input that contains a multi-line comment.
     */
    @Test
    public void testGuessLanguageMultiLineComment() {
        final String input = "/*\nThis\nis\n a\n multi-line\ncomment\n*/Apple";
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);
        final String result = lang.guessLanguage(input);
        assertEquals(Languages.ENGLISH, result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with an input that contains a multi-line comment.
     */
    @Test
    public void testGuessLanguagesMultiLineComment() {
        final String input = "/*\nThis\nis\n a\n multi-line\ncomment\n*/Apple";
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);
        final Set<String> result = lang.guessLanguages(input);
        assertEquals(Collections.singleton(Languages.ENGLISH.getName()), result);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} with an input that starts with a blank line.
     */
    @Test
    public void testGuessLanguageBlankLine() {
        final String input = "\nApple";
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);
        final String result = lang.guessLanguage(input);
        assertEquals(Languages.ENGLISH, result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)} with an input that starts with a blank line.
     */
    @Test
    public void testGuessLanguagesBlankLine() {
        final String input = "\nApple";
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);
        final Set<String> result = lang.guessLanguages(input);
        assertEquals(Collections.singleton(Languages.ENGLISH.getName()), result);
    }

}