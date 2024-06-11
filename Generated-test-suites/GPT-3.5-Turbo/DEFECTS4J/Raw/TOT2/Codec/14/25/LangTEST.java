import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);

        // Typical use case
        assertEquals("english", lang.guessLanguage("hello"));

        // Edge case
        assertEquals("any", lang.guessLanguage(""));

        // Exception handling scenario
        try {
            lang.guessLanguage(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SPANISH);

        // Typical use case
        assertEquals("spanish", lang.guessLanguages("hola").getAny());

        // Edge case
        assertEquals(Languages.ANY_LANGUAGE, lang.guessLanguages(""));

        // Exception handling scenario
        try {
            lang.guessLanguages(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }
}