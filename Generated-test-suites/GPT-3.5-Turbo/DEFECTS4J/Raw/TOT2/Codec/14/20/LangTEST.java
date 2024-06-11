import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LangTest {

    // Test for guessing the language of a word
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        
        assertEquals("English", lang.guessLanguage("hello"));
        assertEquals("French", lang.guessLanguage("bonjour"));
        assertEquals("German", lang.guessLanguage("hallo"));
    }

    // Test for guessing the languages of a word
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.ENGLISH);

        assertTrue(lang.guessLanguages("hello").contains("English"));
        assertTrue(lang.guessLanguages("bonjour").contains("French"));
        assertTrue(lang.guessLanguages("hallo").contains("German"));
    }

    // Test for edge cases and error scenarios
    @Test
    public void testEdgeCasesAndErrors() {
        Lang lang = Lang.instance(NameType.ENGLISH);

        // Empty input
        assertEquals("Any Language", lang.guessLanguage(""));
        assertTrue(lang.guessLanguages("").contains("English"));

        // Non-alphabetic characters
        assertEquals("Any Language", lang.guessLanguage("!@#$"));
        assertTrue(lang.guessLanguages("!@#$").contains("English"));

        // Null input
        assertEquals("Any Language", lang.guessLanguage(null));
        assertTrue(lang.guessLanguages(null).contains("English"));
    }
}