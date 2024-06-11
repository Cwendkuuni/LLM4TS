import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.COMMON);
        
        // Typical use case
        assertEquals("english", lang.guessLanguage("hello"));
        
        // Edge case
        assertEquals("german", lang.guessLanguage("Guten Tag"));
        
        // Error scenario
        assertEquals(Languages.ANY, lang.guessLanguage("12345"));
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.COMMON);
        
        // Typical use case
        assertEquals(Languages.LanguageSet.from("english"), lang.guessLanguages("hello"));
        
        // Edge case
        assertEquals(Languages.LanguageSet.from("german"), lang.guessLanguages("Guten Tag"));
        
        // Error scenario
        assertEquals(Languages.ANY_LANGUAGE, lang.guessLanguages("12345"));
    }
}