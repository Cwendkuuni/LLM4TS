import static org.junit.Assert.*;

import org.junit.Test;

public class LangTest {
    
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        
        // Typical use case
        assertEquals("English", lang.guessLanguage("hello"));
        
        // Edge case
        assertEquals("unknown", lang.guessLanguage(""));
        
        // Error scenario
        assertEquals("unknown", lang.guessLanguage(null));
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.FRENCH);
        
        // Typical use case
        assertEquals(Languages.LanguageSet.from(new HashSet<>(Collections.singletonList("fra"))), lang.guessLanguages("bonjour"));
        
        // Edge case
        assertEquals(Languages.NO_LANGUAGES, lang.guessLanguages(""));
        
        // Error scenario
        assertEquals(Languages.NO_LANGUAGES, lang.guessLanguages(null));
    }
}