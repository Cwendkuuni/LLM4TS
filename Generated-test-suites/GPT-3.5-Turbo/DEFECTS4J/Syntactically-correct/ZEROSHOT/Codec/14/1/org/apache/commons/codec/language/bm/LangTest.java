import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        assertEquals("eng", result);

        result = lang.guessLanguage("hola");
        assertEquals("spa", result);

        lang = Lang.instance(NameType.NAME);
        result = lang.guessLanguage("John");
        assertEquals("eng", result);

        result = lang.guessLanguage("Juan");
        assertEquals("spa", result);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertTrue(result.contains("eng"));

        result = lang.guessLanguages("hola");
        assertTrue(result.contains("spa"));

        lang = Lang.instance(NameType.NAME);
        result = lang.guessLanguages("John");
        assertTrue(result.contains("eng"));

        result = lang.guessLanguages("Juan");
        assertTrue(result.contains("spa"));
    }
}