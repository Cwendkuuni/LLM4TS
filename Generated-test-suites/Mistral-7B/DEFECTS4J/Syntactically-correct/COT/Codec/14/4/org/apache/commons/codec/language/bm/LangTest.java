package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test class for Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}
     */
    @Test
    public void testInstance() {
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);
        assertNotNull(lang);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}
     */
    @Test
    public void testGuessLanguage() {
        final Lang lang = Lang.instance(NameType.ENGLISH);
        final String englishText = "hello world";
        final String englishLanguage = lang.guessLanguage(englishText);
        assertEquals(Languages.ENGLISH.getLangName(), englishLanguage);

        final String spanishText = "hola mundo";
        final String spanishLanguage = lang.guessLanguage(spanishText);
        assertEquals(Languages.SPANISH.getLangName(), spanishLanguage);

        final String germanText = "hallo welt";
        final String germanLanguage = lang.guessLanguage(germanText);
        assertEquals(Languages.GERMAN.getLangName(), germanLanguage);

        final String randomText = "randomText";
        final String anyLanguage = lang.guessLanguage(randomText);
        assertEquals(Languages.ANY.getLangName(), anyLanguage);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}
     */
    @Test
    public void testGuessLanguages() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        final String englishText = "hello world";
        final Languages.LanguageSet englishLanguages = lang.guessLanguages(englishText);
        assertEquals(Collections.singletonList(Languages.ENGLISH), englishLanguages.getLanguages());

        final String spanishText = "hola mundo";
        final Languages.LanguageSet spanishLanguages = lang.guessLanguages(spanishText);
        final List<String> spanishLangs = spanishLanguages.getLanguages();
        assertTrue(spanishLangs.contains(Languages.SPANISH.getLangName()));
        assertTrue(spanishLangs.contains(Languages.ITALIAN.getLangName())); // Italian and Spanish have similar rules

        final String germanText = "hallo welt";
        final Languages.LanguageSet germanLanguages = lang.guessLanguages(germanText);
        assertEquals(Collections.singletonList(Languages.GERMAN), germanLanguages.getLanguages());

        final String randomText = "randomText";
        final Languages.LanguageSet anyLanguages = lang.guessLanguages(randomText);
        assertEquals(Languages.ANY_LANGUAGES, anyLanguages);

        final String emptyText = "";
        final Languages.LanguageSet emptyLanguages = lang.guessLanguages(emptyText);
        assertEquals(Collections.singletonList(Languages.ANY), emptyLanguages.getLanguages());
    }

    /**
     * Test method for edge cases and exception handling.
     */
    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException() {
        final Lang lang = Lang.instance(NameType.ENGLISH);
        final String nullText = null;
        lang.guessLanguage(nullText);
    }
}