import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals("english", lang.guessLanguage("hello"));
        assertEquals("english", lang.guessLanguage("world"));
        assertEquals("english", lang.guessLanguage("programming"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SPANISH);
        assertEquals(Languages.LanguageSet.from("spanish"), lang.guessLanguages("hola"));
        assertEquals(Languages.LanguageSet.from("spanish"), lang.guessLanguages("mundo"));
        assertEquals(Languages.LanguageSet.from("spanish"), lang.guessLanguages("programación"));
    }

    @Test
    public void testGuessLanguageEdgeCases() {
        Lang lang = Lang.instance(NameType.GERMAN);
        assertEquals("german", lang.guessLanguage("Hallo"));
        assertEquals("german", lang.guessLanguage("Welt"));
        assertEquals("german", lang.guessLanguage("Programmierung"));
    }

    @Test
    public void testGuessLanguagesEdgeCases() {
        Lang lang = Lang.instance(NameType.FRENCH);
        assertEquals(Languages.LanguageSet.from("french"), lang.guessLanguages("bonjour"));
        assertEquals(Languages.LanguageSet.from("french"), lang.guessLanguages("monde"));
        assertEquals(Languages.LanguageSet.from("french"), lang.guessLanguages("programmation"));
    }

    @Test
    public void testGuessLanguageErrorScenarios() {
        Lang lang = Lang.instance(NameType.PORTUGUESE);
        assertEquals(Languages.ANY, lang.guessLanguage(""));
        assertEquals(Languages.ANY, lang.guessLanguage(null));
    }

}