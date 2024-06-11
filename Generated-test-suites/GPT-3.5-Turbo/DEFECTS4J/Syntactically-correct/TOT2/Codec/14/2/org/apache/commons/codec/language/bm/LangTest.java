import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        
        // Typical use case
        String result = lang.guessLanguage("hello");
        assertEquals("english", result);
        
        // Edge cases
        result = lang.guessLanguage("");
        assertEquals("any", result);
        
        result = lang.guessLanguage(null);
        assertEquals("any", result);
        
        // Error scenarios
        result = lang.guessLanguage("12345");
        assertEquals("any", result);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SPANISH);
        
        // Typical use case
        Languages.LanguageSet result = lang.guessLanguages("hola");
        assertTrue(result.contains("spanish"));
        
        // Edge cases
        result = lang.guessLanguages("");
        assertTrue(result.equals(Languages.ANY_LANGUAGE));
        
        result = lang.guessLanguages(null);
        assertTrue(result.equals(Languages.ANY_LANGUAGE));
        
        // Error scenarios
        result = lang.guessLanguages("12345");
        assertTrue(result.equals(Languages.ANY_LANGUAGE));
    }

}