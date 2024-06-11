import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Typical use cases
        assertEquals("eng", lang.guessLanguage("hello"));
        assertEquals("fra", lang.guessLanguage("bonjour"));
        assertEquals("spa", lang.guessLanguage("hola"));

        // Edge cases
        assertEquals("eng", lang.guessLanguage(""));
        assertEquals("eng", lang.guessLanguage(" "));
        assertEquals("eng", lang.guessLanguage("123"));

        // Error scenarios
        assertEquals("any", lang.guessLanguage(null));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Typical use cases
        assertArrayEquals(new String[]{"eng"}, lang.guessLanguages("hello").getLanguages());
        assertArrayEquals(new String[]{"fra"}, lang.guessLanguages("bonjour").getLanguages());
        assertArrayEquals(new String[]{"spa"}, lang.guessLanguages("hola").getLanguages());

        // Edge cases
        assertArrayEquals(new String[]{"eng"}, lang.guessLanguages("").getLanguages());
        assertArrayEquals(new String[]{"eng"}, lang.guessLanguages(" ").getLanguages());
        assertArrayEquals(new String[]{"eng"}, lang.guessLanguages("123").getLanguages());

        // Error scenarios
        assertArrayEquals(new String[0], lang.guessLanguages(null).getLanguages());
    }
}