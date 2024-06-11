package org.apache.commons.codec.language.bm;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LangTest {

    @Test
    public void testEnglishLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String englishWord = "hello";
        String guessedLanguage = lang.guessLanguage(englishWord);
        assertTrue(guessedLanguage.equals(Languages.ENGLISH));
    }

    @Test
    public void testFrenchLanguage() {
        Lang lang = Lang.instance(NameType.FRENCH);
        String frenchWord = "bonjour";
        String guessedLanguage = lang.guessLanguage(frenchWord);
        assertTrue(guessedLanguage.equals(Languages.FRENCH));
    }

    @Test
    public void testMixedLanguageWord() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String mixedWord = "hello bonjour";
        String guessedLanguage = lang.guessLanguage(mixedWord);
        Assert.assertEquals(Languages.ENGLISH, guessedLanguage);
    }

    @Test
    public void testEmptyString() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String emptyString = "";
        String guessedLanguage = lang.guessLanguage(emptyString);
        assertEquals(Languages.ANY, guessedLanguage);
    }

    @Test
    public void testNullString() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String nullString = null;
        String guessedLanguage = lang.guessLanguage(nullString);
        assertEquals(Languages.ANY, guessedLanguage);
    }

    @Test
    public void testMalformedRuleResource() {
        try {
            Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/malformedlang.txt", Languages.getInstance(NameType.ENGLISH));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}