package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Lang.LangRule;
import org.apache.commons.codec.language.bm.Lang.NameType;
import org.apache.commons.codec.language.bm.Languages;

public class LangTest {

    @SuppressWarnings("unchecked")
    private static <T> T instance(Class<T> clazz) {
        try {
            return (T) Lang.instance(NameType.getDefaultInstance());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to get default Lang instance", e);
        }
    }

    @org.junit.Test
    public void testGuessLanguage() {
        Lang lang = instance(Lang.class);

        // Test simple words
        assertEquals(Lang.Languages.ENGLISH, lang.guessLanguage("hello"));
        assertEquals(Lang.Languages.SPANISH, lang.guessLanguage("hola"));
        assertEquals(Lang.Languages.FRENCH, lang.guessLanguage("bonjour"));

        // Test empty string
        assertEquals(Lang.Languages.ANY, lang.guessLanguage(""));

        // Test null input
        assertNull(lang.guessLanguage(null));

        // Test invalid input
        assertEquals(Lang.Languages.ANY, lang.guessLanguage(""));
        assertEquals(Lang.Languages.ANY, lang.guessLanguage(" "));
        assertEquals(Lang.Languages.ANY, lang.guessLanguage("hello world"));

        // Test edge cases and exceptions
        testEdgeCasesAndExceptions(lang);
    }

    @org.junit.Test
    public void testGuessLanguages() {
        Lang lang = instance(Lang.class);

        // Test simple words
        Set<String> expected = new HashSet<>(Arrays.asList("en", "es", "fr"));
        Set<String> actual = lang.guessLanguages("hello").getLanguages();
        assertEquals(expected, actual);

        expected = new HashSet<>(Arrays.asList("es", "fr"));
        actual = lang.guessLanguages("hola").getLanguages();
        assertEquals(expected, actual);

        expected = new HashSet<>(Arrays.asList("fr"));
        actual = lang.guessLanguages("bonjour").getLanguages();
        assertEquals(expected, actual);

        // Test empty string
        assertEquals(Collections.singleton(Lang.Languages.ANY.getName()), lang.guessLanguages("").getLanguages());

        // Test null input
        assertNull(lang.guessLanguages(null));

        // Test invalid input
        Set<String> actual = lang.guessLanguages("").getLanguages();
        assertEquals(Collections.singleton(Lang.Languages.ANY.getName()), actual);
        actual = lang.guessLanguages(" ").getLanguages();
        assertEquals(Collections.singleton(Lang.Languages.ANY.getName()), actual);
        actual = lang.guessLanguages("hello world").getLanguages();
        assertEquals(Collections.singleton(Lang.Languages.ANY.getName()), actual);

        // Test edge cases and exceptions
        testEdgeCasesAndExceptions(lang, true);
    }

    private void testEdgeCasesAndExceptions(Lang lang) {
        // Test long input
        String longInput = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        Set<String> actual = lang.guessLanguages(longInput).getLanguages();
        assertTrue(actual.size() > 0);
        assertTrue(actual.contains(Lang.Languages.LATIN.getName()));
        assertTrue(actual.contains(Lang.Languages.ENGLISH.getName()));
        assertTrue(actual.contains(Lang.Languages.FRENCH.getName()));
        //... add more expected languages based on the language rules

        // Test input with special characters
        String specialInput = "Héllö wórld!";
        Set<String> actualSpecial = lang.guessLanguages(specialInput).getLanguages();
        assertTrue(actualSpecial.size() > 0);
        assertTrue(actualSpecial.contains(Lang.Languages.FRENCH.getName()));
        assertTrue(actualSpecial.contains(Lang.Languages.ENGLISH.getName()));
        //... add more expected languages based on the language rules

        // Test input with numbers
        String numberInput = "12345 hello world";
        Set<String> actualNumber = lang.guessLanguages(numberInput).getLanguages();
        assertTrue(actualNumber.size() > 0);
        assertTrue(actualNumber.contains(Lang.Languages.ENGLISH.getName()));
        //... add more expected languages based on the language rules

        // Test input with multiple languages
        String multiLanguageInput = "hello hola bonjour";
        Set<String> actualMulti = lang.guessLanguages(multiLanguageInput).getLanguages();
        assertTrue(actualMulti.size() > 0);
        assertTrue(actualMulti.contains(Lang.Languages.ENGLISH.getName()));
        assertTrue(actualMulti.contains(Lang.Languages.SPANISH.getName()));
        assertTrue(actualMulti.contains(Lang.Languages.FRENCH.getName()));
        //... add more expected languages based on the language rules

        // Test input with special characters and numbers
        String multiLanguageInputSpecialNumber = "Héllö wórld! 12345";
        Set<String> actualMultiSpecialNumber = lang.guessLanguages(multiLanguageInputSpecialNumber).getLanguages();
        assertTrue(actualMultiSpecialNumber.size() > 0);
        assertTrue(actualMultiSpecialNumber.contains(Lang.Languages.FRENCH.getName()));
        assertTrue(actualMultiSpecialNumber.contains(Lang.Languages.ENGLISH.getName()));
        //... add more expected languages based on the language rules

        // Test input with empty lines
        String emptyLinesInput = "hello\n\nworld";
        Set<String> actualEmptyLines = lang.guessLanguages(emptyLinesInput).getLanguages();
        assertTrue(actualEmptyLines.size() > 0);
        assertTrue(actualEmptyLines.contains(Lang.Languages.ENGLISH.getName()));
        //... add more expected languages based on the language rules

        // Test input with comments
        String commentInput = "hello// this is a comment world";
        Set<String> actualComment = lang.guessLanguages(commentInput).getLanguages();
        assertTrue(actualComment.size() > 0);
        assertTrue(actualComment.contains(Lang.Languages.ENGLISH.getName()));
        //... add more expected languages based on the language rules

        // Test input with extended comments
        String extendedCommentInput = "hello/* this is an extended comment */world";
        Set<String> actualExtendedComment = lang.guessLanguages(extendedCommentInput).getLanguages();
        assertTrue(actualExtendedComment.size() > 0);
        assertTrue(actualExtendedComment.contains(Lang.Languages.ENGLISH.getName()));
        //... add more expected languages based on the language rules
    }

    private void testEdgeCasesAndExceptions(Lang lang, boolean printDetails) {
        // Test long input
        String longInput = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        Set<String> actual = lang.guessLanguages(longInput).getLanguages();
        if (printDetails) {
            System.out.println("Long input: expected = " + Arrays.toString(Lang.Languages.values()) + ", actual = " + actual);
        }
        assertTrue(actual.size() > 0);
        assertTrue(actual.containsAll(Arrays.asList(Lang.Languages.LATIN.getName(), Lang.Languages.ENGLISH.getName(), Lang.Languages.FRENCH.getName())));
        //... add more expected languages based on the language rules

        // Test input with special characters
        String specialInput = "Héllö wórld!";
        Set<String> actualSpecial = lang.guessLanguages(specialInput).getLanguages();
        if (printDetails) {
            System.out.println("Special input: expected = " + Arrays.toString(Lang.Languages.values()) + ", actual = " + actual);
        }
        assertTrue(actual.size() > 0);
        assertTrue(actual.containsAll(Arrays.asList(Lang.Languages.FRENCH.getName(), Lang.Languages.ENGLISH.getName())));
        //... add more expected languages based on the language rules

        // Test input with numbers
        String numberInput = "12345 hello world";
        Set<String> actualNumber = lang.guessLanguages(numberInput).getLanguages();
        if (printDetails) {
            System.out.println("Number input: expected = " + Arrays.toString(Lang.Languages.values()) + ", actual = " + actual);
        }
        assertTrue(actual.size() > 0);
        assertTrue(actual.containsAll(Arrays.asList(Lang.Languages.ENGLISH.getName())));
        //... add more expected languages based on the language rules

        // Test input with multiple languages
        String multiLanguageInput = "hello hola bonjour";
        Set<String> actualMulti = lang.guessLanguages(multiLanguageInput).getLanguages();
        if (printDetails) {
            System.out.println("Multi language input: expected = " + Arrays.toString(Lang.Languages.values()) + ", actual = " + actual);
        }
        assertTrue(actual.size() > 0);
        assertTrue(actual.containsAll(Arrays.asList(Lang.Languages.ENGLISH.getName(), Lang.Languages.SPANISH.getName(), Lang.Languages.FRENCH.getName())));
        //... add more expected languages based on the language rules

        // Test input with special characters and numbers
        String multiLanguageInputSpecialNumber = "Héllö wórld! 12345";
        Set<String> actualMultiSpecialNumber = lang.guessLanguages(multiLanguageInputSpecialNumber).getLanguages();
        if (printDetails) {
            System.out.println("Multi language input with special characters and numbers: expected = " + Arrays.toString(Lang.Languages.values()) + ", actual = " + actual);
        }
        assertTrue(actualMultiSpecialNumber.size() > 0);
        assertTrue(actualMultiSpecialNumber.containsAll(Arrays.asList(Lang.Languages.FRENCH.getName(), Lang.Languages.ENGLISH.getName())));
        //... add more expected languages based on the language rules

        // Test input with empty lines
        String emptyLinesInput = "hello\n\nworld";
        Set<String> actualEmptyLines = lang.guessLanguages(emptyLinesInput).getLanguages();
        if (printDetails) {
            System.out.println("Empty lines input: expected = " + Arrays.toString(Lang.Languages.values()) + ", actual = " + actual);
        }
        assertTrue(actualEmptyLines.size() > 0);
        assertTrue(actualEmptyLines.containsAll(Arrays.asList(Lang.Languages.ENGLISH.getName())));
        //... add more expected languages based on the language rules

        // Test input with comments
        String commentInput = "hello// this is a comment world";
        Set<String> actualComment = lang.guessLanguages(commentInput).getLanguages();
        if (printDetails) {
            System.out.println("Comment input: expected = " + Arrays.toString(Lang.Languages.values()) + ", actual = " + actual);
        }
        assertTrue(actualComment.size() > 0);
        assertTrue(actualComment.containsAll(Arrays.asList(Lang.Languages.ENGLISH.getName())));
        //... add more expected languages based on the language rules

        // Test input with extended comments
        String extendedCommentInput = "hello/* this is an extended comment */world";
        Set<String> actualExtendedComment = lang.guessLanguages(extendedCommentInput).getLanguages();
        if (printDetails) {
            System.out.println("Extended comment input: expected = " + Arrays.toString(Lang.Languages.values()) + ", actual = " + actual);
        }
        assertTrue(actualExtendedComment.size() > 0);
        assertTrue(actualExtendedComment.containsAll(Arrays.asList(Lang.Languages.ENGLISH.getName())));
        //... add more expected languages based on the language rules
    }
}