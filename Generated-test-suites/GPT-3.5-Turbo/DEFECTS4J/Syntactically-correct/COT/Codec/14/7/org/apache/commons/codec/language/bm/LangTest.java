import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String language = lang.guessLanguage("hello");
        assertNotEquals(Languages.UNKNOWN_LANGUAGE, language);
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet languageSet = lang.guessLanguages("hello");
        assertNotNull(languageSet);
        assertNotEquals(Languages.NO_LANGUAGES, languageSet);
    }
}