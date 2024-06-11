import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LangTest {

    @Test
    public void testGuessLanguage_ValidInput() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String result = lang.guessLanguage("Bonjour");
        assertEquals("fr", result);

        result = lang.guessLanguage("Hola");
        assertEquals("es", result);
    }

    @Test
    public void testGuessLanguage_InvalidInput() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String result = lang.guessLanguage("");
        assertEquals("und", result);

        result = lang.guessLanguage("123");
        assertEquals("und", result);
    }

    @Test
    public void testGuessLanguages_ValidInput() {
        Lang lang = Lang.instance(NameType.GENERIC);

        Languages.LanguageSet result = lang.guessLanguages("Hello");
        assertEquals("eng", result.getAny());

        result = lang.guessLanguages("Hola");
        assertEquals("spa", result.getAny());
    }

    @Test
    public void testGuessLanguages_InvalidInput() {
        Lang lang = Lang.instance(NameType.GENERIC);

        Languages.LanguageSet result = lang.guessLanguages("");
        assertEquals(true, result.isEmpty());

        result = lang.guessLanguages("123");
        assertEquals(true, result.isEmpty());
    }
}