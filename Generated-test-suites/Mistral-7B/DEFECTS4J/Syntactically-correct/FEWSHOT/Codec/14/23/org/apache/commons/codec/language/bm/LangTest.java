package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);

        assertEquals("en", lang.guessLanguage("hello"));
        assertEquals("en", lang.guessLanguage("hello world"));
        assertEquals("en", lang.guessLanguage("12345"));
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("foo"));
        assertEquals(Lang.Languages.ANY, lang.guessLanguage(""));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.ENGLISH);

        Set<String> expected = EnumSet.of("en", "fr", "de");
        Set<String> actual = lang.guessLanguages("hello world!");
        assertTrue(expected.containsAll(actual));
        assertEquals(3, actual.size());

        expected = Collections.emptySet();
        actual = lang.guessLanguages("foo");
        assertTrue(expected.isEmpty());
        assertEquals(0, actual.size());

        expected = EnumSet.of("en", "it");
        actual = lang.guessLanguages("one two three");
        assertTrue(expected.containsAll(actual));
        assertEquals(2, actual.size());

        expected = EnumSet.of("nl");
        actual = lang.guessLanguages("ijsgegrrrt");
        assertTrue(expected.containsAll(actual));
        assertEquals(1, actual.size());

        expected = Lang.Languages.ANY_LANGUAGE;
        actual = lang.guessLanguages("");
        assertEquals(expected, actual);
        assertEquals(1, actual.size());
    }

    @Test
    public void testLoadFromResource() {
        Lang english = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.getInstance(NameType.ENGLISH));
        assertNotNull(english);

        Lang french = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_fr.txt", Languages.getInstance(NameType.FRENCH));
        assertNotNull(french);

        Lang german = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_de.txt", Languages.getInstance(NameType.GERMAN));
        assertNotNull(german);

        List<String> englishRules = english.rules;
        List<String> frenchRules = french.rules;
        List<String> germanRules = german.rules;

        assertEquals(22, englishRules.size());
        assertEquals(18, frenchRules.size());
        assertEquals(20, germanRules.size());

        // Test some rules from each language file

        // English
        String englishPattern = "^[aeiou].*e$";
        String englishLang = "en";
        String englishAcceptOnMatch = "true";
        assertEquals(englishLang, getLanguageForRule(englishRules, englishPattern, englishAcceptOnMatch));

        englishPattern = "^[bcdfghjklmnpqrstvwxyz]$";
        englishLang = "en";
        englishAcceptOnMatch = "false";
        assertEquals(Lang.Languages.ANY, getLanguageForRule(englishRules, englishPattern, englishAcceptOnMatch));

        // French
        String frenchPattern = "^j|qu|que|la|le|les";
        String frenchLang = "fr";
        String frenchAcceptOnMatch = "true";
        assertEquals(frenchLang, getLanguageForRule(frenchRules, frenchPattern, frenchAcceptOnMatch));

        frenchPattern = "^[bcdfghjklmnpqrstvwxyz]$";
        frenchLang = "fr";
        frenchAcceptOnMatch = "false";
        assertEquals(Lang.Languages.ANY, getLanguageForRule(frenchRules, frenchPattern, frenchAcceptOnMatch));

        // German
        String germanPattern = "^[äöüß][bcdfghjklmnpqrstvwxyz]$";
        String germanLang = "de";
        String germanAcceptOnMatch = "true";
        assertEquals(germanLang, getLanguageForRule(germanRules, germanPattern, germanAcceptOnMatch));

        germanPattern = "^[aeiou].*e$";
        germanLang = Lang.Languages.ANY;
        germanAcceptOnMatch = "false";
        assertEquals(germanLang, getLanguageForRule(germanRules, germanPattern, germanAcceptOnMatch));
    }

    private String getLanguageForRule(List<String> rules, String pattern, String acceptOnMatch) {
        for (String rule : rules) {
            String[] parts = rule.split("\\s+");
            if (parts[0].equals(pattern)) {
                return parts[1];
            }
        }
        if (acceptOnMatch.equals("false")) {
            return Lang.Languages.ANY;
        }
        throw new AssertionError("No rule found for pattern: " + pattern);
    }
}