package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals("English language instance", "en", englishLang.getLanguageIdentifier());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test English words
        assertTrue("English word 'hello' should be identified as English",
                englishLang.guessLanguage("hello").isSingleton());
        assertEquals("English word 'hello' should be identified as English", "en",
                englishLang.guessLanguage("hello").getAny());

        assertTrue("English word 'world' should be identified as English",
                englishLang.guessLanguage("world").isSingleton());
        assertEquals("English word 'world' should be identified as English", "en",
                englishLang.guessLanguage("world").getAny());

        // Test non-English words
        final String nonEnglishWord = "Hallo";
        assertFalse("Non-English word 'Hallo' should not be identified as English",
                englishLang.guessLanguage(nonEnglishWord).isSingleton());
        assertEquals("Non-English word 'Hallo' should not be identified as English",
                Languages.ANY, englishLang.guessLanguage(nonEnglishWord));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        // Test English words
        final Languages.LanguageSet expectedEnglishLanguages = Languages.languageSet("en");
        final Languages.LanguageSet englishLanguages = englishLang.guessLanguages("hello");
        assertTrue("Guessed languages for 'hello' should contain expected languages",
                englishLanguages.isSubsetOf(expectedEnglishLanguages));

        // Test non-English words
        final String nonEnglishWord = "Hallo";
        final Languages.LanguageSet expectedNonEnglishLanguages = Languages.languageSet("de");
        final Languages.LanguageSet nonEnglishLanguages = englishLang.guessLanguages(nonEnglishWord);
        assertTrue("Guessed languages for 'Hallo' should contain expected languages",
                nonEnglishLanguages.containsAll(expectedNonEnglishLanguages));
        assertFalse("Guessed languages for 'Hallo' should not contain English",
                englishLanguages.contains("en"));
    }
}