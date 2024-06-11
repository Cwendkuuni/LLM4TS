package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.DE);
        assertEquals(Lang.class, lang.getClass());
        assertEquals(NameType.DE, lang.getNameType());
    }

    @Test
    public void testLoadFromResource() {
        Languages languages = Languages.getInstance(NameType.DE);
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_de.txt", languages);
        assertEquals(NameType.DE, lang.getNameType());
        assertEquals(languages, lang.getLanguages());

        List<LangRule> rules = lang.getRules();
        assertEquals(3, rules.size());

        LangRule rule1 = rules.get(0);
        assertEquals("(?i)\\bschönes\\b", rule1.getPattern().pattern());
        Set<String> languages1 = rule1.getLanguages();
        assertEquals(1, languages1.size());
        assertEquals("German", languages1.iterator().next());
        assertTrue(rule1.acceptOnMatch());

        LangRule rule2 = rules.get(1);
        assertEquals("(?i)\\babend\\b", rule2.getPattern().pattern());
        Set<String> languages2 = rule2.getLanguages();
        assertEquals(1, languages2.size());
        assertEquals("German", languages2.iterator().next());
        assertFalse(rule2.acceptOnMatch());

        LangRule rule3 = rules.get(2);
        assertEquals("(?i)([aeiou]|[bcdfghjklmnpqrstvwz]){2,}", rule3.getPattern().pattern());
        Set<String> languages3 = rule3.getLanguages();
        assertEquals(3, languages3.size());
        assertArrayEquals(new String[]{"German", "Dutch", "English"}, languages3.toArray(new String[0]));
        assertTrue(rule3.acceptOnMatch());
    }

    @Test
    public void testGuessLanguage() {
        Languages languages = Languages.getInstance(NameType.DE);
        Lang lang = Lang.instance(NameType.DE);

        String input1 = "Schönes Wetter";
        String expectedOutput1 = "German";
        String output1 = lang.guessLanguage(input1);
        assertEquals(expectedOutput1, output1);

        String input2 = "abend";
        String expectedOutput2 = null;
        String output2 = lang.guessLanguage(input2);
        assertNull(output2);

        String input3 = "apfelstrudel";
        String expectedOutput3 = "German";
        String output3 = lang.guessLanguage(input3);
        assertEquals(expectedOutput3, output3);

        String input4 = "apple pie";
        String expectedOutput4 = Languages.ANY;
        String output4 = lang.guessLanguage(input4);
        assertEquals(expectedOutput4, output4);

        String input5 = "morgen";
        String expectedOutput5 = "German";
        String output5 = lang.guessLanguage(input5);
        assertEquals(expectedOutput5, output5);
    }

    @Test
    public void testGuessLanguages() {
        Languages languages = Languages.getInstance(NameType.DE);
        Lang lang = Lang.instance(NameType.DE);

        String input1 = "Schönes Wetter";
        Set<String> expectedOutput1 = Collections.singleton("German");
        Set<String> output1 = lang.guessLanguages(input1);
        assertEquals(expectedOutput1, output1);

        String input2 = "abend";
        Set<String> expectedOutput2 = Collections.emptySet();
        Set<String> output2 = lang.guessLanguages(input2);
        assertEquals(expectedOutput2, output2);

        String input3 = "apfelstrudel";
        Set<String> expectedOutput3 = Collections.singleton("German");
        Set<String> output3 = lang.guessLanguages(input3);
        assertEquals(expectedOutput3, output3);

        String input4 = "apple pie";
        Set<String> expectedOutput4 = EnumSet.allOf(Languages.Language.class);
        Set<String> output4 = lang.guessLanguages(input4);
        assertEquals(expectedOutput4, output4);

        String input5 = "morgen";
        Set<String> expectedOutput5 = Collections.singleton("German");
        Set<String> output5 = lang.guessLanguages(input5);
        assertEquals(expectedOutput5, output5);
    }

    @Test
    public void testGetNameType() {
        Lang lang = Lang.instance(NameType.DE);
        assertEquals(NameType.DE, lang.getNameType());
    }

    @Test(expected = IllegalStateException.class)
    public void testInstanceNullNameType() {
        Lang.instance(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceNullLanguageRulesResourceName() {
        Lang.loadFromResource(null, Languages.getInstance(NameType.DE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceNullLanguages() {
        Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_de.txt", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGuessLanguageNullText() {
        Lang lang = Lang.instance(NameType.DE);
        assertNull(lang.guessLanguage(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGuessLanguagesNullText() {
        Lang lang = Lang.instance(NameType.DE);
        assertNull(lang.guessLanguages(null));
    }
}