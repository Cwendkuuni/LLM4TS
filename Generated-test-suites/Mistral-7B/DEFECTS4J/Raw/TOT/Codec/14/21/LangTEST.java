package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertNotNull(englishLang);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        assertEquals(Languages.ENGLISH, englishLang.guessLanguage("hello"));
        assertEquals(Languages.ENGLISH, englishLang.guessLanguage("world"));
        assertEquals(Languages.ENGLISH, englishLang.guessLanguage("hello world"));

        assertEquals(Languages.ANY, englishLang.guessLanguage("hola"));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        final Set<String> expectedLanguages = new HashSet<String>(Arrays.asList(Languages.ENGLISH.getName(), Languages.SPANISH.getName()));
        final Languages.LanguageSet result = englishLang.guessLanguages("hola");

        assertTrue(result.contains(Languages.ENGLISH));
        assertTrue(result.contains(Languages.SPANISH));
        assertFalse(result.contains(Languages.FRENCH.getName()));

        assertEquals(expectedLanguages, result.getLanguages());
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguagesEmpty() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);

        final Languages.LanguageSet result = englishLang.guessLanguages("");
        assertEquals(Languages.ANY, result);
    }
}