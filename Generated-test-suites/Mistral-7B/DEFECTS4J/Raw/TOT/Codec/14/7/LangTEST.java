package org.apache.commons.codec.language.bm;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Lang utility.
 */
public class LangTest {

    /**
     * Tests language guessing for English words.
     */
    @Test
    public void testEnglishWords() {
        final Lang lang = Lang.instance(NameType.ENGLISH);
        final String[] englishWords = new String[]{"apple", "banana", "cat", "dog", "elephant"};
        for (final String word : englishWords) {
            assertEquals(Lang.Languages.ENGLISH, lang.guessLanguage(word));
        }
    }

    /**
     * Tests language guessing for non-English words.
     */
    @Test
    public void testNonEnglishWords() {
        final Lang lang = Lang.instance(NameType.GERMAN);
        final String[] germanWords = new String[]{"Apfel", "Banane", "Katze", "Hund", "Elefant"};
        for (final String word : germanWords) {
            assertEquals(Lang.Languages.GERMAN, lang.guessLanguage(word));
        }
    }

    /**
     * Tests language guessing for an empty string.
     */
    @Test
    public void testEmptyString() {
        final Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals(Lang.Languages.ANY, lang.guessLanguage(""));
    }

    /**
     * Tests language guessing for a word containing invalid characters.
     */
    @Test
    public void testInvalidCharacters() {
        final Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("\u0000apple"));
    }

    /**
     * Tests language guessing for a word with uppercase letters.
     */
    @Test
    public void testUppercaseLetters() {
        final Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals(Lang.Languages.ENGLISH, lang.guessLanguage("Apple"));
    }

    /**
     * Tests language guessing for a multi-word input.
     */
    @Test
    public void testMultiWordInput() {
        final Lang lang = Lang.instance(NameType.ENGLISH);
        final String text = "the quick brown fox jumps over the lazy dog";
        final Languages.LanguageSet languages = lang.guessLanguages(text);
        assertTrue(languages.contains(Lang.Languages.ENGLISH));
    }

    /**
     * Tests language guessing for a multi-language input.
     */
    @Test
    public void testMultiLanguageInput() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final Lang germanLang = Lang.instance(NameType.GERMAN);
        final String text = "Apfel Banane Katze Hund Elefant the quick brown fox jumps over the lazy dog";
        final Languages.LanguageSet englishLanguages = englishLang.guessLanguages(text);
        assertTrue(englishLanguages.contains(Lang.Languages.ENGLISH));

        final Languages.LanguageSet germanLanguages = germanLang.guessLanguages(text);
        assertTrue(germanLanguages.contains(Lang.Languages.GERMAN));
    }
}