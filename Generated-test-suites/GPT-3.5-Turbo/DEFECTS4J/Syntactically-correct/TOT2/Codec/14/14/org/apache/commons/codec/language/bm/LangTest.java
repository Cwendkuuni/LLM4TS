import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.COMMON);

        // Typical use case
        String result = lang.guessLanguage("hello");
        assertEquals("eng", result);

        // Edge case: empty input
        result = lang.guessLanguage("");
        assertEquals("any", result);

        // Error scenario: null input
        try {
            lang.guessLanguage(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.COMMON);

        // Typical use case
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertEquals(1, result.size());
        assertTrue(result.contains("eng"));

        // Edge case: word with mixed case
        result = lang.guessLanguages("HeLLo");
        assertEquals(1, result.size());
        assertTrue(result.contains("eng"));

        // Error scenario: word with special characters
        result = lang.guessLanguages("hello!");
        assertEquals(0, result.size());
    }
}