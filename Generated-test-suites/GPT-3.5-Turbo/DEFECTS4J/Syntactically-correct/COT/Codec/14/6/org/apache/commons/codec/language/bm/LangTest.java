
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("English", lang.guessLanguage("hello"));
        assertEquals("French", lang.guessLanguage("bonjour"));
        assertEquals("German", lang.guessLanguage("guten tag"));
        assertEquals("Spanish", lang.guessLanguage("hola"));
        assertEquals("Italian", lang.guessLanguage("ciao"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals(Languages.getInstance(NameType.GENERIC).getLanguageSet("english"), lang.guessLanguages("hello"));
        assertEquals(Languages.getInstance(NameType.GENERIC).getLanguageSet("french"), lang.guessLanguages("bonjour"));
        assertEquals(Languages.getInstance(NameType.GENERIC).getLanguageSet("german"), lang.guessLanguages("guten tag"));
        assertEquals(Languages.getInstance(NameType.GENERIC).getLanguageSet("spanish"), lang.guessLanguages("hola"));
        assertEquals(Languages.getInstance(NameType.GENERIC).getLanguageSet("italian"), lang.guessLanguages("ciao"));
    }

    @Test
    public void testGuessLanguageNoMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("?", lang.guessLanguage("xyz"));
    }

    @Test
    public void testGuessLanguagesNoMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals(Languages.NO_LANGUAGES, lang.guessLanguages("xyz"));
    }
}

