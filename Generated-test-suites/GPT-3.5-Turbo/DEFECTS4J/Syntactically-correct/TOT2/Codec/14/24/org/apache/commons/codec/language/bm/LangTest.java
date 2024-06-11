import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);

        // Typical use case
        assertEquals("english", lang.guessLanguage("hello"));

        // Edge cases
        assertEquals("english", lang.guessLanguage(""));
        assertEquals("english", lang.guessLanguage("123"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SPANISH);

        // Typical use case
        assertEquals("spanish", lang.guessLanguage("hola"));

        // Edge cases
        assertEquals("spanish", lang.guessLanguage(""));
        assertEquals("spanish", lang.guessLanguage("123"));
    }

    @Test
    public void testUnknownLanguages() {
        Lang lang = Lang.instance(NameType.UNKNOWN);

        // Error scenario
        assertEquals("unknown", lang.guessLanguage("test"));
    }
}