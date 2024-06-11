package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceWithNullName() {
        Lang.loadFromResource(null, Languages.getInstance(NameType.ENGLISH));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceWithNullResourceName() {
        Lang.loadFromResource("", Languages.getInstance(NameType.ENGLISH));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceWithNullLanguages() {
        Lang.loadFromResource(LANGUAGE_RULES_RN, null);
    }

    @Test
    public void testGuessLanguageWithEmptyString() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertNull(englishLang.guessLanguage(""));
    }

    @Test
    public void testGuessLanguageWithSingleEnglishWord() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("apple"));
    }

    @Test
    public void testGuessLanguageWithSingleMixedWord() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("apfel"));
    }

    @Test
    public void testGuessLanguageWithMixedWords() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("apple pie"));
    }

    @Test
    public void testGuessLanguageWithNoMatch() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals(Lang.Languages.ANY, englishLang.guessLanguage("مرحبا"));
    }

    @Test
    public void testGuessLanguagesWithEmptyString() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals(Collections.emptyList(), englishLang.guessLanguages(""));
    }

    @Test
    public void testGuessLanguagesWithSingleEnglishWord() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        Set<String> expected = EnumSet.of(NameType.ENGLISH.toString());
        assertEquals(expected, englishLang.guessLanguages("apple"));
    }

    @Test
    public void testGuessLanguagesWithSingleMixedWord() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        Set<String> expected = EnumSet.of(NameType.ENGLISH.toString(), NameType.GERMAN.toString());
        assertEquals(expected, englishLang.guessLanguages("apfel"));
    }

    @Test
    public void testGuessLanguagesWithMixedWords() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        Set<String> expected = EnumSet.of(NameType.ENGLISH.toString(), NameType.GERMAN.toString());
        assertEquals(expected, englishLang.guessLanguages("apple pie"));
    }

    @Test
    public void testGuessLanguagesWithNoMatch() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        Set<String> expected = Collections.singletonList(Lang.Languages.ANY.toString());
        assertEquals(expected, englishLang.guessLanguages("مرحبا"));
    }

    @Test
    public void testGuessLanguagesWithMultipleMatches() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        Set<String> expected = EnumSet.of(NameType.ENGLISH.toString(), NameType.GERMAN.toString(), NameType.FRENCH.toString());
        assertEquals(expected, englishLang.guessLanguages("appfel pie"));
    }

    private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";

    @Test
    public void testLoadFromResourceWithValidData() {
        Lang englishLang = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.ENGLISH));
        assertNotNull(englishLang);
    }

    @Test
    public void testLoadFromResourceWithEmptyFile() {
        Lang englishLang = null;
        try {
            englishLang = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalStateException e) {
            // expected
        }
        assertNull(englishLang);
    }

    @Test
    public void testLoadFromResourceWithInvalidFileFormat() {
        Lang englishLang = null;
        try {
            englishLang = Lang.loadFromResource("invalidFormat.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(englishLang);
    }

    @Test
    public void testLoadFromResourceWithInvalidLanguageRule() {
        Lang englishLang = null;
        try {
            englishLang = Lang.loadFromResource("lang.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(englishLang);
    }

    @Test
    public void testLoadFromResourceWithInvalidLanguageRuleAcceptOnMatch() {
        Lang englishLang = null;
        try {
            englishLang = Lang.loadFromResource("acceptOnMatch.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(englishLang);
    }

    @Test
    public void testLoadFromResourceWithInvalidLanguageRuleLanguages() {
        Lang englishLang = null;
        try {
            englishLang = Lang.loadFromResource("languages.txt", Languages.getInstance(NameType.ENGLISH));
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertNull(englishLang);
    }
}