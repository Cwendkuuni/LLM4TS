package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

public class LangTest {
    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.SIMPLE);
        assertNotNull(lang);
    }

    @Test
    public void testLangRule() {
        Pattern pattern = Pattern.compile("^[a-z]+( [a-z]+){2,}$");
        Set<String> languages = new HashSet<>(Arrays.asList("en", "fr", "de"));
        LangRule rule = new LangRule(pattern, languages, true);
        assertTrue(rule.matches("hello world"));
        assertTrue(rule.matches("john doe"));
        assertFalse(rule.matches("12345"));
        assertFalse(rule.matches("hello world!"));
        assertEquals(Collections.singletonList("en"), rule.languages.toList(Collections::emptyList));
    }

    @Test
    public void testLoadFromResource() {
        Languages languages = Languages.getInstance(NameType.SIMPLE);

        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_simple.txt", languages);
        assertNotNull(lang);
        assertEquals(10, lang.rules.size());

        LangRule rule1 = lang.rules.get(0);
        Pattern pattern1 = rule1.pattern;
        Set<String> languages1 = rule1.languages;
        assertTrue(pattern1.matcher("hello").matches());
        assertTrue(languages1.containsAll(Arrays.asList("en", "de")));

        LangRule rule2 = lang.rules.get(1);
        Pattern pattern2 = rule2.pattern;
        Set<String> languages2 = rule2.languages;
        assertFalse(pattern2.matcher("hello").matches());
        assertTrue(languages2.containsAll(Arrays.asList("fr", "es")));
    }

    @Test
    public void testGuessLanguage() {
        Languages languages = Languages.getInstance(NameType.SIMPLE);
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_simple.txt", languages);

        String text1 = "hello world";
        String guessedLanguage1 = lang.guessLanguage(text1);
        assertEquals("en", guessedLanguage1);

        String text2 = "bonjour le monde";
        String guessedLanguage2 = lang.guessLanguage(text2);
        assertEquals("fr", guessedLanguage2);

        String text3 = "hallo die Welt";
        String guessedLanguage3 = lang.guessLanguage(text3);
        assertEquals("de", guessedLanguage3);

        String text4 = "12345";
        String guessedLanguage4 = lang.guessLanguage(text4);
        assertEquals(Languages.ANY, guessedLanguage4);

        String text5 = "hello world!";
        String guessedLanguage5 = lang.guessLanguage(text5);
        assertEquals(Languages.ANY, guessedLanguage5);
    }

    @Test
    public void testGuessLanguages() {
        Languages languages = Languages.getInstance(NameType.SIMPLE);
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_simple.txt", languages);

        String text1 = "hello world";
        Languages.LanguageSet languages1 = lang.guessLanguages(text1);
        List<String> languagesList1 = languages1.toList(Collections::emptyList);
        assertTrue(languagesList1.containsAll(Arrays.asList("en", "de")));

        String text2 = "bonjour le monde";
        Languages.LanguageSet languages2 = lang.guessLanguages(text2);
        List<String> languagesList2 = languages2.toList(Collections::emptyList);
        assertTrue(languagesList2.containsAll(Arrays.asList("fr", "es")));

        String text3 = "12345";
        Languages.LanguageSet languages3 = lang.guessLanguages(text3);
        List<String> languagesList3 = languages3.toList(Collections::emptyList);
        assertTrue(languagesList3.isEmpty());

        String text4 = "hello world!";
        Languages.LanguageSet languages4 = lang.guessLanguages(text4);
        List<String> languagesList4 = languages4.toList(Collections::emptyList);
        assertTrue(languagesList4.isEmpty());
    }

    @Test
    public void testGuessLanguages_NoMatch() {
        Languages languages = Languages.getInstance(NameType.SIMPLE);
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_simple.txt", languages);

        String text = "XXXXXXXXXXX";
        Languages.LanguageSet languagesSet = lang.guessLanguages(text);
        assertTrue(languagesSet.isEmpty());
    }
}