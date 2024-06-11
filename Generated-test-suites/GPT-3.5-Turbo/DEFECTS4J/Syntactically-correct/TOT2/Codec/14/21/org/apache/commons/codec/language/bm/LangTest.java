import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ORIGIN);

        // Typical use case
        String language = lang.guessLanguage("hello");
        assertEquals("English", language);

        // Edge cases
        language = lang.guessLanguage("");
        assertEquals("UNKNOWN", language);

        language = lang.guessLanguage(null);
        assertEquals("UNKNOWN", language);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.ORIGIN);

        // Typical use case
        Languages.LanguageSet languageSet = lang.guessLanguages("hello");
        assertTrue(languageSet.contains("English"));

        // Edge cases
        languageSet = lang.guessLanguages("");
        assertTrue(languageSet.isEmpty());

        languageSet = lang.guessLanguages(null);
        assertTrue(languageSet.isEmpty());
    }
}