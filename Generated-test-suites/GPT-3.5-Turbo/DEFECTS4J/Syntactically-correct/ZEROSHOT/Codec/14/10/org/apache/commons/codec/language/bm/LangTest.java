import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage_WhenWordIsNull_ReturnsAny() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage(null);
        assertEquals(Languages.ANY, result);
    }

    @Test
    public void testGuessLanguage_WhenWordIsEmpty_ReturnsAny() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("");
        assertEquals(Languages.ANY, result);
    }

    @Test
    public void testGuessLanguage_WhenWordDoesNotMatchAnyRule_ReturnsAny() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("abcdef");
        assertEquals(Languages.ANY, result);
    }

    @Test
    public void testGuessLanguage_WhenWordMatchesOneRule_ReturnsCorrectLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        assertEquals("en", result);
    }

    @Test
    public void testGuessLanguage_WhenWordMatchesMultipleRules_ReturnsAny() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("despedida");
        assertEquals(Languages.ANY, result);
    }

    @Test
    public void testGuessLanguages_WhenWordIsNull_ReturnsAnyLanguageSet() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages(null);
        assertEquals(Languages.ANY_LANGUAGE, result);
    }

    @Test
    public void testGuessLanguages_WhenWordIsEmpty_ReturnsAnyLanguageSet() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("");
        assertEquals(Languages.ANY_LANGUAGE, result);
    }

    @Test
    public void testGuessLanguages_WhenWordDoesNotMatchAnyRule_ReturnsAnyLanguageSet() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("abcdef");
        assertEquals(Languages.ANY_LANGUAGE, result);
    }

    @Test
    public void testGuessLanguages_WhenWordMatchesOneRule_ReturnsLanguageSetWithCorrectLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertEquals("en", result.getAny());
        assertTrue(result.isSingleton());
    }

    @Test
    public void testGuessLanguages_WhenWordMatchesMultipleRules_ReturnsAnyLanguageSet() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("despedida");
        assertEquals(Languages.ANY_LANGUAGE, result);
    }
}