import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class LangTest {
    
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
    
    @Test
    public void testGuessLanguageNoMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        String result = lang.guessLanguage("foo");
        assertEquals("und", result);
    }
    
    @Test
    public void testGuessLanguagesNoMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        Languages.LanguageSet result = lang.guessLanguages("foo");
        assertFalse(result.contains("eng"));
        assertFalse(result.contains("spa"));
        assertFalse(result.contains("fra"));
    }
    
}