package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        assertNotNull(langEnglish);
    }

    @Test
    public void testLoadFromResource() {
        Lang englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.ENGLISH);
        assertEquals(englishLang, Lang.instance(NameType.ENGLISH));
    }

    @Test
    public void testLoadFromResourceNotFound() {
        try {
            Lang.loadFromResource("org/apache/commons/codec/language/bm/nonexistent-lang.txt", Languages.ENGLISH);
            assertFalse("Expected an IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGuessLanguage() {
        Lang englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.ENGLISH);

        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("hello"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("hello world"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("how are you"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("the quick brown fox jumps over the lazy dog"));

        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("hola")); // Spanish word in English context
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("bonjour")); // French word in English context
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("guten tag")); // German word in English context

        assertEquals(Languages.ANY, englishLang.guessLanguage(""));
        assertEquals(Languages.ANY, englishLang.guessLanguage(" "));
        assertEquals(Languages.ANY, englishLang.guessLanguage("1234"));
        assertEquals(Languages.ANY, englishLang.guessLanguage("abcdef"));
    }

    @Test
    public void testGuessLanguages() {
        Lang englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.ENGLISH);

        Set<String> expectedLanguages = new HashSet<String>(Arrays.asList(
                "aa", "abkhaz", "af", "ak", "am", "an", "ar", "as", "be", "bg", "bn", "br", "bs", "ca", "ceb",
                "cs", "cy", "da", "de", "dv", "el", "en", "eo", "es", "et", "eu", "fa", "fi", "fo", "fr", "ga",
                "gd", "gl", "gu", "gv", "ha", "he", "hi", "hr", "hu", "hy", "id", "is", "it", "ja", "jv", "ka",
                "kk", "kl", "km", "kn", "ko", "ks", "ku", "ky", "la", "lb", "lg", "li", "lt", "lv", "mg", "mi",
                "mk", "ml", "mn", "mr", "ms", "mt", "my", "ne", "nl", "no", "ny", "oc", "pa", "pl", "pms", "pt",
                "qu", "ro", "ru", "sa", "sc", "sd", "si", "sk", "sl", "sm", "sn", "so", "sq", "sr", "ss", "st",
                "su", "sv", "sw", "ta", "te", "tg", "th", "ti", "tk", "to", "tr", "ts", "tt", "tzm", "tw", "ug",
                "uk", "ur", "uz", "ve", "vi", "vo", "wa", "wo", "xh", "yi", "yo", "zh", "zu"));

        Languages.LanguageSet ls = englishLang.guessLanguages("hello");
        assertEquals(Languages.ENGLISH, ls.getAny());
        assertTrue(ls.containsLanguage(NameType.ENGLISH));
        assertTrue(ls.containsLanguage("aa"));
        assertTrue(ls.containsLanguage("abkhaz"));
        // ... other languages
        assertTrue(ls.containsLanguage("zh"));
        assertTrue(ls.containsLanguage("zu"));

        Languages.LanguageSet lsEmpty = englishLang.guessLanguages("");
        assertEquals(Languages.ANY, lsEmpty.getAny());
        assertTrue(lsEmpty.isEmpty());

        Languages.LanguageSet lsSingle = englishLang.guessLanguages("the quick brown fox jumps over the lazy dog");
        assertEquals(NameType.ENGLISH, lsSingle.getAny());
        assertTrue(lsSingle.containsLanguage(NameType.ENGLISH));
        assertFalse(lsSingle.containsLanguage("aa"));
        // ... no other languages
    }

    @Test
    public void testLangRule() {
        Pattern pattern = Pattern.compile("^the\\s+quick");
        Set<String> languages = new HashSet<String>(Arrays.asList("en"));
        LangRule rule = new LangRule(pattern, languages, true);

        assertTrue(rule.matches("the quick brown fox jumps over the lazy dog"));
        assertFalse(rule.matches("hello world"));

        assertEquals("en", rule.languages.toArray(new String[0])[0]);
    }
}