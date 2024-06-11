import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
    
    private final Lang lang = Lang.instance(NameType.GENERIC_NAME);
    
    @Test
    public void testGuessLanguage() {
        String word = "Hello";
        String result = lang.guessLanguage(word);
        assertEquals(Languages.ANY, result);
    }
    
    @Test
    public void testGuessLanguages() {
        String word = "World";
        Languages.LanguageSet result = lang.guessLanguages(word);
        assertTrue(result.contains("en"));    
    }
    
    @Test
    public void testGuessLanguageCaseInsensitive() {
        String word = "hElLo";
        String result = lang.guessLanguage(word);
        assertEquals(Languages.ANY, result);
    }
    
    @Test
    public void testGuessLanguagesNoMatch() {
        String word = "Guten Tag";
        Languages.LanguageSet result = lang.guessLanguages(word);
        assertEquals(Languages.NO_LANGUAGES, result);
    }
    
    @Test
    public void testGuessLanguagesMultipleMatches() {
        String word = "Hola";
        Languages.LanguageSet result = lang.guessLanguages(word);
        assertTrue(result.contains("es"));
        assertTrue(result.contains("eu"));
    }
}