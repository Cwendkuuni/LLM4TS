package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    @Test
    public void testLoadFromResource() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);
        assertEquals(englishLang.rules.size(), 31);

        Lang frenchLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_fr.txt", Languages.getInstance(NameType.FRENCH));
        assertEquals(frenchLang.rules.size(), 24);
    }

    @Test
    public void testGuessLanguage() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        assertEquals(englishLang.guessLanguage("hello"), Languages.ENGLISH);
        assertEquals(englishLang.guessLanguage("hola"), Languages.SPANISH);
        assertEquals(englishLang.guessLanguage(""), Languages.ANY);
        assertNull(englishLang.guessLanguage("$$$"));

        Lang frenchLang = Lang.instance(NameType.FRENCH);

        assertEquals(frenchLang.guessLanguage("bonjour"), Languages.FRENCH);
        assertEquals(frenchLang.guessLanguage("hola"), Languages.SPANISH);
        assertEquals(frenchLang.guessLanguage(""), Languages.ANY);
        assertEquals(frenchLang.guessLanguage("hello"), Languages.ENGLISH);
    }

    @Test
    public void testGuessLanguages() {
        Lang englishLang = Lang.instance(NameType.ENGLISH);

        Set<String> expectedEnglish = new HashSet<>(Arrays.asList("en", "eng"));
        Set<String> englishLanguages = englishLang.guessLanguages("hello");
        assertEquals(expectedEnglish, englishLanguages);

        Set<String> expectedSpanish = new HashSet<>(Arrays.asList("es", "spa"));
        Set<String> spanishLanguages = englishLang.guessLanguages("hola");
        assertEquals(expectedSpanish, spanishLanguages);

        Set<String> emptyLanguages = new HashSet<>();
        Set<String> emptyLanguageSet = englishLang.guessLanguages("");
        assertEquals(emptyLanguages, emptyLanguageSet);

        Set<String> invalidLanguages = new HashSet<>(Arrays.asList("xyz"));
        Set<String> invalidLanguageSet = englishLang.guessLanguages("$$$");
        assertEquals(Collections.emptySet(), invalidLanguageSet);

        Lang frenchLang = Lang.instance(NameType.FRENCH);

        Set<String> expectedFrench = new HashSet<>(Arrays.asList("fr", "fra"));
        Set<String> frenchLanguages = frenchLang.guessLanguages("bonjour");
        assertEquals(expectedFrench, frenchLanguages);

        Set<String> languagesForHello = frenchLang.guessLanguages("hello");
        assertEquals(expectedEnglish, languagesForHello);
    }
}