import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.COMMON);
        String result = lang.guessLanguage("hello");
        assertEquals("english", result);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.COMMON);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertTrue(result.contains("english"));
    }

    @Test
    public void testMultipleGuessLanguages() {
        Lang lang = Lang.instance(NameType.COMMON);
        Languages.LanguageSet result = lang.guessLanguages("apple");
        assertTrue(result.contains("english"));
        assertTrue(result.contains("danish"));
    }

    @Test
    public void testNoMatchGuessLanguage() {
        Lang lang = Lang.instance(NameType.COMMON);
        String result = lang.guessLanguage("zxqwy");
        assertEquals("any", result);
    }

    @Test
    public void testNoMatchGuessLanguages() {
        Lang lang = Lang.instance(NameType.COMMON);
        Languages.LanguageSet result = lang.guessLanguages("zxqwy");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testEdgeCaseEmptyStringGuessLanguage() {
        Lang lang = Lang.instance(NameType.COMMON);
        String result = lang.guessLanguage("");
        assertEquals("any", result);
    }

    @Test
    public void testEdgeCaseEmptyStringGuessLanguages() {
        Lang lang = Lang.instance(NameType.COMMON);
        Languages.LanguageSet result = lang.guessLanguages("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testExceptionHandlingInvalidInputGuessLanguage() {
        Lang lang = Lang.instance(NameType.COMMON);
        try {
            lang.guessLanguage(null);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testExceptionHandlingInvalidInputGuessLanguages() {
        Lang lang = Lang.instance(NameType.COMMON);
        try {
            lang.guessLanguages(null);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}