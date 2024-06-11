package org.apache.commons.codec.language.bm;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.Lang.LangRule.AcceptOnMatch;

/**
 * Test class for Lang class
 */
public class LangTest {

    // Test data
    private static final String TEST_TEXT = "testText";
    private static final NameType TEST_NAME_TYPE = NameType.ENGLISH;
    private static final String TEST_RESOURCE_NAME = "org/apache/commons/codec/language/bm/lang.txt";
    private static final Languages TEST_LANGUAGES = Languages.getInstance(TEST_NAME_TYPE);

    /**
     * Test constructor
     */
    @org.junit.Test
    public void testConstructor() {
        Lang instance = Lang.instance(TEST_NAME_TYPE);
        assertNotNull(instance);
    }

    /**
     * Test static factory method instance()
     */
    @org.junit.Test
    public void testInstance() {
        Lang instance = Lang.instance(TEST_NAME_TYPE);
        assertNotNull(instance);
    }

    /**
     * Test method guessLanguage
     */
    @org.junit.Test
    public void testGuessLanguage() {
        Lang instance = Lang.instance(TEST_NAME_TYPE);
        String result = instance.guessLanguage(TEST_TEXT);
        assertEquals("Expected ANY as result", Languages.ANY, result);
    }

    /**
     * Test method guessLanguages
     */
    @org.junit.Test
    public void testGuessLanguages() {
        Lang instance = Lang.instance(TEST_NAME_TYPE);
        Set<String> expected = new HashSet<>(Arrays.asList("English", "French"));
        Set<String> result = instance.guessLanguages(TEST_TEXT);
        assertEquals("Expected English and French as possible matches", expected, result);
    }

    /**
     * Test method guessLanguages with a rule that accepts a language
     */
    @org.junit.Test
    public void testGuessLanguagesAcceptOnMatch() {
        Lang instance = Lang.instance(TEST_NAME_TYPE);
        Set<String> expected = new HashSet<>(Arrays.asList("German", "Dutch"));
        Set<String> result = instance.guessLanguages("testTextWithGermanDutchRules");
        assertEquals("Expected German and Dutch as possible matches", expected, result);
    }

    /**
     * Test method guessLanguages with a rule that excludes a language
     */
    @org.junit.Test
    public void testGuessLanguagesExcludeOnMatch() {
        Lang instance = Lang.instance(TEST_NAME_TYPE);
        Set<String> expected = new HashSet<>();
        Set<String> result = instance.guessLanguages("testTextWithoutEnglishRules");
        assertEquals("Expected no languages as possible matches", expected, result);
    }

    /**
     * Test method loadFromResource
     */
    @org.junit.Test
    public void testLoadFromResource() {
        Lang instance = Lang.loadFromResource(TEST_RESOURCE_NAME, TEST_LANGUAGES);
        assertNotNull(instance);
    }

    /**
     * Test method loadFromResource with invalid resource
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testLoadFromResourceInvalidResource() {
        Lang.loadFromResource("org/apache/commons/codec/language/bm/nonExistentResource.txt", TEST_LANGUAGES);
    }

    /**
     * Test method loadFromResource with empty resource
     */
    @org.junit.Test
    public void testLoadFromResourceEmptyResource() {
        Lang instance = Lang.loadFromResource("org/apache/commons/codec/language/bm/emptyResource.txt", TEST_LANGUAGES);
        assertNotNull(instance);
        Set<String> result = instance.guessLanguages("testText");
        assertTrue("Expected empty Set as result", result.isEmpty());
    }

    /**
     * Test method loadFromResource with a rule that accepts a language
     */
    @org.junit.Test
    public void testLoadFromResourceAcceptOnMatch() {
        Lang instance = Lang.loadFromResource("org/apache/commons/codec/language/bm/langWithAcceptOnMatch.txt", TEST_LANGUAGES);
        Set<String> expected = new HashSet<>(Arrays.asList("German", "Dutch"));
        Set<String> result = instance.guessLanguages("testTextWithGermanDutchRules");
        assertEquals("Expected German and Dutch as possible matches", expected, result);
    }

    /**
     * Test method loadFromResource with a rule that excludes a language
     */
    @org.junit.Test
    public void testLoadFromResourceExcludeOnMatch() {
        Lang instance = Lang.loadFromResource("org/apache/commons/codec/language/bm/langWithExcludeOnMatch.txt", TEST_LANGUAGES);
        Set<String> expected = new HashSet<>();
        Set<String> result = instance.guessLanguages("testTextWithoutEnglishRules");
        assertEquals("Expected empty Set as result", expected, result);
    }
}