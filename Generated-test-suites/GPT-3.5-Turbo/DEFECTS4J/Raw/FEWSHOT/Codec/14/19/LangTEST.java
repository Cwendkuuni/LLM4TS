package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals("eng", englishLang.guessLanguage("hello"));

        Lang frenchLang = Lang.instance(NameType.FRENCH);
        assertEquals("fre", frenchLang.guessLanguage("bonjour"));

        Lang germanLang = Lang.instance(NameType.GERMAN);
        assertEquals("deu", germanLang.guessLanguage("hallo"));

        Lang italianLang = Lang.instance(NameType.ITALIAN);
        assertEquals("ita", italianLang.guessLanguage("ciao"));
    }

    @Test
    public void testGuessLanguages() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals(Languages.LanguageSet.from("eng"), englishLang.guessLanguages("hello"));

        Lang frenchLang = Lang.instance(NameType.FRENCH);
        assertEquals(Languages.LanguageSet.from("fre"), frenchLang.guessLanguages("bonjour"));

        Lang germanLang = Lang.instance(NameType.GERMAN);
        assertEquals(Languages.LanguageSet.from("deu"), germanLang.guessLanguages("hallo"));

        Lang italianLang = Lang.instance(NameType.ITALIAN);
        assertEquals(Languages.LanguageSet.from("ita"), italianLang.guessLanguages("ciao"));
    }
}