import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
    
    //Test for guessLanguage method
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        assertEquals("eng", result);
        
        result = lang.guessLanguage("hola");
        assertEquals("spa", result);
        
        result = lang.guessLanguage("bonjour");
        assertEquals("fra", result);
    }
    
    //Test for guessLanguages method
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertTrue(result.contains("eng"));
        
        result = lang.guessLanguages("hola");
        assertTrue(result.contains("spa"));
        
        result = lang.guessLanguages("bonjour");
        assertTrue(result.contains("fra"));
    }
}