import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        String result1 = lang.guessLanguage("hello");
        assertEquals("eng", result1);
        
        String result2 = lang.guessLanguage("bonjour");
        assertEquals("fra", result2);
        
        String result3 = lang.guessLanguage("hola");
        assertEquals("spa", result3);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);

        Languages.LanguageSet result1 = lang.guessLanguages("hello");
        assertEquals("eng", result1.getAny());
        
        Languages.LanguageSet result2 = lang.guessLanguages("bonjour");
        assertEquals("fra", result2.getAny());
        
        Languages.LanguageSet result3 = lang.guessLanguages("hola");
        assertEquals("spa", result3.getAny());
    }
}