package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Langs;
import org.apache.commons.codec.language.bm.Languages;

public class LangTest {

    @Test
    public void testInstance() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        assertNotNull(langEnglish);
        assertEquals(NameType.ENGLISH, langEnglish.getNameType());
    }

    @Test
    public void testLoadFromResource() {
        Lang langEnglish = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.getInstance(NameType.ENGLISH));
        assertSame(langEnglish, Lang.instance(NameType.ENGLISH));
    }

    @Test
    public void testGuessLanguage() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);

        // test with English words
        assertEquals(NameType.ENGLISH, langEnglish.guessLanguage("hello"));
        assertEquals(NameType.ENGLISH, langEnglish.guessLanguage("world"));
        assertEquals(NameType.ENGLISH, langEnglish.guessLanguage("foo"));

        // test with non-English words
        assertEquals(Languages.CHINESE, langEnglish.guessLanguage("你好"));
        assertEquals(Languages.FRENCH, langEnglish.guessLanguage("salut"));
    }

    @Test
    public void testGuessLanguages() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);

        // test with English words
        Set<String> expectedEnglish = new HashSet<String>(Arrays.asList(NameType.ENGLISH.toString(), Languages.ENGLISH.toString()));
        Set<String> actualEnglish = langEnglish.guessLanguages("hello");
        assertEquals(expectedEnglish, actualEnglish);

        // test with non-English words
        Set<String> expectedMixed = new HashSet<String>(Arrays.asList(NameType.ENGLISH.toString(), Languages.CHINESE.toString(), Languages.FRENCH.toString()));
        Set<String> actualMixed = langEnglish.guessLanguages("你好 salut");
        assertEquals(expectedMixed, actualMixed);

        // test with empty string
        Set<String> expectedEmpty = Collections.emptySet();
        Set<String> actualEmpty = langEnglish.guessLanguages("");
        assertEquals(expectedEmpty, actualEmpty);
    }

    @Test
    public void testLoadFromResource_NoRules() {
        // test with an empty resource file
        Lang langEmpty = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_empty.txt", Languages.getInstance(NameType.ENGLISH));
        assertNotNull(langEmpty);
        assertEquals(0, langEmpty.rules.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResource_InvalidResource() {
        Lang langInvalid = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_invalid.txt", Languages.getInstance(NameType.ENGLISH));
        fail("Expected an IllegalStateException");
    }

    @Test
    public void testLangRule() {
        String ruleLine = "^[aeiou]{2,}|[bcdfghjklmnpqrstvwxyz]{2,}+$\\s+English\\s+true";
        Pattern pattern = Pattern.compile(ruleLine);
        Set<String> expectedLangs = new HashSet<String>(Arrays.asList(NameType.ENGLISH.toString(), Languages.ENGLISH.toString()));
        boolean acceptOnMatch = true;

        LangRule rule = new LangRule(pattern, expectedLangs, acceptOnMatch);

        // test matches
        assertTrue(rule.matches("hello"));
        assertTrue(rule.matches("world"));
        assertTrue(rule.matches("foo"));
        assertTrue(rule.matches("aa"));
        assertTrue(rule.matches("hellofoo"));

        // test acceptOnMatch
        Set<String> actualLangs = new HashSet<String>();
        for (LangRule r : Lang.instance(NameType.ENGLISH).rules) {
            if (r.matches("hello")) {
                actualLangs.addAll(r.languages);
                break;
            }
        }

        assertEquals(expectedLangs, actualLangs);
    }
}