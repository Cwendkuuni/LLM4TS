import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;

public class LangTest {
    
    private Lang lang;
    
    @Before
    public void setUp() {
        lang = Lang.instance(Languages.NAME_TYPE);
    }
    
    // Test for guessLanguage method
    
    @Test
    public void testGuessLanguage() {
        // Typical use case
        String language = lang.guessLanguage("hello");
        assertEquals("eng", language);
        
        // Edge case: Empty text
        language = lang.guessLanguage("");
        assertEquals("und", language);
        
        // Edge case: Text with numbers
        language = lang.guessLanguage("12345");
        assertEquals("und", language);
        
        // Error scenario: Text with special characters
        language = lang.guessLanguage("!@#$%^&*()");
        assertEquals("und", language);
    }
    
    // Test for guessLanguages method
    
    @Test
    public void testGuessLanguages() {
        // Typical use case
        Languages.LanguageSet languageSet = lang.guessLanguages("hello");
        assertTrue(languageSet.contains("eng"));
        
        // Edge case: Empty text
        languageSet = lang.guessLanguages("");
        assertTrue(languageSet.isEmpty());
        
        // Edge case: Text with numbers
        languageSet = lang.guessLanguages("12345");
        assertTrue(languageSet.isEmpty());
        
        // Error scenario: Text with special characters
        languageSet = lang.guessLanguages("!@#$%^&*()");
        assertTrue(languageSet.isEmpty());
    }
    
}