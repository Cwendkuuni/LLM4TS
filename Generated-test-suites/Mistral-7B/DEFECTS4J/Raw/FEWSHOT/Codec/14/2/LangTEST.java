package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    @Test
    public void testLoadFromResource() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        List<LangRule> rules = englishLang.getRules();
        assertEquals(42, rules.size());

        LangRule rule1 = rules.get(0);
        Pattern pattern1 = rule1.getPattern();
        Set<String> languages1 = rule1.getLanguages();
        assertTrue(languages1.contains("en"));
        assertTrue(languages1.contains("de"));
        assertEquals("true", rule1.isAcceptOnMatch());

        LangRule rule41 = rules.get(41);
        Pattern pattern41 = rule41.getPattern();
        Set<String> languages41 = rule41.getLanguages();
        assertTrue(languages41.contains("af"));
        assertTrue(languages41.contains("am"));
        assertEquals("false", rule41.isAcceptOnMatch());
    }

    @Test
    public void testGuessLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        String text1 = "this is a test";
        String lang1 = englishLang.guessLanguage(text1);
        assertEquals("en", lang1);

        String text2 = "Hallo Welt";
        String lang2 = englishLang.guessLanguage(text2);
        assertEquals("de", lang2);

        String text3 = "こんにちは";
        String lang3 = englishLang.guessLanguage(text3);
        assertEquals(Languages.ANY, lang3);
    }

    @Test
    public void testGuessLanguages() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        String text1 = "this is a test";
        Languages.LanguageSet languages1 = englishLang.guessLanguages(text1);
        List<String> languagesList1 = languages1.getLanguages();
        assertTrue(languagesList1.contains("en"));
        assertTrue(languagesList1.contains("de"));
        assertFalse(languagesList1.contains("fr"));

        String text2 = "Hallo Welt";
        Languages.LanguageSet languages2 = englishLang.guessLanguages(text2);
        List<String> languagesList2 = languages2.getLanguages();
        assertTrue(languagesList2.contains("de"));
        assertFalse(languagesList2.contains("en"));

        String text3 = "こんにちは";
        Languages.LanguageSet languages3 = englishLang.guessLanguages(text3);
        List<String> languagesList3 = languages3.getLanguages();
        assertTrue(languagesList3.isEmpty());
    }

    @Test
    public void testGuessLanguagesForEmptyString() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        Languages.LanguageSet languages = englishLang.guessLanguages("");
        assertEquals(Languages.ANY, languages);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRuleLineFormat() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_invalid.txt";
        Lang invalidLang = Lang.loadFromResource(languageRulesResourceName, englishLang.getLanguages());
    }
}