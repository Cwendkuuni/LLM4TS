import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage_validInput_returnLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("Hello");
        Assert.assertEquals("en", result);
    }

    @Test
    public void testGuessLanguage_emptyInput_returnAnyLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result1 = lang.guessLanguage("");
        String result2 = lang.guessLanguage("   ");
        Assert.assertEquals("und", result1);
        Assert.assertEquals("und", result2);
    }

    @Test
    public void testGuessLanguage_invalidInput_returnAnyLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("123");
        Assert.assertEquals("und", result);
    }

    @Test
    public void testGuessLanguages_validInput_returnLanguageSet() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("Hello");
        Assert.assertEquals("en", result.getAny());
    }

    @Test
    public void testGuessLanguages_emptyInput_returnAnyLanguageSet() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result1 = lang.guessLanguages("");
        Languages.LanguageSet result2 = lang.guessLanguages("   ");
        Assert.assertEquals(Languages.ANY_LANGUAGE, result1);
        Assert.assertEquals(Languages.ANY_LANGUAGE, result2);
    }

    @Test
    public void testGuessLanguages_invalidInput_returnAnyLanguageSet() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("123");
        Assert.assertEquals(Languages.ANY_LANGUAGE, result);
    }
}