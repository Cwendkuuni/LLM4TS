import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("en", lang.guessLanguage("Hello"));
        assertEquals("es", lang.guessLanguage("Hola"));
        assertEquals("fr", lang.guessLanguage("Bonjour"));
        assertEquals("de", lang.guessLanguage("Guten Tag"));
        assertEquals("it", lang.guessLanguage("Ciao"));
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertNotNull(lang.guessLanguages("Hello"));
        assertNotNull(lang.guessLanguages("Hola"));
        assertNotNull(lang.guessLanguages("Bonjour"));
        assertNotNull(lang.guessLanguages("Guten Tag"));
        assertNotNull(lang.guessLanguages("Ciao"));
    }
}