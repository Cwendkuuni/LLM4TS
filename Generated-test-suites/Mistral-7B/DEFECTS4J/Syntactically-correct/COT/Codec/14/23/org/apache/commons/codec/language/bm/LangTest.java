package org.apache.commons.codec.language.bm.test;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    /**
     * Test guessLanguage method with English language rules.
     */
    @Test
    public void testGuessLanguageEnglish() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String word = "Java Programming";
        String expectedLanguage = "en";

        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    /**
     * Test guessLanguages method with English language rules.
     */
    @Test
    public void testGuessLanguagesEnglish() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String word = "Java Programming";

        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("en", "nl", "de"));

        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    /**
     * Test guessLanguage method with French language rules.
     */
    @Test
    public void testGuessLanguageFrench() {
        Lang lang = Lang.instance(NameType.FRENCH);
        String word = "Java Programmation";
        String expectedLanguage = "fr";

        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    /**
     * Test guessLanguages method with French language rules.
     */
    @Test
    public void testGuessLanguagesFrench() {
        Lang lang = Lang.instance(NameType.FRENCH);
        String word = "Java Programmation";

        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("fr", "de"));

        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    /**
     * Test guessLanguage method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGuessLanguageInvalidInput() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String word = null;
        lang.guessLanguage(word);
    }

    /**
     * Test guessLanguages method with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGuessLanguagesInvalidInput() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String word = "";
        lang.guessLanguages(word);
    }

    /**
     * Test guessLanguages method with empty rules.
     */
    @Test
    public void testGuessLanguagesEmptyRules() {
        Lang lang = new Lang(Collections.emptyList(null), Languages.getInstance(NameType.ENGLISH));
        String word = "Java Programming";

        Set<String> expectedLanguages = Collections.emptySet();

        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

    /**
     * Test guessLanguage method with no match.
     */
    @Test
    public void testGuessLanguageNoMatch() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String word = "قومه";
        String expectedLanguage = Languages.ANY;

        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    /**
     * Test guessLanguages method with no match.
     */
    @Test
    public void testGuessLanguagesNoMatch() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String word = "قومه";

        Set<String> expectedLanguages = Collections.emptySet();

        Set<String> actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }
}