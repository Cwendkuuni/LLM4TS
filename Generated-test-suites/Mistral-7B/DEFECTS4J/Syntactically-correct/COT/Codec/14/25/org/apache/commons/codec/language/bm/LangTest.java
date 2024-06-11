package org.apache.commons.codec.language.bm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;

/**
 * JUnit test cases for Lang class.
 */
public class LangTest {

    @SuppressWarnings("unchecked")
    private Lang getLangInstance(NameType nameType) {
        return Lang.instance(nameType);
    }

    @SuppressWarnings({ "unchecked", "resultofmethodcallignored" })
    @org.junit.Test
    public void testInstance() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        assertNotNull(langEnglish);
    }

    @org.junit.Test
    public void testGuessLanguageEnglish() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        String result = langEnglish.guessLanguage("hello world");
        assertEquals(Languages.ENGLISH, result);
    }

    @org.junit.Test
    public void testGuessLanguageUnknown() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        String result = langEnglish.guessLanguage("привет мир");
        assertEquals(Languages.ANY, result);
    }

    @org.junit.Test
    public void testGuessLanguagesEnglish() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        Set<String> expected = new HashSet<String>(Arrays.asList(Languages.AMHARIC, Languages.ENGLISH,
                Languages.GARBANZUSI, Languages.IGBO, Languages.KANNADA, Languages.KHMER, Languages.LAO,
                Languages.LATIN, Languages.MYANMAR, Languages.NEPALI, Languages.TIBETAN, Languages.VIETNAMESE));
        Set<String> actual = langEnglish.guessLanguages("hello world");
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGuessLanguagesUnknown() {
        Lang langEnglish = getLangInstance(NameType.ENGLISH);
        Set<String> actual = langEnglish.guessLanguages("привет мир");
        assertTrue(actual.size() == 0);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testInstanceWithInvalidNameType() {
        Lang langInvalid = getLangInstance(new NameType("INVALID"));
    }

    @org.junit.Test
    public void testLoadFromResource() {
        Lang langEnglish = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt",
                Languages.ENGLISH);
        assertNotNull(langEnglish);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testLoadFromResourceWithNullLanguage() {
        Lang langNull = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceWithMalformedLine() {
        Lang langMalformed = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en_malformed.txt",
                Languages.ENGLISH);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testLoadFromResourceWithMissingResource() {
        Lang langMissing = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_non_existent.txt",
                Languages.ENGLISH);
    }
}