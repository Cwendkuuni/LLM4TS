import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Typical use case
        assertEquals("english", lang.guessLanguage("hello"));

        // Edge cases
        assertEquals("any", lang.guessLanguage(""));
        assertEquals("any", lang.guessLanguage(null));

        // Error scenarios
        // Add any error scenarios here
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Typical use case
        assertEquals(new Languages.LanguageSet("english"), lang.guessLanguages("hello"));

        // Edge cases
        assertEquals(Languages.ANY_LANGUAGE, lang.guessLanguages(""));
        assertEquals(Languages.ANY_LANGUAGE, lang.guessLanguages(null));

        // Error scenarios
        // Add any error scenarios here
    }
}