import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("eng", lang.guessLanguage("hello"));
        assertEquals("spa", lang.guessLanguage("hola"));
        assertEquals("fra", lang.guessLanguage("bonjour"));
        assertEquals("deu", lang.guessLanguage("hallo"));
        assertEquals("ita", lang.guessLanguage("ciao"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("eng", lang.guessLanguages("hello").getAny());
        assertEquals("spa", lang.guessLanguages("hola").getAny());
        assertEquals("fra", lang.guessLanguages("bonjour").getAny());
        assertEquals("deu", lang.guessLanguages("hallo").getAny());
        assertEquals("ita", lang.guessLanguages("ciao").getAny());
    }
}