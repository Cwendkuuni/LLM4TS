import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.SIMPLIFIED);
        
        // Typical use case
        assertEquals("en", lang.guessLanguage("hello"));
        
        // Edge case
        assertEquals("und", lang.guessLanguage("12345"));
        
        // Error scenario
        assertEquals("und", lang.guessLanguage(""));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SIMPLIFIED);
        
        // Typical use case
        assertEquals("en", lang.guessLanguages("hello").getAny());
        
        // Edge case
        assertEquals("und", lang.guessLanguages("12345").getAny());
        
        // Error scenario
        assertEquals("und", lang.guessLanguages("").getAny());
    }

}