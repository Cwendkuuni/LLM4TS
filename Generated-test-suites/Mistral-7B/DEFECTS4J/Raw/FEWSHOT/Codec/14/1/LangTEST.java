package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.lang3.StringUtils;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.DETECT);
        assertNotNull(lang);

        Lang langDetect = Lang.instance(NameType.DETECT);
        assertEquals(lang, langDetect);

        Lang langEnglish = Lang.instance(NameType.ENGLISH);
        Lang langFrench = Lang.instance(NameType.FRENCH);
        assertNotNull(langEnglish);
        assertNotNull(langFrench);
        assertNotEquals(langEnglish, langFrench);
    }

    @Test
    public void testGuessLanguage() {
        Lang langDetect = Lang.instance(NameType.DETECT);

        Set<String> supportedLanguages = new HashSet<String>(Arrays.asList(
                "af", "ar", "az", "ba", "be", "bg", "bn", "bs", "ca", "cs", "cy", "da", "de", "el", "en",
                "eo", "es", "et", "eu", "fa", "fi", "fo", "fr", "ga", "gl", "gu", "he", "hi", "hr", "hu",
                "hy", "id", "is", "it", "ja", "ka", "kk", "kl", "ko", "la", "lv", "lt", "mk", "ml", "mn",
                "mr", "ms", "mt", "ne", "nl", "no", "pa", "pl", "ps", "pt", "ro", "ru", "sa", "si", "sk",
                "sl", "sq", "sr", "sv", "sw", "ta", "te", "th", "tr", "ts", "tt", "ug", "uk", "ur", "vi",
                "wo", "yi", "zh"
        ));

        Set<String> languages;

        // Test empty input
        languages = langDetect.guessLanguages("");
        assertEquals(Languages.ANY, languages);

        // Test some known language names
        languages = langDetect.guessLanguages("English");
        assertEquals(Collections.singletonList("en"), languages.getLanguages());

        languages = langDetect.guessLanguages("French");
        assertEquals(Collections.singletonList("fr"), languages.getLanguages());

        languages = langDetect.guessLanguages("Deutsch");
        assertEquals(Collections.singletonList("de"), languages.getLanguages());

        // Test some known words
        languages = langDetect.guessLanguages("hello");
        assertEquals(supportedLanguages, languages.getLanguages());

        languages = langDetect.guessLanguages("bonjour");
        assertEquals(Collections.singletonList("fr"), languages.getLanguages());

        languages = langDetect.guessLanguages("Привет");
        assertEquals(Collections.singletonList("ru"), languages.getLanguages());

        // Test some ambiguous words
        languages = langDetect.guessLanguages("world");
        assertEquals(new HashSet<String>(Arrays.asList("en", "es", "fr", "de")), languages.getLanguages());

        languages = langDetect.guessLanguages("apple");
        assertEquals(new HashSet<String>(Arrays.asList("en", "de", "es", "fr", "pl")), languages.getLanguages());

        languages = langDetect.guessLanguages("בוקר");
        assertEquals(new HashSet<String>(Arrays.asList("he", "ar")), languages.getLanguages());

        // Test some words with special characters
        languages = langDetect.guessLanguages("Möhren");
        assertEquals(new HashSet<String>(Arrays.asList("de")), languages.getLanguages());

        languages = langDetect.guessLanguages("محمد");
        assertEquals(new HashSet<String>(Arrays.asList("ar")), languages.getLanguages());

        languages = langDetect.guessLanguages("हिंदू");
        assertEquals(new HashSet<String>(Arrays.asList("hi")), languages.getLanguages());

        // Test some words with multiple languages
        languages = langDetect.guessLanguages("abracadabra");
        assertEquals(new HashSet<String>(Arrays.asList("en", "es", "pt")), languages.getLanguages());

        // Test some words with no language match
        languages = langDetect.guessLanguages("xpto");
        assertEquals(Languages.ANY, languages);

        // Test some words with multi-byte characters
        languages = langDetect.guessLanguages("你好");
        assertEquals(new HashSet<String>(Arrays.asList("zh")), languages.getLanguages());

        // Test some words with diacritics
        languages = langDetect.guessLanguages("Mërens");
        assertEquals(new HashSet<String>(Arrays.asList("de")), languages.getLanguages());

        // Test some words with multiple words
        languages = langDetect.guessLanguages("hello world");
        assertEquals(new HashSet<String>(Arrays.asList("en")), languages.getLanguages());

        // Test some words with numbers
        languages = langDetect.guessLanguages("12345");
        assertEquals(Languages.ANY, languages);

        // Test some words with digits only
        languages = langDetect.guessLanguages("1234567890");
        assertEquals(Languages.ANY, languages);
    }

    @Test
    public void testGuessLanguages() {
        Lang langDetect = Lang.instance(NameType.DETECT);

        Set<String> languages;

        // Test empty input
        languages = langDetect.guessLanguages(StringUtils.EMPTY);
        assertEquals(Languages.ANY, languages);

        // Test some known language names
        languages = langDetect.guessLanguages("English");
        assertEquals(Collections.singletonList("en"), languages.getLanguages());

        languages = langDetect.guessLanguages("French");
        assertEquals(Collections.singletonList("fr"), languages.getLanguages());

        languages = langDetect.guessLanguages("Deutsch");
        assertEquals(Collections.singletonList("de"), languages.getLanguages());

        // Test some known words
        languages = langDetect.guessLanguages("hello");
        assertEquals(supportedLanguages, languages.getLanguages());

        languages = langDetect.guessLanguages("bonjour");
        assertEquals(Collections.singletonList("fr"), languages.getLanguages());

        languages = langDetect.guessLanguages("Привет");
        assertEquals(Collections.singletonList("ru"), languages.getLanguages());

        // Test some ambiguous words
        languages = langDetect.guessLanguages("world");
        assertEquals(new EnumSet<NameType>(NameType.DETECT, NameType.EN, NameType.ES, NameType.FR, NameType.DE), languages.getNames());

        languages = langDetect.guessLanguages("apple");
        assertEquals(new EnumSet<NameType>(NameType.DETECT, NameType.EN, NameType.DE, NameType.ES, NameType.FR, NameType.PL), languages.getNames());

        languages = langDetect.guessLanguages("בוקר");
        assertEquals(new EnumSet<NameType>(NameType.DETECT, NameType.HE, NameType.AR), languages.getNames());

        // Test some words with special characters
        languages = langDetect.guessLanguages("Möhren");
        assertEquals(new EnumSet<NameType>(NameType.DETECT, NameType.DE), languages.getNames());

        languages = langDetect.guessLanguages("محمد");
        assertEquals(new EnumSet<NameType>(NameType.DETECT, NameType.AR), languages.getNames());

        languages = langDetect.guessLanguages("हिंदू");
        assertEquals(new EnumSet<NameType>(NameType.DETECT, NameType.HI), languages.getNames());

        // Test some words with multiple languages
        languages = langDetect.guessLanguages("abracadabra");
        assertEquals(new EnumSet<NameType>(NameType.DETECT, NameType.EN, NameType.ES, NameType.PT), languages.getNames());

        // Test some words with no language match
        languages = langDetect.guessLanguages("xpto");
        assertEquals(Languages.NO_LANGUAGES, languages);

        // Test some words with multi-byte characters
        languages = langDetect.guessLanguages("你好");
        assertEquals(new EnumSet<NameType>(NameType.DETECT, NameType.ZH), languages.getNames());

        // Test some words with diacritics
        languages = langDetect.guessLanguages("Mërens");
        assertEquals(new EnumSet<NameType>(NameType.DETECT, NameType.DE), languages.getNames());

        // Test some words with multiple words
        languages = langDetect.guessLanguages("hello world");
        assertEquals(new EnumSet<NameType>(NameType.DETECT, NameType.EN), languages.getNames());

        // Test some words with numbers
        languages = langDetect.guessLanguages("12345");
        assertEquals(Languages.ANY, languages);

        // Test some words with digits only
        languages = langDetect.guessLanguages("1234567890");
        assertEquals(Languages.ANY, languages);
    }
}