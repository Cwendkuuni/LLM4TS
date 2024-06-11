package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;

/**
 * JUnit test file for the Lang class.
 *
 * @author john.doe
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)} and {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);

        // Basic functionality test
        assertEquals("en", lang.guessLanguage("hello"));

        // Test for language not supported by the given NameType
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("こんにちは"));

        // Test for empty input
        assertEquals(Lang.Languages.ANY, lang.guessLanguage(""));

        // Test for null input
        assertNull(lang.guessLanguage(null));

        // Test for edge cases
        final String edgeCase1 = "Café";
        final String edgeCase2 = "Müller";
        final String edgeCase3 = "Schönste Strasse";

        assertEquals("de", lang.guessLanguage(edgeCase1));
        assertEquals("de", lang.guessLanguage(edgeCase2));
        assertEquals("de", lang.guessLanguage(edgeCase3));
    }

    /**
     * Test method for {@link Lang#instance(NameType)} and {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final NameType nameType = NameType.ENGLISH;
        final Lang lang = Lang.instance(nameType);

        // Basic functionality test
        final Set<String> expectedLanguages = Arrays.asList("en", "de", "nl");
        final Set<String> languages = lang.guessLanguages("Hallo Welt");
        assertEquals(expectedLanguages, languages);

        // Test for empty input
        final Set<String> emptyInputLanguages = Collections.singleton(Lang.Languages.ANY);
        final Set<String> languagesEmptyInput = lang.guessLanguages("");
        assertEquals(emptyInputLanguages, languagesEmptyInput);

        // Test for null input
        final Set<String> nullInputLanguages = Collections.singleton(Lang.Languages.ANY);
        final Set<String> languagesNullInput = lang.guessLanguages(null);
        assertEquals(nullInputLanguages, languagesNullInput);

        // Test for edge cases
        final String edgeCase1 = "Café";
        final String edgeCase2 = "Müller";
        final String edgeCase3 = "Schönste Strasse";

        final Set<String> edgeCase1Languages = Arrays.asList("de");
        final Set<String> edgeCase2Languages = Arrays.asList("de");
        final Set<String> edgeCase3Languages = Arrays.asList("de", "fr");

        final Set<String> languagesEdgeCase1 = lang.guessLanguages(edgeCase1);
        assertEquals(edgeCase1Languages, languagesEdgeCase1);

        final Set<String> languagesEdgeCase2 = lang.guessLanguages(edgeCase2);
        assertEquals(edgeCase2Languages, languagesEdgeCase2);

        final Set<String> languagesEdgeCase3 = lang.guessLanguages(edgeCase3);
        assertEquals(edgeCase3Languages, languagesEdgeCase3);

        // Test for multiple languages with the same probability
        final String multipleLanguagesInput = "Hola Hola";
        final Set<String> multipleLanguagesExpected = Arrays.asList("es", "en");
        final Set<String> multipleLanguagesActual = lang.guessLanguages(multipleLanguagesInput);
        assertEquals(multipleLanguagesExpected, multipleLanguagesActual);
    }

    /**
     * Test method for {@link Lang#instance(NameType)} and {@link Lang#guessLanguage(String)} with unknown NameType.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnknownNameType() {
        final NameType unknownNameType = new NameType("unknown");
        Lang.instance(unknownNameType);
    }
}