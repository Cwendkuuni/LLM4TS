package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String englishWord = "hello";
        assertEquals(Languages.ENGLISH, englishLang.guessLanguage(englishWord));

        final Lang frenchLang = Lang.instance(NameType.FRENCH);
        final String frenchWord = "bonjour";
        assertEquals(Languages.FRENCH, frenchLang.guessLanguage(frenchWord));

        final Lang anyLang = Lang.instance(NameType.ANY);
        final String anyWord = "hola";
        assertEquals(Languages.ANY, anyLang.guessLanguage(anyWord));

        // Test for unknown words
        final String unknownWord = "unknown";
        final Lang unknownLang = Lang.instance(NameType.UNKNOWN);
        assertEquals(Languages.ANY, unknownLang.guessLanguage(unknownWord));
    }

    @Test
    public void testGuessLanguages() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final String englishWord = "hello";

        final Set<String> expected = EnumSet.of(Languages.ENGLISH.getName());
        final Set<String> actual = englishLang.guessLanguages(englishWord);
        assertEquals(expected, actual);

        final Lang frenchLang = Lang.instance(NameType.FRENCH);
        final String frenchWord = "bonjour";

        final Set<String> frenchExpected = EnumSet.of(Languages.FRENCH.getName(), Languages.FRENCH_SWISS.getName());
        final Set<String> frenchActual = frenchLang.guessLanguages(frenchWord);
        assertEquals(frenchExpected, frenchActual);

        final Lang anyLang = Lang.instance(NameType.ANY);
        final String anyWord = "hola";

        final Set<String> anyExpected = Collections.emptySet();
        final Set<String> anyActual = anyLang.guessLanguages(anyWord);
        assertEquals(anyExpected, anyActual);

        // Test for unknown words
        final Lang unknownLang = Lang.instance(NameType.UNKNOWN);
        final String unknownWord = "unknown";

        final Set<String> unknownExpected = Collections.emptySet();
        final Set<String> unknownActual = unknownLang.guessLanguages(unknownWord);
        assertEquals(unknownExpected, unknownActual);
    }

    @Test
    public void testLoadFromResource() {
        final Lang englishLang = Lang.instance(NameType.ENGLISH);
        final List<LangRule> rules = englishLang.getRules();

        final int expectedRuleCount = 5;
        assertEquals(expectedRuleCount, rules.size());

        final LangRule rule1 = rules.get(0);
        final Pattern pattern1 = rule1.getPattern();
        final Set<String> languages1 = rule1.getLanguages();
        final boolean acceptOnMatch1 = rule1.isAcceptOnMatch();

        final String input1 = "foo bar baz";
        assertTrue(rule1.matches(input1));
        assertTrue(languages1.contains(Languages.ENGLISH.getName()));
        assertFalse(languages1.contains(Languages.FRENCH.getName()));
        assertFalse(languages1.contains(Languages.GERMAN.getName()));
        assertFalse(acceptOnMatch1);

        final LangRule rule2 = rules.get(1);
        final Pattern pattern2 = rule2.getPattern();
        final Set<String> languages2 = rule2.getLanguages();
        final boolean acceptOnMatch2 = rule2.isAcceptOnMatch();

        final String input2 = "hello world";
        assertTrue(rule2.matches(input2));
        assertTrue(languages2.contains(Languages.ENGLISH.getName()));
        assertFalse(languages2.contains(Languages.FRENCH.getName()));
        assertFalse(languages2.contains(Languages.GERMAN.getName()));
        assertTrue(acceptOnMatch2);

        final LangRule rule3 = rules.get(2);
        final Pattern pattern3 = rule3.getPattern();
        final Set<String> languages3 = rule3.getLanguages();
        final boolean acceptOnMatch3 = rule3.isAcceptOnMatch();

        final String input3 = "foo bar baz foo";
        assertTrue(rule3.matches(input3));
        assertTrue(languages3.contains(Languages.ENGLISH.getName()));
        assertFalse(languages3.contains(Languages.FRENCH.getName()));
        assertFalse(languages3.contains(Languages.GERMAN.getName()));
        assertTrue(acceptOnMatch3);

        final LangRule rule4 = rules.get(3);
        final Pattern pattern4 = rule4.getPattern();
        final Set<String> languages4 = rule4.getLanguages();
        final boolean acceptOnMatch4 = rule4.isAcceptOnMatch();

        final String input4 = "maman papa fille";
        assertTrue(rule4.matches(input4));
        assertTrue(languages4.contains(Languages.FRENCH.getName()));
        assertFalse(languages4.contains(Languages.ENGLISH.getName()));
        assertFalse(languages4.contains(Languages.GERMAN.getName()));
        assertTrue(acceptOnMatch4);

        final LangRule rule5 = rules.get(4);
        final Pattern pattern5 = rule5.getPattern();
        final Set<String> languages5 = rule5.getLanguages();
        final boolean acceptOnMatch5 = rule5.isAcceptOnMatch();

        final String input5 = "hallo der Welt";
        assertTrue(rule5.matches(input5));
        assertTrue(languages5.contains(Languages.GERMAN.getName()));
        assertFalse(languages5.contains(Languages.ENGLISH.getName()));
        assertFalse(languages5.contains(Languages.FRENCH.getName()));
        assertTrue(acceptOnMatch5);
    }
}