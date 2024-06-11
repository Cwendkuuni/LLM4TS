package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

/**
 * Test class for Lang.
 */
public class LangTest {

    /**
     * Test method for {@link Lang#instance(NameType)}.
     */
    @Test
    public void testInstance() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final Lang german = Lang.instance(NameType.GERMAN);
        assertEquals(NameType.ENGLISH, english.getNameType());
        assertEquals(NameType.GERMAN, german.getNameType());
    }

    /**
     * Test method for {@link Lang#guessLanguage(String)}.
     */
    @Test
    public void testGuessLanguage() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final String englishText = "hello world";
        assertEquals(NameType.ENGLISH, english.guessLanguage(englishText));

        final Lang german = Lang.instance(NameType.GERMAN);
        final String germanText = "hallo welt";
        assertEquals(NameType.GERMAN, german.guessLanguage(germanText));

        final Lang any = Lang.instance(NameType.ANY);
        final String anyText = "こんにちは"; // Japanese "Hello"
        assertEquals(NameType.ANY, any.guessLanguage(anyText));
    }

    /**
     * Test method for {@link Lang#guessLanguages(String)}.
     */
    @Test
    public void testGuessLanguages() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        final String englishText = "hello world";
        final Set<NameType> expectedEngLanguages = EnumSet.of(NameType.ENGLISH, NameType.CHINESE, NameType.DUTCH);
        final Set<NameType> actualEngLanguages = english.guessLanguages(englishText);
        assertTrue(expectedEngLanguages.equals(actualEngLanguages) || actualEngLanguages.containsAll(expectedEngLanguages));

        final Lang german = Lang.instance(NameType.GERMAN);
        final String germanText = "hallo welt";
        final Set<NameType> expectedGermanLanguages = EnumSet.of(NameType.GERMAN, NameType.ENGLISH, NameType.DUTCH);
        final Set<NameType> actualGermanLanguages = german.guessLanguages(germanText);
        assertTrue(expectedGermanLanguages.equals(actualGermanLanguages) || actualGermanLanguages.containsAll(expectedGermanLanguages));

        final Lang any = Lang.instance(NameType.ANY);
        final String japaneseText = "こんにちは"; // Japanese "Hello"
        final Set<NameType> expectedAnyLanguages = Arrays.asList(NameType.CHINESE, NameType.DUTCH, NameType.ENGLISH, NameType.GERMAN, NameType.JAPANESE).asSubList(0, 3);
        final Set<NameType> actualAnyLanguages = any.guessLanguages(japaneseText);
        assertTrue(expectedAnyLanguages.equals(actualAnyLanguages) || actualAnyLanguages.containsAll(expectedAnyLanguages));

        final Lang japanese = Lang.instance(NameType.JAPANESE);
        final String japaneseText = "こんにちは"; // Japanese "Hello"
        assertEquals(NameType.JAPANESE, japanese.guessLanguage(japaneseText));
        final Set<NameType> expectedJapaneseLanguages = EnumSet.of(NameType.JAPANESE);
        final Set<NameType> actualJapaneseLanguages = japanese.guessLanguages(japaneseText);
        assertTrue(expectedJapaneseLanguages.equals(actualJapaneseLanguages) || actualJapaneseLanguages.containsAll(expectedJapaneseLanguages));

        final Lang empty = Lang.instance(NameType.EMPTY);
        final Set<NameType> expectedEmptyLanguages = Collections.emptySet();
        final Set<NameType> actualEmptyLanguages = empty.guessLanguages("test");
        assertEquals(expectedEmptyLanguages, actualEmptyLanguages);
    }

    /**
     * Test method for {@link LangRule#matches(String)}.
     */
    @Test
    public void testLangRuleMatches() {
        final LangRule rule = new LangRule(Pattern.compile("^hallo"), EnumSet.of(NameType.GERMAN), true);
        final String germanText = "hallo welt";
        assertTrue(rule.matches(germanText));

        final String englishText = "hello world";
        assertFalse(rule.matches(englishText));
    }
}