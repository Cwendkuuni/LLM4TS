import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String language = lang.guessLanguage("hello");
        Assert.assertEquals("eng", language);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet languages = lang.guessLanguages("hello");
        Assert.assertEquals("eng", languages.getAny());
    }

    @Test
    public void testGuessLanguage_EmptyString() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String language = lang.guessLanguage("");
        Assert.assertEquals("und", language);
    }

    @Test
    public void testGuessLanguages_EmptyString() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet languages = lang.guessLanguages("");
        Assert.assertEquals("und", languages.getAny());
    }

    @Test
    public void testGuessLanguage_Null() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String language = lang.guessLanguage(null);
        Assert.assertEquals("und", language);
    }

    @Test
    public void testGuessLanguages_Null() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet languages = lang.guessLanguages(null);
        Assert.assertEquals("und", languages.getAny());
    }

    @Test
    public void testGuessLanguage_Alphanumeric() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String language = lang.guessLanguage("123abc");
        Assert.assertEquals("und", language);
    }

    @Test
    public void testGuessLanguages_Alphanumeric() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet languages = lang.guessLanguages("123abc");
        Assert.assertEquals("und", languages.getAny());
    }

    @Test
    public void testGuessLanguage_SpecialCharacters() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String language = lang.guessLanguage("!@#$%^&*()");
        Assert.assertEquals("und", language);
    }

    @Test
    public void testGuessLanguages_SpecialCharacters() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet languages = lang.guessLanguages("!@#$%^&*()");
        Assert.assertEquals("und", languages.getAny());
    }
}