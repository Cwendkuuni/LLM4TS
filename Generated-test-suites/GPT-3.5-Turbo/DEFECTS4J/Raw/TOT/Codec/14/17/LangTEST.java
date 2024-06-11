import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Typical use case
        assertEquals("english", lang.guessLanguage("Hello"));
        
        // Edge case: Empty input
        assertEquals("any", lang.guessLanguage(""));
        
        // Error scenario: Null input
        assertNull(lang.guessLanguage(null));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Typical use case
        Languages.LanguageSet languageSet = lang.guessLanguages("Hello");
        assertTrue(languageSet.contains("english"));
        
        // Edge case: Empty input
        languageSet = lang.guessLanguages("");
        assertTrue(languageSet.isAny());
        
        // Error scenario: Null input
        languageSet = lang.guessLanguages(null);
        assertTrue(languageSet.isEmpty());
    }
}