import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.COMMON);
        assertEquals("english", lang.guessLanguage("hello world"));
        assertEquals("french", lang.guessLanguage("bonjour"));
        assertEquals("spanish", lang.guessLanguage("hola"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.COMMON);
        assertEquals(Languages.NO_LANGUAGES, lang.guessLanguages("12345"));
        assertEquals(Languages.ANY_LANGUAGE, lang.guessLanguages("hello world"));
    }

    @Test
    public void testInvalidLanguageRule() {
        Lang lang = Lang.instance(NameType.COMMON);
        assertEquals(Languages.ANY, lang.guessLanguage("12345"));
    }

    // Edge cases and error scenarios can be added here
}