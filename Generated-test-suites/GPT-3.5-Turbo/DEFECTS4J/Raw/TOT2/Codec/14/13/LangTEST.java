import static org.junit.Assert.*;
import org.junit.Test;

public class LangTest {

    // Test for guessing language of a word
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals("english", lang.guessLanguage("hello"));
    }

    // Test for guessing languages of a word
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SPANISH);
        assertEquals("[spanish]", lang.guessLanguages("hola").toString());
    }

    // Test for handling edge case of empty text in guessLanguages method
    @Test
    public void testGuessLanguagesWithEmptyText() {
        Lang lang = Lang.instance(NameType.FRENCH);
        assertEquals("[french, german]", lang.guessLanguages("").toString());
    }

    // Test for handling unexpected input in guessLanguage method
    @Test(expected = NullPointerException.class)
    public void testGuessLanguageWithNullText() {
        Lang lang = Lang.instance(NameType.PORTUGUESE);
        lang.guessLanguage(null);
    }

    // Test for handling edge case of no unique match in guessLanguages
    @Test
    public void testGuessLanguagesWithNoUniqueMatch() {
        Lang lang = Lang.instance(NameType.RUSSIAN);
        assertEquals("[russian]", lang.guessLanguages("hello").toString());
    }
}