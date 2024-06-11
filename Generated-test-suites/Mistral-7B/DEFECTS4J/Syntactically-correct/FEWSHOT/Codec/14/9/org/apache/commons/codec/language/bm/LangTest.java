package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    @Test
    public void testLoadFromResource() {
        List<LangRule> rules = Arrays.asList(
                new LangRule(Pattern.compile("^[a-z]+s?$"), Arrays.asList("en"), true),
                new LangRule(Pattern.compile("^[sz]s?$"), Arrays.asList("de", "nl"), true),
                new LangRule(Pattern.compile("^[sz]s?$"), Arrays.asList("pl"), false));

        Lang englishLang = new Lang(rules, Languages.ENGLISH);
        assertEquals(englishLang.rules, rules);
    }

    @Test
    public void testGuessLanguage() {
        Lang engLang = Lang.instance(NameType.ENGLISH);
        assertEquals(engLang.guessLanguage("apple"), Languages.ENGLISH);
        assertEquals(engLang.guessLanguage("apfel"), Languages.GERMAN);
        assertNull(engLang.guessLanguage("jabłko"));

        Lang plLang = Lang.instance(NameType.POLISH);
        assertEquals(plLang.guessLanguage("jabłko"), Languages.POLISH);
    }

    @Test
    public void testGuessLanguages() {
        Lang engLang = Lang.instance(NameType.ENGLISH);
        Set<String> expectedEngLangs = Collections.singleton("en");
        assertEquals(engLang.guessLanguages("apple"), Languages.languageSet(expectedEngLangs));

        Lang plLang = Lang.instance(NameType.POLISH);
        Set<String> expectedPlLangs = Arrays.asList("pl", "cs", "sk");
        assertEquals(plLang.guessLanguages("jabłko"), Languages.languageSet(expectedPlLangs));

        Lang anyLang = Lang.instance(NameType.ANY);
        assertEquals(anyLang.guessLanguages("jabłko"), Languages.ANY_LANGUAGE);
    }
}