package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;

/**
 * JUnit test file for Lang class.
 */
public class LangTest {

    /**
     * Test instance creation.
     */
    @org.junit.Test
    public void testInstance() {
        final Lang lang = Lang.instance(NameType.ENGLISH);
        assertNotNull(lang);
    }

    /**
     * Test guessing language for simple words.
     */
    @org.junit.Test
    public void testGuessLanguageSimple() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        final String[] words = { "hello", "world", "test", "java" };
        for (final String word : words) {
            final String result = lang.guessLanguage(word);
            final Languages expected = Languages.getLanguageByName(word);
            assertEquals(expected.getName(), result);
        }
    }

    /**
     * Test guessing language for multi-language words.
     */
    @org.junit.Test
    public void testGuessLanguageMultiLanguage() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        final String[] words = { "hallå", "bonjour", "guten Tag", "ciao", "こんにちは" };
        for (final String word : words) {
            final Languages expected = Languages.getLanguageByName(word.substring(0, 2));
            final String result = lang.guessLanguage(word);
            assertEquals(expected.getName(), result);
        }
    }

    /**
     * Test guessing language for ambiguous words.
     */
    @org.junit.Test
    public void testGuessLanguageAmbiguous() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        final String[] words = { "tom", "jim", "kris", "matt" };
        for (final String word : words) {
            final Set<String> result = lang.guessLanguages(word);
            final Set<String> expected = Arrays.asList("en", "da", "nl", "de", "fi").stream()
                    .map(Languages::getLanguageByName)
                    .map(Languages.Language::getName)
                    .collect(Collectors.toSet());
            assertEquals(expected, result);
        }
    }

    /**
     * Test guessing language for incorrect words.
     */
    @org.junit.Test
    public void testGuessLanguageIncorrect() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        final String[] words = { "zebra", "xyzzy", "quux" };
        for (final String word : words) {
            final Languages expected = Languages.ANY;
            final String result = lang.guessLanguage(word);
            assertEquals(expected.getName(), result);
        }
    }

    /**
     * Test guessing languages for incorrect words.
     */
    @org.junit.Test
    public void testGuessLanguagesIncorrect() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        final String[] words = { "zebra", "xyzzy", "quux" };
        for (final String word : words) {
            final Set<String> expected = Collections.emptySet();
            final Set<String> result = lang.guessLanguages(word);
            assertEquals(expected, result);
        }
    }

    /**
     * Test guessing languages for empty words.
     */
    @org.junit.Test
    public void testGuessLanguagesEmpty() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        final String empty = "";
        final Set<String> expected = Collections.emptySet();
        final Set<String> result = lang.guessLanguages(empty);
        assertEquals(expected, result);
    }

    /**
     * Test guessing languages for null words.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testGuessLanguagesNull() {
        final Lang lang = Lang.instance(NameType.ENGLISH);

        final String nullWord = null;
        lang.guessLanguages(nullWord);
    }

    /**
     * Test loading rules from resource.
     */
    @org.junit.Test
    public void testLoadFromResource() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", english);

        assertNotNull(lang);
        assertEquals(english, lang.languages);

        final Set<String> expected = Arrays.asList("en", "da", "nl", "de", "fi", "fr", "ja", "no", "sv").stream()
                .map(Languages::getLanguageByName)
                .map(Languages.Language::getName)
                .collect(Collectors.toSet());
        final Set<String> result = new HashSet<String>(lang.rules.size());
        for (final LangRule rule : lang.rules) {
            result.addAll(rule.languages);
        }
        assertEquals(expected, result);
    }

    /**
     * Test loading rules from empty resource.
     */
    @org.junit.Test
    public void testLoadFromResourceEmpty() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_empty.txt", english);

        assertNotNull(lang);
        assertEquals(english, lang.languages);
        assertTrue(lang.rules.isEmpty());
    }

    /**
     * Test loading rules from invalid resource.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceInvalid() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_invalid.txt", english);
    }

    /**
     * Test loading rules from comment-only resource.
     */
    @org.junit.Test
    public void testLoadFromResourceCommentOnly() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_comment_only.txt", english);

        assertNotNull(lang);
        assertEquals(english, lang.languages);
        assertTrue(lang.rules.isEmpty());
    }

    /**
     * Test loading rules from multi-line comment resource.
     */
    @org.junit.Test
    public void testLoadFromResourceMultiLineComment() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_multi_line_comment.txt", english);

        assertNotNull(lang);
        assertEquals(english, lang.languages);
        assertTrue(lang.rules.isEmpty());
    }

    /**
     * Test loading rules from invalid pattern resource.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceInvalidPattern() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_invalid_pattern.txt", english);
    }

    /**
     * Test loading rules from missing resource.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testLoadFromResourceMissing() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_missing.txt", english);
    }

    /**
     * Test loading rules with no matches resource.
     */
    @org.junit.Test
    public void testLoadFromResourceNoMatches() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_no_matches.txt", english);

        assertNotNull(lang);
        assertEquals(english, lang.languages);
        assertTrue(lang.rules.isEmpty());
    }

    /**
     * Test loading rules with no languages resource.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceNoLanguages() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_no_languages.txt", english);
    }

    /**
     * Test loading rules with empty lines resource.
     */
    @org.junit.Test
    public void testLoadFromResourceEmptyLines() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_empty_lines.txt", english);

        assertNotNull(lang);
        assertEquals(english, lang.languages);
        assertTrue(lang.rules.isEmpty());
    }

    /**
     * Test loading rules with incorrect format resource.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceIncorrectFormat() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_incorrect_format.txt", english);
    }

    /**
     * Test loading rules with malformed pattern resource.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceMalformedPattern() {
        final Languages english = Languages.getInstance(NameType.ENGLISH);
        final Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_malformed_pattern.txt", english);
    }
}