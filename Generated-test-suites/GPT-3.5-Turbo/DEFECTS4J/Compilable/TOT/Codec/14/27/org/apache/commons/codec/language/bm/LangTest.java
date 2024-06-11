package org.apache.commons.codec.language.bm;
import org.junit.Assert;
import org.junit.Test;

public class LangTest {
    // Test Case 1: Typical use case for guessLanguage()
    @Test
    public void testGuessLanguage_typical() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        Assert.assertEquals("en", result);
    }

    // Test Case 2: Typical use case for guessLanguages()
    @Test
    public void testGuessLanguages_typical() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        Assert.assertTrue(result.contains("en"));
    }

    // Test Case 3: Edge case for guessLanguage() with empty input
    @Test
    public void testGuessLanguage_empty() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("");
        Assert.assertEquals("und", result);
    }

    // Test Case 4: Error scenario for guessLanguages() with null input
    @Test(expected = NullPointerException.class)
    public void testGuessLanguages_null() {
        Lang lang = Lang.instance(NameType.GENERIC);
        lang.guessLanguages(null);
    }

    // Test Case 5: Edge case for guessLanguage() with no unique match
    @Test
    public void testGuessLanguage_noUniqueMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("xyz");
        Assert.assertEquals("und", result);
    }

    // Test Case 6: Edge case for guessLanguages() with no matches
    @Test
    public void testGuessLanguages_noMatches() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("xyz");
        Assert.assertEquals(result, Languages.NO_LANGUAGES);
    }
}