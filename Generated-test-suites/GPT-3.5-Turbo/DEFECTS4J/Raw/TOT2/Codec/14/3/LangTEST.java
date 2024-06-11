
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        
        // typical use case
        assertEquals("english", lang.guessLanguage("hello"));
        
        // edge case
        assertEquals("unknown", lang.guessLanguage(""));
        
        // error scenario
        assertEquals(Languages.ANY, lang.guessLanguage(null));
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.FRENCH);
        
        // typical use case
        assertEquals(Languages.LanguageSet.from("french"), lang.guessLanguages("bonjour"));
        
        // edge case
        assertEquals(Languages.NO_LANGUAGES, lang.guessLanguages("!@#"));
        
        // error scenario
        assertEquals(Languages.ANY_LANGUAGE, lang.guessLanguages(null));
    }
}
