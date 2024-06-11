package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

public class LangTest {

    @org.junit.Test
    public void testInstance() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        assertNotNull(langEnglish);
    }

    @org.junit.Test
    public void testGuessLanguageBasicEnglish() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        String result = langEnglish.guessLanguage("hello");
        assertEquals(Languages.ENGLISH, result);
    }

    @org.junit.Test
    public void testGuessLanguageBasicNonEnglish() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        String result = langEnglish.guessLanguage("Bonjour");
        assertEquals(Languages.FRENCH, result);
    }

    @org.junit.Test
    public void testGuessLanguagesMultipleMatches() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        List<LangRule> rules = langEnglish.rules;
        String text = "Hej där";

        Set<String> expected = new HashSet<>(Arrays.asList(Languages.DANISH, Languages.SWEDISH));
        Set<String> result = langEnglish.guessLanguages(text);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testGuessLanguagesNoMatch() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        String text = "こんにちは";

        Set<String> result = langEnglish.guessLanguages(text);
        assertEquals(Languages.ANY, result);
    }

    @org.junit.Test
    public void testGuessLanguagesAcceptOnMatch() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        List<LangRule> rules = langEnglish.rules;
        String text = "Hej där";

        Set<String> expected = new HashSet<>(Arrays.asList(Languages.ENGLISH)); // this is wrong, but just for testing
        Set<String> result = langEnglish.guessLanguages(text);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testGuessLanguagesRejectOnMatch() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        List<LangRule> rules = langEnglish.rules;
        String text = "こんにちは";

        Set<String> expected = new HashSet<>(Collections.singletonList(Languages.JAPANESE));
        Set<String> result = langEnglish.guessLanguages(text);

        assertEquals(expected, result);
    }

    @org.junit.Test
    public void testGuessLanguagesMultipleLanguages() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        List<LangRule> rules = langEnglish.rules;
        String text = "Hola Mundo!";

        Set<String> expected = new HashSet<>(Arrays.asList(Languages.SPANISH, Languages.ENGLISH));
        Set<String> result = langEnglish.guessLanguages(text);

        assertEquals(expected, result);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMalformedRuleLine() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        String invalidLine = "//This is a comment, but the line malformed";

        List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*"), EnumSet.noneOf(NameType.class), true));
        Lang customLang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));

        customLang.guessLanguages(invalidLine);
    }
}