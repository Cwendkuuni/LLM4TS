package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.ResourceConstants;
import org.junit.Test;

/**
 * Test file for the Lang class.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        final Lang langEng = Lang.instance(NameType.ENGLISH);
        assertNotNull(langEng);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        final Lang langEng = Lang.instance(NameType.ENGLISH);
        final String englishWord = "hello";

        final String result = langEng.guessLanguage(englishWord);
        assertEquals(NameType.ENGLISH.getCode(), result);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final Lang langEng = Lang.instance(NameType.ENGLISH);
        final String englishWord = "hello";

        final Set<String> expectedLanguages = Collections.singleton("en");
        final Set<String> resultLanguages = langEng.guessLanguages(englishWord);
        assertEquals(expectedLanguages, resultLanguages);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguagesWithMixedResult() {
        final Lang langFr = Lang.instance(NameType.FRENCH);
        final String frenchWord = "bonjour";

        final Set<String> expectedLanguages = Arrays.asList("fr", "oc").asList();
        final Set<String> resultLanguages = langFr.guessLanguages(frenchWord);
        assertEquals(expectedLanguages, resultLanguages);
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguagesWithNoMatch() {
        final Lang langEng = Lang.instance(NameType.ENGLISH);
        final String germanWord = "guten tag";

        final Set<String> expectedLanguages = Collections.singleton(NameType.ANY.getCode());
        final Set<String> resultLanguages = langEng.guessLanguages(germanWord);
        assertEquals(expectedLanguages, resultLanguages);
    }

    /**
     * Test method for {@link Lang#loadFromResource(String, Languages)}.
     */
    @Test
    public void testLoadFromResource() {
        final String langRulesResourceName = "org/apache/commons/codec/language/bm/lang.txt";

        final Languages enLangs = EnumSet.allOf(Languages.class);
        final Lang langEng = Lang.loadFromResource(langRulesResourceName, enLangs);

        assertNotNull(langEng);
        assertNotNull(langEng.rules);
    }
}