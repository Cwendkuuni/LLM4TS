import static org.junit.Assert.*;
import org.junit.Test;

public class LangTest {
 
    @Test
    public void testGuessLanguage() {
        Lang lang = new Lang();
        String result = lang.guessLanguage("hello");
        assertEquals("eng", result);
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = new Lang();
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertEquals(1, result.size());
        assertTrue(result.contains("eng"));
    }
    
    @Test
    public void testGuessLanguageEdgeCase1() {
        Lang lang = new Lang();
        String result = lang.guessLanguage("");
        assertEquals("any", result);
    }
    
    @Test
    public void testGuessLanguagesEdgeCase1() {
        Lang lang = new Lang();
        Languages.LanguageSet result = lang.guessLanguages("");
        assertEquals(1, result.size());
        assertTrue(result.contains("any"));
    }
    
    @Test
    public void testGuessLanguageEdgeCase2() {
        Lang lang = new Lang();
        String result = lang.guessLanguage("h#@$ello");
        assertEquals("eng", result);
    }
    
    @Test
    public void testGuessLanguagesEdgeCase2() {
        Lang lang = new Lang();
        Languages.LanguageSet result = lang.guessLanguages("h#@$ello");
        assertEquals(1, result.size());
        assertTrue(result.contains("eng"));
    }
    
    @Test
    public void testGuessLanguageEdgeCase3() {
        Lang lang = new Lang();
        String result = lang.guessLanguage("123");
        assertEquals("any", result);
    }
    
    @Test
    public void testGuessLanguagesEdgeCase3() {
        Lang lang = new Lang();
        Languages.LanguageSet result = lang.guessLanguages("123");
        assertEquals(1, result.size());
        assertTrue(result.contains("any"));
    }
}