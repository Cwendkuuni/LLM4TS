package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LangTest {

    private static final Lang ENGLISH_LANGUAGE_RULES = Lang.instance(NameType.ENGLISH);

    @Test
    public void testEnglishGuessLanguage() {
        // Test some simple cases
        assertEquals("en", ENGLISH_LANGUAGE_RULES.guessLanguage("hello"));
        assertEquals("en", ENGLISH_LANGUAGE_RULES.guessLanguage("world"));
        assertEquals("en", ENGLISH_LANGUAGE_RULES.guessLanguage("hello world"));

        // Test some edge cases
        assertEquals("en", ENGLISH_LANGUAGE_RULES.guessLanguage("Hello World!"));
        assertEquals("en", ENGLISH_LANGUAGE_RULES.guessLanguage("HeLLo wOrLd!"));
        assertEquals("en", ENGLISH_LANGUAGE_RULES.guessLanguage("He!llo wOrLd!"));

        // Test some cases where the language should not be guessed
        assertFalse(ENGLISH_LANGUAGE_RULES.guessLanguage("hola").equals("es"));
        assertFalse(ENGLISH_LANGUAGE_RULES.guessLanguage("bonjour").equals("fr"));
        assertFalse(ENGLISH_LANGUAGE_RULES.guessLanguage("guten tag").equals("de"));

        // Test some cases where the language should be guessed even if there are other languages that match
        assertEquals("en", ENGLISH_LANGUAGE_RULES.guessLanguage("hello there"));
        assertEquals("en", ENGLISH_LANGUAGE_RULES.guessLanguage("hello how are you"));
        assertEquals("en", ENGLISH_LANGUAGE_RULES.guessLanguage("hello i am fine"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidResource() {
        Lang.loadFromResource("", Languages.getInstance(NameType.ENGLISH));
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidResourceReference() {
        Lang.loadFromResource("invalid:resource", Languages.getInstance(NameType.ENGLISH));
    }

    @Test
    public void testEmptyResource() {
        final List<LangRule> rules = Lang.loadFromResource("org/apache/commons/codec/language/bm/empty.txt", Languages.getInstance(NameType.ENGLISH)).getRules();
        assertEquals(0, rules.size());
    }

    @Test
    public void testSingleLanguageRule() {
        final List<LangRule> rules = Lang.loadFromResource("org/apache/commons/codec/language/bm/single.txt", Languages.getInstance(NameType.ENGLISH)).getRules();
        assertEquals(1, rules.size());
        final LangRule rule = rules.get(0);
        final String input = "example";
        final Set<String> expected = new HashSet<String>(Arrays.asList("de"));
        final Set<String> actual = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input));
        assertTrue(actual.equals(expected) || actual.equals(Collections.singleton(Languages.ANY)));
        assertTrue(rule.matches(input));
        assertTrue(rule.acceptOnMatch);
    }

    @Test
    public void testMultipleLanguageRules() {
        final List<LangRule> rules = Lang.loadFromResource("org/apache/commons/codec/language/bm/multiple.txt", Languages.getInstance(NameType.ENGLISH)).getRules();
        assertEquals(3, rules.size());

        final String input1 = "example1";
        final Set<String> expected1 = new HashSet<String>(Arrays.asList("de", "fr"));
        final Set<String> actual1 = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input1));
        assertTrue(actual1.equals(expected1) || actual1.equals(Collections.singleton(Languages.ANY)));

        final String input2 = "example2";
        final Set<String> expected2 = new HashSet<String>(Arrays.asList("en", "de"));
        final Set<String> actual2 = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input2));
        assertTrue(actual2.equals(expected2) || actual2.equals(Collections.singleton(Languages.ANY)));

        final String input3 = "example3";
        final Set<String> expected3 = new HashSet<String>(Arrays.asList("en", "fr"));
        final Set<String> actual3 = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input3));
        assertTrue(actual3.equals(expected3) || actual3.equals(Collections.singleton(Languages.ANY)));

        final String input4 = "example4";
        final Set<String> expected4 = new HashSet<String>(Arrays.asList("en"));
        final Set<String> actual4 = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input4));
        assertTrue(actual4.equals(expected4) || actual4.equals(Collections.singleton(Languages.ANY)));
    }

    @Test
    public void testAcceptOnMatch() {
        final List<LangRule> rules = Lang.loadFromResource("org/apache/commons/codec/language/bm/accept.txt", Languages.getInstance(NameType.ENGLISH)).getRules();
        assertEquals(1, rules.size());

        final String input = "example";
        final Set<String> expected = new HashSet<String>(Collections.singletonList("en"));
        final Set<String> actual = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input));
        assertTrue(actual.equals(expected) || actual.equals(Collections.singleton(Languages.ANY)));

        final LangRule rule = rules.get(0);
        assertTrue(rule.matches(input));
        assertTrue(rule.acceptOnMatch);

        final String input2 = "another example";
        final Set<String> expected2 = new HashSet<String>(Collections.emptySet());
        final Set<String> actual2 = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input2));
        assertTrue(actual2.equals(expected2) || actual2.equals(Collections.singleton(Languages.ANY)));
        assertFalse(rule.matches(input2));
    }

    @Test
    public void testRejectOnMatch() {
        final List<LangRule> rules = Lang.loadFromResource("org/apache/commons/codec/language/bm/reject.txt", Languages.getInstance(NameType.ENGLISH)).getRules();
        assertEquals(1, rules.size());

        final String input = "example";
        final Set<String> expected = new HashSet<String>(Collections.emptySet());
        final Set<String> actual = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input));
        assertTrue(actual.equals(expected) || actual.equals(Collections.singleton(Languages.ANY)));

        final LangRule rule = rules.get(0);
        assertTrue(rule.matches(input));
        assertFalse(rule.acceptOnMatch);

        final String input2 = "another example";
        final Set<String> expected2 = new HashSet<String>(Collections.singletonList("en"));
        final Set<String> actual2 = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input2));
        assertTrue(actual2.equals(expected2) || actual2.equals(Collections.singleton(Languages.ANY)));
        assertTrue(rule.matches(input2));
    }

    @Test
    public void testCaseInsensitive() {
        final List<LangRule> rules = Lang.loadFromResource("org/apache/commons/codec/language/bm/caseinsensitive.txt", Languages.getInstance(NameType.ENGLISH)).getRules();
        assertEquals(1, rules.size());

        final String input = "example";
        final Set<String> expected = new HashSet<String>(Arrays.asList("en"));
        final Set<String> actual = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input));
        assertTrue(actual.equals(expected) || actual.equals(Collections.singleton(Languages.ANY)));

        final LangRule rule = rules.get(0);
        final String inputLower = input.toLowerCase(Locale.ENGLISH);
        assertTrue(rule.matches(inputLower));
        assertTrue(rule.acceptOnMatch);

        final String input2 = "Another Example";
        final Set<String> expected2 = new HashSet<String>(Arrays.asList("en"));
        final Set<String> actual2 = new HashSet<String>(ENGLISH_LANGUAGE_RULES.guessLanguages(input2));
        assertTrue(actual2.equals(expected2) || actual2.equals(Collections.singleton(Languages.ANY)));
        assertTrue(rule.matches(input2));
    }
}