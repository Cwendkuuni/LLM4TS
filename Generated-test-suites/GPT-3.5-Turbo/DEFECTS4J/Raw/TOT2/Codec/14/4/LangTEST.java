import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGINE);
        
        // Typical use case
        String typicalWord = "hello";
        String typicalLanguage = lang.guessLanguage(typicalWord);
        assertEquals("en", typicalLanguage);
        
        // Edge case
        String edgeWord = "123";
        String edgeLanguage = lang.guessLanguage(edgeWord);
        assertEquals(Languages.ANY, edgeLanguage);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.ENGINE);
        
        // Typical use case
        String typicalWord = "world";
        Languages.LanguageSet typicalLanguages = lang.guessLanguages(typicalWord);
        assertTrue(typicalLanguages.getLanguages().contains("en"));
        
        // Edge case
        String edgeWord = "1234";
        Languages.LanguageSet edgeLanguages = lang.guessLanguages(edgeWord);
        assertEquals(Languages.ANY_LANGUAGE, edgeLanguages);
    }
}