import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        
        // Typical use case
        assertEquals("English", lang.guessLanguage("hello"));
        
        // Edge case
        assertEquals("Any", lang.guessLanguage("12345"));
        
        // Error scenario
        assertEquals("Any", lang.guessLanguage(null));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SPANISH);
        
        // Typical use case
        assertEquals("Spanish", lang.guessLanguages("hola").getAny());
        
        // Edge case
        assertEquals("Any", lang.guessLanguages("12345").getAny());
        
        // Error scenario
        assertNotNull(lang.guessLanguages(null));
    }
}