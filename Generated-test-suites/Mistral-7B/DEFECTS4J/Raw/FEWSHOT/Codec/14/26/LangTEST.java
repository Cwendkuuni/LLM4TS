package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    @Test
    public void testLoadFromResource() {
        Lang englishLang = Lang.loadFromResource(Lang.LANGUAGE_RULES_RN, Languages.getInstance(NameType.ENGLISH));
        assertEquals(englishLang, Lang.instance(NameType.ENGLISH));
    }

    @Test
    public void testGuessLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // English words
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("hello"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("goodbye"));
        assertEquals(NameType.ENGLISH, englishLang.guessLanguage("world"));

        // Non-English words
        assertEquals(Languages.ANY, englishLang.guessLanguage("hola"));
        assertEquals(Languages.ANY, englishLang.guessLanguage("adios"));
        assertEquals(Languages.ANY, englishLang.guessLanguage("monde"));
    }

    @Test
    public void testGuessLanguages() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        // English words
        assertEquals(Languages.singleton("en"), englishLang.guessLanguages("hello"));
        assertEquals(Languages.singleton("en"), englishLang.guessLanguages("goodbye"));
        assertEquals(Languages.singleton("en"), englishLang.guessLanguages("world"));

        // Non-English words
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("es", "en", "fr"));
        assertEquals(Languages.languageSet(expectedLanguages), englishLang.guessLanguages("hola"));
        assertEquals(Languages.languageSet(expectedLanguages), englishLang.guessLanguages("adios"));
        assertEquals(Languages.languageSet(Arrays.asList("fr", "en")), englishLang.guessLanguages("monde"));
    }

    @Test
    public void testGuessLanguageWithEmptyRules() {
        Lang emptyLang = Lang.loadFromResource("", Languages.getInstance(NameType.ENGLISH));
        assertEquals(Languages.ANY, emptyLang.guessLanguage(""));
        assertEquals(Collections.emptySet(), emptyLang.guessLanguages(""));
    }
}