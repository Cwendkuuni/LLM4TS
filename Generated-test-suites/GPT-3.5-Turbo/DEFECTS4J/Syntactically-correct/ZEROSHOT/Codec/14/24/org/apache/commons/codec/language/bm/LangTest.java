import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("eng", lang.guessLanguage("hello"));
        assertEquals("spa", lang.guessLanguage("hola"));
        assertEquals("deu", lang.guessLanguage("hallo"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet languageSet = lang.guessLanguages("hello");
        assertEquals(1, languageSet.size());
        assertEquals("eng", languageSet.iterator().next());
    }
}