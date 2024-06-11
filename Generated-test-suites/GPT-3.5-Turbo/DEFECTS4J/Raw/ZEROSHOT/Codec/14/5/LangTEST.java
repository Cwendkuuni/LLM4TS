import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.LANGUAGE);
        String result = lang.guessLanguage("Hello");
        assertEquals("eng", result);

        result = lang.guessLanguage("Hola");
        assertEquals("spa", result);

        result = lang.guessLanguage("Bonjour");
        assertEquals("fra", result);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.LANGUAGE);
        Languages.LanguageSet result = lang.guessLanguages("Hello");
        Languages.LanguageSet expected = Languages.LanguageSet.from("eng");
        assertEquals(expected, result);

        result = lang.guessLanguages("Hola");
        expected = Languages.LanguageSet.from("spa");
        assertEquals(expected, result);

        result = lang.guessLanguages("Bonjour");
        expected = Languages.LanguageSet.from("fra");
        assertEquals(expected, result);
    }
}