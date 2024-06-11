import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class LangTest {
    private static Lang langInstance;
    
    @BeforeClass
    public static void setUpBeforeClass() {
        langInstance = Lang.instance(NameType.GENERIC);
    }
    
    @Test
    public void testGuessLanguage_typical() {
        assertEquals("eng", langInstance.guessLanguage("hello"));
        assertEquals("fra", langInstance.guessLanguage("bonjour"));
        assertEquals("deu", langInstance.guessLanguage("hallo"));
    }
    
    @Test
    public void testGuessLanguage_edgeCases() {
        // Word with numbers
        assertEquals("jpn", langInstance.guessLanguage("ありがとう123"));
        
        // Word with special characters
        assertEquals("spa", langInstance.guessLanguage("¡Hola!"));
        
        // Empty word
        assertEquals("und", langInstance.guessLanguage(""));
    }
    
    @Test
    public void testGuessLanguage_errorScenarios() {
        // Word not in any supported language
        assertEquals("und", langInstance.guessLanguage("xyz"));
        
        // Word with no characters
        assertEquals("und", langInstance.guessLanguage("123"));
    }
    
    @Test
    public void testGuessLanguages_typical() {
        Languages.LanguageSet expectedSet1 = Languages.LanguageSet.from("eng");
        assertEquals(expectedSet1, langInstance.guessLanguages("hello"));
        
        Languages.LanguageSet expectedSet2 = Languages.LanguageSet.from("fra");
        assertEquals(expectedSet2, langInstance.guessLanguages("bonjour"));
        
        Languages.LanguageSet expectedSet3 = Languages.LanguageSet.from("deu");
        assertEquals(expectedSet3, langInstance.guessLanguages("hallo"));
    }
    
    @Test
    public void testGuessLanguages_edgeCases() {
        // Word with numbers
        Languages.LanguageSet expectedSet1 = Languages.LanguageSet.from("jpn");
        assertEquals(expectedSet1, langInstance.guessLanguages("ありがとう123"));
        
        // Word with special characters
        Languages.LanguageSet expectedSet2 = Languages.LanguageSet.from("spa");
        assertEquals(expectedSet2, langInstance.guessLanguages("¡Hola!"));
        
        // Empty word
        Languages.LanguageSet expectedSet3 = Languages.LanguageSet.from("und");
        assertEquals(expectedSet3, langInstance.guessLanguages(""));
    }
    
    @Test
    public void testGuessLanguages_errorScenarios() {
        // Word not in any supported language
        Languages.LanguageSet expectedSet1 = Languages.NO_LANGUAGES;
        assertEquals(expectedSet1, langInstance.guessLanguages("xyz"));
        
        // Word with no characters
        Languages.LanguageSet expectedSet2 = Languages.LanguageSet.from("und");
        assertEquals(expectedSet2, langInstance.guessLanguages("123"));
    }

}