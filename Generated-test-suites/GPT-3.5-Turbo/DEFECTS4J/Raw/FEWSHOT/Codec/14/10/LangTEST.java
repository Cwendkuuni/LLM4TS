import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LangTest {
    
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.EST);
        assertEquals("est", lang.guessLanguage("Tere"));
        assertEquals("unknown", lang.guessLanguage("Hello"));
        assertEquals("est", lang.guessLanguage("Tere, kuidas läheb?"));
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.EST);
        assertTrue(lang.guessLanguages("Tere").contains("est"));
        assertTrue(lang.guessLanguages("Hello").isEmpty());
        assertTrue(lang.guessLanguages("Tere, kuidas läheb?").contains("est"));
    }
    
    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.EST));
        assertEquals("est", lang.guessLanguage("Tere"));
        assertEquals("unknown", lang.guessLanguage("Hello"));
        assertEquals("est", lang.guessLanguage("Tere, kuidas läheb?"));
    }
}