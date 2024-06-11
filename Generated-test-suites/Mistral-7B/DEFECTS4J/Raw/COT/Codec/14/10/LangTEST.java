package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals(NameType.ENGLISH, lang.getNameType());
    }

    @Test
    public void testGuessLanguage() {
        Lang english = Lang.instance(NameType.ENGLISH);
        String text = "Hello World";
        String expected = Languages.ENGLISH.getName();
        String result = english.guessLanguage(text);
        assertEquals(expected, result);
    }

    @Test
    public void testGuessLanguages() {
        Lang english = Lang.instance(NameType.ENGLISH);
        String text = "Hello World";
        Set<String> expected = new HashSet<String>(Arrays.asList(Languages.ENGLISH.getName(), Languages.GERMAN.getName()));
        Set<String> result = english.guessLanguages(text);
        assertEquals(expected, result);

        text = "Hallo Welt";
        expected.clear();
        expected.add(Languages.GERMAN.getName());
        result = english.guessLanguages(text);
        assertEquals(expected, result);
    }

    @Test
    public void testAcceptOnMatch() {
        Lang english = Lang.instance(NameType.ENGLISH);
        String text = "Hello World";
        Set<String> languages = new HashSet<String>(Arrays.asList(Languages.ENGLISH.getName(), Languages.GERMAN.getName()));
        LangRule rule = new LangRule(Pattern.compile("^Hello"), languages, true);
        List<LangRule> rules = Arrays.asList(rule);
        Lang lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        Set<String> result = lang.guessLanguages(text);
        assertEquals(languages, result);

        text = "Hallo Welt";
        languages.clear();
        languages.add(Languages.GERMAN.getName());
        rule = new LangRule(Pattern.compile("^Hallo"), languages, false);
        rules = Arrays.asList(rule);
        lang = new Lang(rules, Languages.getInstance(NameType.ENGLISH));
        result = lang.guessLanguages(text);
        assertFalse(languages.containsAll(result));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMalformedLine() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang.txt";
        Lang lang = Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(NameType.ENGLISH));
    }

    @Test(expected = IllegalStateException.class)
    public void testUnableToResolveRequiredResource() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_missing.txt";
        Lang lang = Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(NameType.ENGLISH));
    }

}