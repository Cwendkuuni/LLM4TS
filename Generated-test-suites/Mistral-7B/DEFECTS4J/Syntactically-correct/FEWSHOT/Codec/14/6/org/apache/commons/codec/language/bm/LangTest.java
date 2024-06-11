package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LangTest {

    @Test
    public void testLangInstance() {
        Lang english = Lang.instance(NameType.ENGLISH);
        assertEquals(NameType.ENGLISH, english.getNameType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceWithNullNameType() {
        Lang.loadFromResource(null, Languages.getInstance(NameType.ENGLISH));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceWithNullLanguageRulesResourceName() {
        Lang.loadFromResource(null, Languages.getInstance(NameType.ENGLISH));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceWithNullLanguages() {
        Lang.loadFromResource(LANGUAGE_RULES_RN, null);
    }

    @Test
    public void testLoadFromResource() {
        Lang english = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.ENGLISH));
        assertEquals(NameType.ENGLISH, english.getNameType());
        assertEquals(2, english.rules.size());

        Lang german = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.GERMAN));
        assertEquals(NameType.GERMAN, german.getNameType());
        assertEquals(3, german.rules.size());
    }

    @Test
    public void testGuessLanguage() {
        Lang english = Lang.instance(NameType.ENGLISH);
        String textEnglish = "hello world";
        assertEquals(NameType.ENGLISH, english.guessLanguage(textEnglish));

        Lang german = Lang.instance(NameType.GERMAN);
        String textGerman = "hallo welt";
        assertEquals(NameType.GERMAN, german.guessLanguage(textGerman));

        Lang neither = Lang.instance(NameType.NEUTRAL);
        String textNeither = "こんにちは世界"; // Japanese
        assertEquals(Languages.ANY, neither.guessLanguage(textNeither));
    }

    @Test
    public void testGuessLanguages() {
        Lang english = Lang.instance(NameType.ENGLISH);
        String textEnglish = "hello world";
        Set<String> expected = EnumSet.of("en", "English");
        Set<String> actual = english.guessLanguages(textEnglish);
        assertEquals(expected, actual);

        Lang german = Lang.instance(NameType.GERMAN);
        String textGerman = "hallo welt";
        expected = EnumSet.of("de", "German");
        actual = german.guessLanguages(textGerman);
        assertEquals(expected, actual);

        Lang neither = Lang.instance(NameType.NEUTRAL);
        String textNeither = "こんにちは世界"; // Japanese
        Set<String> empty = Collections.emptySet();
        Set<String> actual = neither.guessLanguages(textNeither);
        assertEquals(empty, actual);
    }

    @Test
    public void testAcceptOnMatch() {
        Lang english = Lang.instance(NameType.ENGLISH);
        String textEnglish1 = "hello world";
        Set<String> expected1 = EnumSet.of("en", "English");
        Set<String> actual1 = english.guessLanguages(textEnglish1);
        assertEquals(expected1, actual1);

        Lang englishAccept = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.ENGLISH_ACCEPT));
        String textEnglish2 = "hello world";
        Set<String> expected2 = EnumSet.of("en", "English");
        Set<String> actual2 = englishAccept.guessLanguages(textEnglish2);
        assertEquals(expected2, actual2);

        Lang englishReject = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.ENGLISH_REJECT));
        String textEnglish3 = "hello world";
        Set<String> empty = Collections.emptySet();
        Set<String> actual3 = englishReject.guessLanguages(textEnglish3);
        assertEquals(empty, actual3);
    }

    private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";
}