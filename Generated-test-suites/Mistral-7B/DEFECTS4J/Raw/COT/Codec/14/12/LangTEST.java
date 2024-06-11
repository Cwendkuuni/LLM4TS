package org.apache.commons.codec.language.bm;

import static org.apache.commons.codec.language.bm.Lang.NameType.EN;
import static org.apache.commons.codec.language.bm.Lang.NameType.ES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.Languages;
import org.apache.commons.codec.language.bm.Lang.Languages.LanguageSet;

/**
 * Test class for Lang class.
 *
 * @author John Doe
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)} method with EN NameType.
     */
    @org.junit.Test
    public void testInstanceEN() {
        Lang lang = Lang.instance(EN);
        LangRule rule1 = new LangRule(Pattern.compile("^[A-Z][a-z]*s?$"), new HashSet<String>(Arrays.asList("en")), true);
        assertTrue(lang.rules.contains(rule1));
    }

    /**
     * Test method for {@link Lang#instance(NameType)} method with ES NameType.
     */
    @org.junit.Test
    public void testInstanceES() {
        Lang lang = Lang.instance(ES);
        LangRule rule1 = new LangRule(Pattern.compile("^(Qu|que|que\\s+de|que\\s+de\\s+las|que\\s+de\\s+lo)\\s.*$"),
                new HashSet<String>(Arrays.asList("es")), true);
        assertTrue(lang.rules.contains(rule1));
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method with English language.
     */
    @org.junit.Test
    public void testGuessLanguageEnglish() {
        Lang lang = Lang.instance(EN);
        String input = "the quick brown fox jumps over the lazy dog";
        LanguageSet expected = new LanguageSet(Collections.singletonList("en"));
        LanguageSet actual = lang.guessLanguages(input);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method with Spanish language.
     */
    @org.junit.Test
    public void testGuessLanguageSpanish() {
        Lang lang = Lang.instance(ES);
        String input = "la rápida liebre saltar sobre el perro perezoso";
        LanguageSet expected = new LanguageSet(Collections.singletonList("es"));
        LanguageSet actual = lang.guessLanguages(input);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method with multiple languages.
     */
    @org.junit.Test
    public void testGuessLanguageMultipleLanguages() {
        Lang lang = Lang.instance(EN);
        String input = "the quick brown fox jumps over the lazy dog, pero el perro no quiere correr";
        LanguageSet expected = new LanguageSet(Arrays.asList("en", "es"));
        LanguageSet actual = lang.guessLanguages(input);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)} method with no match languages.
     */
    @org.junit.Test
    public void testGuessLanguageNoMatch() {
        Lang lang = Lang.instance(EN);
        String input = "你好，世界"; // Chinese characters
        LanguageSet expected = Languages.ANY;
        LanguageSet actual = lang.guessLanguages(input);
        assertEquals(expected, actual);
    }
}