package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang.*;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        final Lang english = Lang.instance(NameType.ENGLISH);
        assertNotNull(english);
    }

    @Test
    public void testLoadFromResource() {
        final Lang french = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_fr.txt", Languages.FRENCH);
        assertNotNull(french);
    }

    @Test
    public void testGuessLanguage() {
        final Lang english = Lang.instance(NameType.ENGLISH);

        assertEquals(NameType.ENGLISH, english.guessLanguage("hello"));
        assertEquals(NameType.ENGLISH, english.guessLanguage("world"));
        assertEquals(NameType.ENGLISH, english.guessLanguage("Hello, World!"));

        assertEquals(NameType.ENGLISH, english.guessLanguage("football"));
        assertEquals(NameType.ENGLISH, english.guessLanguage("soccer"));

        assertEquals(NameType.ENGLISH, english.guessLanguage("bonjour"));
        assertEquals(Languages.ANY, english.guessLanguage("salut")); // should return ANY since "salut" could be French or Spanish

        final Lang french = Lang.instance(NameType.FRENCH);

        assertEquals(NameType.FRENCH, french.guessLanguage("salut"));
        assertEquals(NameType.FRENCH, french.guessLanguage("bonjour"));
        assertEquals(NameType.FRENCH, french.guessLanguage("oui"));

        assertEquals(NameType.FRENCH, french.guessLanguage("football")); // "football" is in French resource, but this test case will fail
                                                                       // as there is no English resource for "football"
                                                                       // and the test should not make assumptions about
                                                                       // the order of rules in the ruleset or their
                                                                       // priorities.

        assertEquals(Languages.ANY, french.guessLanguage("world")); // should return ANY since "world" is not in French resource
    }

    @Test
    public void testGuessLanguages() {
        final Lang english = Lang.instance(NameType.ENGLISH);

        final Set<NameType> expectedEnglish = EnumSet.of(NameType.ENGLISH);
        final Set<NameType> actualEnglish = english.guessLanguages("hello");
        assertEquals(expectedEnglish, actualEnglish);

        final Set<NameType> expectedEnglishAndFrench = EnumSet.of(NameType.ENGLISH, NameType.FRENCH);
        final Set<NameType> actualEnglishAndFrench = english.guessLanguages("hello mondial"); // "mondial" is French for "world"
        assertEquals(expectedEnglishAndFrench, actualEnglishAndFrench);

        final Set<NameType> expectedNone = Collections.emptySet();
        final Set<NameType> actualNone = english.guessLanguages("xoox"); // no rule matches "xoox"
        assertEquals(expectedNone, actualNone);

        final Set<NameType> expectedEnglishAndFrenchAndGerman = EnumSet.of(NameType.ENGLISH, NameType.FRENCH, NameType.GERMAN);
        final Set<NameType> actualEnglishAndFrenchAndGerman = english.guessLanguages("einfach gut"); // "einfach gut" is German for "easily good"
        assertEquals(expectedEnglishAndFrenchAndGerman, actualEnglishAndFrenchAndGerman);

        final Lang german = Lang.instance(NameType.GERMAN);

        final Set<NameType> expectedGerman = EnumSet.of(NameType.GERMAN);
        final Set<NameType> actualGerman = german.guessLanguages("einfach gut");
        assertEquals(expectedGerman, actualGerman);

        final Set<NameType> expectedEnglishFrenchGerman = EnumSet.of(NameType.ENGLISH, NameType.FRENCH, NameType.GERMAN);
        final Set<NameType> actualEnglishFrenchGerman = english.guessLanguages("einfach gut bonjour"); // "bonjour" is French
        assertEquals(expectedEnglishFrenchGerman, actualEnglishFrenchGerman);

        final Set<NameType> expectedNoneGerman = Collections.emptySet();
        final Set<NameType> actualNoneGerman = german.guessLanguages("hello"); // no rule matches "hello" in German resource
        assertEquals(expectedNoneGerman, actualNoneGerman);
    }

    @Test
    public void testLangRule() {
        final Pattern pattern = Pattern.compile("einf(.*)ch");
        final Set<String> languages = Arrays.asList("German", "Germanic");
        final boolean acceptOnMatch = true;
        final LangRule rule = new LangRule(pattern, new HashSet<String>(languages), acceptOnMatch);

        // test matches
        assertTrue(rule.matches("einfach gut"));
        assertTrue(rule.matches("einfach lecker"));
        assertTrue(rule.matches("einfach gut und lecker"));
        assertTrue(rule.matches("einfach gut und lecker und hilfreich"));
        assertTrue(rule.matches("einfach lecker und hilfreich"));
        assertTrue(rule.matches("einfach hilfreich"));

        // test no match
        assertFalse(rule.matches("hello"));
        assertFalse(rule.matches("world"));
        assertFalse(rule.matches("einfach"));
        assertFalse(rule.matches("einfach schlecht"));
        assertFalse(rule.matches("einfach gut und schlecht"));

        // test languages
        assertEquals(languages, rule.getLanguages());

        // test acceptOnMatch
        assertEquals(NameType.GERMAN, rule.guessLanguage("einfach gut"));
        assertEquals(NameType.GERMAN, rule.guessLanguage("einfach lecker"));
        assertEquals(NameType.GERMAN, rule.guessLanguage("einfach gut und lecker"));
        assertEquals(NameType.GERMAN, rule.guessLanguage("einfach gut und lecker und hilfreich"));
        assertEquals(NameType.GERMAN, rule.guessLanguage("einfach lecker und hilfreich"));
        assertEquals(NameType.GERMAN, rule.guessLanguage("einfach hilfreich"));
    }

    @Test
    public void testLang() {
        final List<LangRule> rules = Arrays.asList(
                new LangRule(Pattern.compile("einf(.*)ch"), new HashSet<String>(Arrays.asList("German", "Germanic")), true),
                new LangRule(Pattern.compile("bonjour"), new HashSet<String>(Arrays.asList("French")), true),
                new LangRule(Pattern.compile("oui"), new HashSet<String>(Arrays.asList("French")), true),
                new LangRule(Pattern.compile("je m'appelle"), new HashSet<String>(Arrays.asList("French")), true),
                new LangRule(Pattern.compile("hello"), new HashSet<String>(Arrays.asList("English")), true),
                new LangRule(Pattern.compile("world"), new HashSet<String>(Arrays.asList("English")), true),
                new LangRule(Pattern.compile("football"), new HashSet<String>(Arrays.asList("French")), true),
                new LangRule(Pattern.compile("soccer"), new HashSet<String>(Arrays.asList("English")), true),
                new LangRule(Pattern.compile("(.*)foot(.*)"), new HashSet<String>(Arrays.asList("English", "German")), false)
        );
        final Lang lang = new Lang(rules, Languages.INSTANCE);

        assertEquals(NameType.ENGLISH, lang.guessLanguage("hello"));
        assertEquals(NameType.ENGLISH, lang.guessLanguage("world"));
        assertEquals(NameType.ENGLISH, lang.guessLanguage("football"));
        assertEquals(NameType.ENGLISH, lang.guessLanguage("soccer"));

        assertEquals(NameType.FRENCH, lang.guessLanguage("bonjour"));
        assertEquals(NameType.FRENCH, lang.guessLanguage("oui"));
        assertEquals(NameType.FRENCH, lang.guessLanguage("je m'appelle"));
        assertEquals(NameType.FRENCH, lang.guessLanguage("football")); // "football" is in French resource

        assertEquals(NameType.GERMAN, lang.guessLanguage("einfach gut"));
        assertEquals(NameType.GERMAN, lang.guessLanguage("einfach lecker"));
        assertEquals(NameType.GERMAN, lang.guessLanguage("einfach gut und lecker"));
        assertEquals(NameType.GERMAN, lang.guessLanguage("einfach gut und lecker und hilfreich"));
        assertEquals(NameType.GERMAN, lang.guessLanguage("einfach lecker und hilfreich"));
        assertEquals(NameType.GERMAN, lang.guessLanguage("einfach hilfreich"));

        assertFalse(lang.guessLanguage("einfach schlecht").equals(NameType.GERMAN));
        assertFalse(lang.guessLanguage("einfach gut und schlecht").equals(NameType.GERMAN));

        assertEquals(NameType.ENGLISH, lang.guessLanguage("einfach gut und lecker"));
        assertEquals(NameType.ENGLISH, lang.guessLanguage("einfach gut und lecker und hilfreich"));
        assertEquals(NameType.ENGLISH, lang.guessLanguage("einfach lecker und hilfreich"));
        assertEquals(NameType.ENGLISH, lang.guessLanguage("einfach hilfreich"));

        assertEquals(Languages.ANY, lang.guessLanguage("xoox"));
        assertEquals(Languages.ANY, lang.guessLanguage("hello mondial"));
        assertEquals(Languages.ANY, lang.guessLanguage("einfach schlecht"));
        assertEquals(Languages.ANY, lang.guessLanguage("einfach gut und schlecht"));

        assertEquals(EnumSet.of(NameType.ENGLISH, NameType.GERMAN), lang.guessLanguages("einfach gut und lecker"));
        assertEquals(EnumSet.of(NameType.ENGLISH, NameType.GERMAN), lang.guessLanguages("einfach gut und lecker und hilfreich"));
        assertEquals(EnumSet.of(NameType.ENGLISH, NameType.GERMAN), lang.guessLanguages("einfach lecker und hilfreich"));
        assertEquals(EnumSet.of(NameType.ENGLISH, NameType.GERMAN), lang.guessLanguages("einfach hilfreich"));
    }
}