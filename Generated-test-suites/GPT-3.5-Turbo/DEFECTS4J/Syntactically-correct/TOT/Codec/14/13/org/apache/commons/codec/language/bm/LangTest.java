import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LangTest {

    private Lang lang;

    @Before
    public void setUp() {
        lang = Lang.instance(NameType.GENERIC);
    }

    @Test
    public void testGuessLanguage_WithValidWord_ReturnsCorrectLanguage() {
        String word = "hello";
        String expectedLanguage = "en";
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguage_WithEmptyString_ReturnsAnyLanguage() {
        String word = "";
        String expectedLanguage = Languages.ANY;
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguage_WithNullString_ReturnsAnyLanguage() {
        String word = null;
        String expectedLanguage = Languages.ANY;
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguages_WithValidWord_ReturnsCorrectLanguageSet() {
        String word = "hello";
        Languages.LanguageSet expectedLanguageSet = Languages.LanguageSet.from("en");
        Languages.LanguageSet actualLanguageSet = lang.guessLanguages(word);
        assertEquals(expectedLanguageSet, actualLanguageSet);
    }

    @Test
    public void testGuessLanguages_WithEmptyString_ReturnsAnyLanguageSet() {
        String word = "";
        Languages.LanguageSet expectedLanguageSet = Languages.ANY_LANGUAGE;
        Languages.LanguageSet actualLanguageSet = lang.guessLanguages(word);
        assertEquals(expectedLanguageSet, actualLanguageSet);
    }

    @Test
    public void testGuessLanguages_WithNullString_ReturnsAnyLanguageSet() {
        String word = null;
        Languages.LanguageSet expectedLanguageSet = Languages.ANY_LANGUAGE;
        Languages.LanguageSet actualLanguageSet = lang.guessLanguages(word);
        assertEquals(expectedLanguageSet, actualLanguageSet);
    }

    @Test
    public void testLoadFromResource_WithValidResource_ReturnsNonNullValue() {
        String resource = "org/apache/commons/codec/language/bm/lang.txt";
        Lang loadedLang = Lang.loadFromResource(resource, Languages.getInstance(NameType.GENERIC));
        assertNotNull(loadedLang);
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResource_WithInvalidResource_ThrowsIllegalStateException() {
        String resource = "invalid/resource.txt";
        Lang.loadFromResource(resource, Languages.getInstance(NameType.GENERIC));
    }
}