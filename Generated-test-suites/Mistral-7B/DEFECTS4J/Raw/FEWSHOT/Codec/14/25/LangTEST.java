package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.Lang.NameType;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    @Test
    public void testGuessLanguage_English() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = "The quick brown fox jumps over the lazy dog";
        String language = lang.guessLanguage(text);
        assertEquals(Languages.ENGLISH, language);
    }

    @Test
    public void testGuessLanguage_NonExistent() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = "こんにちは";
        String language = lang.guessLanguage(text);
        assertEquals(Languages.ANY, language);
    }

    @Test
    public void testGuessLanguages_English() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = "The quick brown fox jumps over the lazy dog";
        Collection<String> languages = lang.guessLanguages(text);
        assertTrue(languages.contains(Languages.ENGLISH.getName()));
        assertNull(languages.stream().filter(l -> !l.equals(Languages.ENGLISH.getName())).findFirst().orElse(null));
    }

    @Test
    public void testGuessLanguages_Mixed() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = "こんにちは The quick brown fox jumps over the lazy dog";
        Collection<String> languages = lang.guessLanguages(text);
        assertTrue(languages.contains(Languages.ENGLISH.getName()));
        assertTrue(languages.contains(Languages.JAPANESE.getName()));
    }

    @Test
    public void testGuessLanguages_Japanese() {
        Lang lang = Lang.instance(NameType.JAPANESE);
        String text = "こんにちは";
        Collection<String> languages = lang.guessLanguages(text);
        assertTrue(languages.contains(Languages.JAPANESE.getName()));
        assertNull(languages.stream().filter(l -> !l.equals(Languages.JAPANESE.getName())).findFirst().orElse(null));
    }

    @Test
    public void testLoadFromResource() {
        Lang english = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.ENGLISH);
        Lang japanese = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_ja.txt", Languages.JAPANESE);

        assertEquals(EnumSet.allOf(NameType.class), Arrays.asList(english.getNameType(), japanese.getNameType()));

        assertEquals(2, english.getRules().size());
        assertEquals(2, japanese.getRules().size());

        LangRule rule1 = english.getRules().get(0);
        LangRule rule2 = english.getRules().get(1);

        LangRule rule3 = japanese.getRules().get(0);
        LangRule rule4 = japanese.getRules().get(1);

        assertEquals("\\bthe\\.", rule1.getPattern().pattern(), "Pattern for rule 1 in English");
        assertEquals(Arrays.asList(Languages.ENGLISH.getName(), Languages.AFRIKAANS.getName()), rule1.getLanguages());
        assertTrue(rule1.isAcceptOnMatch());

        assertEquals("\\bthe\\.", rule2.getPattern().pattern(), "Pattern for rule 2 in English");
        assertEquals(Collections.emptySet(), rule2.getLanguages());
        assertFalse(rule2.isAcceptOnMatch());

        assertEquals("\\bわたし\\.", rule3.getPattern().pattern(), "Pattern for rule 1 in Japanese");
        assertEquals(Arrays.asList(Languages.JAPANESE.getName()), rule3.getLanguages());
        assertTrue(rule3.isAcceptOnMatch());

        assertEquals("\\bはんぱと\\.", rule4.getPattern().pattern(), "Pattern for rule 2 in Japanese");
        assertEquals(Collections.emptySet(), rule4.getLanguages());
        assertFalse(rule4.isAcceptOnMatch());
    }
}