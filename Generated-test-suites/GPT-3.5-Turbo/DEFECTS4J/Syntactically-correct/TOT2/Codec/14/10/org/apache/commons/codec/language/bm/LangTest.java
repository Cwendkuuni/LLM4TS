
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.NATIVE);
        
        assertEquals("eng", lang.guessLanguage("hello"));
        assertEquals("spa", lang.guessLanguage("hola"));
        assertEquals("fra", lang.guessLanguage("bonjour"));
        assertEquals("ita", lang.guessLanguage("ciao"));
        assertEquals("deu", lang.guessLanguage("hallo"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.NATIVE);
        
        assertEquals("eng", lang.guessLanguages("hello world").getAny());
        assertEquals("cmn", lang.guessLanguages("你好世界").getAny());
        assertEquals("jpn", lang.guessLanguages("こんにちは世界").getAny());
        assertEquals("fra", lang.guessLanguages("bonjour le monde").getAny());
        assertEquals("spa", lang.guessLanguages("hola mundo").getAny());
    }
}
