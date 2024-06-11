package org.apache.commons.codec.language.bm;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    private static Lang lang;

    @BeforeClass
    public static void setUp() {
        lang = Lang.instance(NameType.GENERIC);
    }

    @Test
    public void testGuessLanguage() {
        String text = "hello";
        String language = lang.guessLanguage(text);
        assertEquals("eng", language);
    }

    @Test
    public void testGuessLanguages() {
        String text = "hello";
        Languages.LanguageSet languageSet = lang.guessLanguages(text);
        assertTrue(languageSet.contains("eng"));
    }

    @Test
    public void testGuessLanguage_NotFound() {
        String text = "xyz";
        String language = lang.guessLanguage(text);
        assertEquals("und", language);
    }

    @Test
    public void testGuessLanguages_NotFound() {
        String text = "xyz";
        Languages.LanguageSet languageSet = lang.guessLanguages(text);
        assertFalse(languageSet.contains("eng"));
        assertFalse(languageSet.contains("spa"));
        assertFalse(languageSet.contains("fra"));
    }
}