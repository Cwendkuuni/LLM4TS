import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.COMMON);
        String result = lang.guessLanguage("hello");
        assertEquals("en", result);
        result = lang.guessLanguage("bonjour");
        assertEquals("fr", result);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.COMMON);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertTrue(result.contains("en"));
        result = lang.guessLanguages("bonjour");
        assertTrue(result.contains("fr"));
    }

    @Test
    public void testGuessLanguageEdgeCases() {
        Lang lang = Lang.instance(NameType.COMMON);
        String result = lang.guessLanguage("");
        assertEquals(Languages.ANY, result);
        result = lang.guessLanguage("123");
        assertEquals(Languages.ANY, result);
    }

    @Test
    public void testGuessLanguagesEdgeCases() {
        Lang lang = Lang.instance(NameType.COMMON);
        Languages.LanguageSet result = lang.guessLanguages("");
        assertEquals(Languages.ANY_LANGUAGE, result);
        result = lang.guessLanguages("123");
        assertEquals(Languages.ANY_LANGUAGE, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceInvalidFile() {
        Lang.loadFromResource("invalid_file.txt", Languages.getInstance(NameType.COMMON));
    }
}