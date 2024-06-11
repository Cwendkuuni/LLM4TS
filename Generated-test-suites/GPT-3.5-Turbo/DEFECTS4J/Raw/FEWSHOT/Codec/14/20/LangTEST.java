import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = new Lang(null, null);
        String result = lang.guessLanguage("hello");
        assertEquals(Languages.ANY, result);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = new Lang(null, null);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertEquals(Languages.ANY_LANGUAGE, result);
    }

}