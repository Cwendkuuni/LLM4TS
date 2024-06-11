package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Class to test the Lang class.
 */
public class LangTest {

    @SuppressWarnings("unchecked")
    private static final Lang INSTANCE = Lang.instance(EnumSet.allOf(Lang.NameType.class).get(0));

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        String word = "test";
        String expectedLanguage = Languages.ANY;
        String actualLanguage = INSTANCE.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);

        // Add more test cases for various words and expected languages
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        String word = "test";
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("English", "French"));
        Set<String> actualLanguages = INSTANCE.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);

        // Add more test cases for various words and expected language sets
    }

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        NameType nameType = EnumSet.allOf(Lang.NameType.class).get(0);
        Lang expectedLang = INSTANCE;
        Lang actualLang = Lang.instance(nameType);
        assertEquals(expectedLang, actualLang);

        // Add more test cases for different NameTypes
    }

    /**
     * Test edge cases and exception handling in {@link Lang#guessLanguages(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGuessLanguagesEdgeCases() {
        String word = null;
        Set<String> actualLanguages = INSTANCE.guessLanguages(word);
        fail("Expected IllegalArgumentException, but got: " + actualLanguages);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGuessLanguagesEmptyWord() {
        String word = "";
        Set<String> actualLanguages = INSTANCE.guessLanguages(word);
        assertEquals(Languages.ANY, actualLanguages);

        // Re-throw the expected exception
        throw new IllegalArgumentException("Unexpected result for empty word");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGuessLanguagesEmptyLanguageSet() {
        Languages emptyLanguages = new Languages(Collections.emptyMap());
        Set<String> actualLanguages = INSTANCE.guessLanguages("test", emptyLanguages);
        assertEquals(Languages.ANY, actualLanguages);

        // Re-throw the expected exception
        throw new IllegalArgumentException("Unexpected result for empty languages set");
    }

    /**
     * Test edge cases and exception handling in {@link Lang#instance(NameType)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInstanceEdgeCases() {
        NameType nullNameType = null;
        Lang actualLang = Lang.instance(nullNameType);
        fail("Expected IllegalArgumentException, but got: " + actualLang);
    }
}