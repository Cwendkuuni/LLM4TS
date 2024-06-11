import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("eng", lang.guessLanguage("hello"));
        assertEquals("deu", lang.guessLanguage("Hallo"));
        assertEquals("fra", lang.guessLanguage("bonjour"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet ls = lang.guessLanguages("hello");
        assertTrue(ls.contains("eng"));
        assertTrue(ls.contains("cmn"));
        assertTrue(ls.contains("nld"));
    }
}