package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.Languages.LanguageSet;

public class LangTest {

    private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.DETECT);
        assertNotNull(lang);
    }

    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.DETECT));
        assertNotNull(lang);
    }

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.DETECT);
        assertEquals(Lang.Languages.ENGLISH, lang.guessLanguage("hello"));
        assertEquals(Lang.Languages.GERMAN, lang.guessLanguage("guten tag"));
        assertEquals(Lang.Languages.ANY, lang.guessLanguage(""));
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("12345"));
        assertEquals(Lang.Languages.ANY, lang.guessLanguage(null));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.DETECT);
        List<String> expected = Arrays.asList("en", "de");

        LanguageSet actual = lang.guessLanguages("hello");
        assertEquals(LanguageSet.class, actual.getClass());
        assertEquals(expected, actual.getLanguages());

        actual = lang.guessLanguages("guten tag");
        assertEquals(expected, actual.getLanguages());

        actual = lang.guessLanguages("");
        assertEquals(Lang.Languages.ANY_LANGUAGE, actual);

        actual = lang.guessLanguages("12345");
        assertEquals(Lang.Languages.ANY_LANGUAGE, actual);

        actual = lang.guessLanguages(null);
        assertEquals(Lang.Languages.ANY_LANGUAGE, actual);
    }

    @Test
    public void testRules() {
        Lang lang = Lang.instance(NameType.DETECT);
        // You may add more test cases here to verify the behavior of specific rules.
    }
}