import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);

        // Typical use case
        assertEquals("english", lang.guessLanguage("hello"));

        // Edge case
        assertEquals("unknown", lang.guessLanguage("$$$"));

        // Exception handling scenario
        NullPointerException exception = assertThrows(NullPointerException.class, () -> lang.guessLanguage(null));
        assertEquals("text cannot be null", exception.getMessage());
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SPANISH);

        // Typical use case
        assertTrue(lang.guessLanguages("hola").isSingleton());

        // Edge case
        assertFalse(lang.guessLanguages("$$$").isSingleton());

        // Exception handling scenario
        NullPointerException exception = assertThrows(NullPointerException.class, () -> lang.guessLanguages(null));
        assertEquals("input cannot be null", exception.getMessage());
    }
}