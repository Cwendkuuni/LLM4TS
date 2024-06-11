
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("en", lang.guessLanguage("hello"));
        assertEquals("es", lang.guessLanguage("hola"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet ls = lang.guessLanguages("hello");
        assertTrue(ls.contains("en") && ls.size() == 1);
        
        ls = lang.guessLanguages("hola");
        assertTrue(ls.contains("es") && ls.size() == 1);
    }
}
