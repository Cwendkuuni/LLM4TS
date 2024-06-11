import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
  
    private Lang langInstance;
    
    @Before
    public void setUp() {
        langInstance = Lang.instance(NameType.GENERIC);
    }
    
    @Test
    public void testGuessLanguage_TypicalCase() {
        String text = "hello";
        String expectedLanguage = "en";
        
        String guessedLanguage = langInstance.guessLanguage(text);
        
        assertEquals(expectedLanguage, guessedLanguage);
    }
    
    @Test
    public void testGuessLanguage_NoUniqueMatch() {
        String text = "abc";
        String expectedLanguage = "und";
        
        String guessedLanguage = langInstance.guessLanguage(text);
        
        assertEquals(expectedLanguage, guessedLanguage);
    }
    
    @Test
    public void testGuessLanguages_TypicalCase() {
        String text = "hello";
        Languages.LanguageSet expectedLanguageSet = Languages.LanguageSet.from("en");
        
        Languages.LanguageSet guessedLanguages = langInstance.guessLanguages(text);
        
        assertEquals(expectedLanguageSet, guessedLanguages);
    }
    
    @Test
    public void testGuessLanguages_NoLanguagesMatch() {
        String text = "1234";
        Languages.LanguageSet expectedLanguageSet = Languages.ANY_LANGUAGE;
        
        Languages.LanguageSet guessedLanguages = langInstance.guessLanguages(text);
        
        assertEquals(expectedLanguageSet, guessedLanguages);
    }
    
    @Test
    public void testGuessLanguages_AllLanguagesMatch() {
        String text = "bonjour";
        Languages.LanguageSet expectedLanguageSet = Languages.LanguageSet.from("fr","oc","wa");
        
        Languages.LanguageSet guessedLanguages = langInstance.guessLanguages(text);
        
        assertEquals(expectedLanguageSet, guessedLanguages);
    }
    
    @Test
    public void testLoadFromResource_InvalidResourceName() {
        String invalidResourceName = "invalid_resource.txt";
        
        try {
            Lang.loadFromResource(invalidResourceName, Languages.getInstance(NameType.GENERIC));
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Unable to resolve required resource:" + invalidResourceName, e.getMessage());
        }
    }
    
    @Test
    public void testLoadFromResource_ValidResourceName() {
        String validResourceName = "org/apache/commons/codec/language/bm/lang.txt";
        
        try {
            Lang.loadFromResource(validResourceName, Languages.getInstance(NameType.GENERIC));
        } catch (Exception e) {
            fail("Unexpected exception occurred: " + e.getMessage());
        }
    }
  
}