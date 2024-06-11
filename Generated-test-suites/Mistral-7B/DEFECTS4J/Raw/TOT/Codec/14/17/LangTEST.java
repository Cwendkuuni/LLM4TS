package org.apache.commons.codec.language.bm;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Lang class.
 */
public class LangTest {
    @Test
    public void testEnglishLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String englishText = "The quick brown fox jumps over the lazy dog.";
        String expectedLanguage = Languages.ENGLISH;
        String language = lang.guessLanguage(englishText);
        assertTrue(language.equals(expectedLanguage));
    }

    @Test
    public void testNonEnglishLanguage() {
        Lang lang = Lang.instance(NameType.GERMAN);
        String germanText = "Der schnelle rote Hund springt über den faulen grünen Esel.";
        String expectedLanguage = Languages.GERMAN;
        String language = lang.guessLanguage(germanText);
        assertTrue(language.equals(expectedLanguage));
    }

    @Test
    public void testMixedLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String mixedText = "The quick brown fox jumps over the lazy dog. Der Hund springt.";
        List<String> expectedLanguages = Arrays.asList(Languages.ENGLISH, Languages.GERMAN);
        Languages.LanguageSet languages = lang.guessLanguages(mixedText);
        assertTrue(languages.containsAll(expectedLanguages));
    }

    @Test
    public void testEmptyText() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String emptyText = "";
        String language = lang.guessLanguage(emptyText);
        assertEquals(Languages.ANY, language);
    }

    @Test
    public void testNullText() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = null;
        String language = lang.guessLanguage(text);
        assertEquals(Languages.ANY, language);
    }

    @Test
    public void testUnknownText() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String unknownText = "ഇത്തന്നുവന്നത്തിന്റെ വായ്ക്കനവുമറ്റുന്ന് മ്യോജാനായിൻോപ്പെട്ട് കുട്ടിയത്തിന്റെ മൊന്ത്രത്തിന്റെ കല്ലാത്തില്ല" ;
        String language = lang.guessLanguage(unknownText);
        assertEquals(Languages.ANY, language);
    }
}