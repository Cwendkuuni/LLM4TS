import static org.junit.Assert.*;
import org.junit.Test;

public class LangTest {
    
    @Test
    public void testGuessLanguage_typicalUseCase() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        String text = "hello";
        String expectedLanguage = "en";
        
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }
    
    @Test
    public void testGuessLanguage_edgeCase() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        String text = "";
        String expectedLanguage = "und";
        
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }
    
    @Test
    public void testGuessLanguage_errorScenario() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        String text = null;
        String expectedLanguage = "und";
        
        assertEquals(expectedLanguage, lang.guessLanguage(text));
    }
    
    @Test
    public void testGuessLanguages_typicalUseCase() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        String text = "goodbye";
        Languages.LanguageSet expectedLanguages = Languages.ENGLISH;
        
        assertEquals(expectedLanguages, lang.guessLanguages(text));
    }
    
    @Test
    public void testGuessLanguages_edgeCase() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        String text = "";
        Languages.LanguageSet expectedLanguages = Languages.NO_LANGUAGES;
        
        assertEquals(expectedLanguages, lang.guessLanguages(text));
    }
    
    @Test
    public void testGuessLanguages_errorScenario() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        String text = null;
        Languages.LanguageSet expectedLanguages = Languages.NO_LANGUAGES;
        
        assertEquals(expectedLanguages, lang.guessLanguages(text));
    }
}