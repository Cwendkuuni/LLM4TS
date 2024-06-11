import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
    
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.SIMPLIFIED);
        
        // Typical use case
        assertEquals("english", lang.guessLanguage("hello"));
        
        // Edge case
        assertEquals("german", lang.guessLanguage("schmetterling"));
        
        // Error scenario
        assertEquals("unknown", lang.guessLanguage(""));
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SIMPLIFIED);
        
        // Typical use case
        assertEquals("english", lang.guessLanguages("hello").getAny());
        
        // Edge case
        assertEquals("german", lang.guessLanguages("schmetterling").getAny());
        
        // Error scenario
        assertEquals("unknown", lang.guessLanguages("").getAny());
    }
    
    @Test
    public void testInstance() {
        // Typical use case
        assertNotNull(Lang.instance(NameType.SIMPLIFIED));
        
        // Edge case - invalid NameType
        assertNull(Lang.instance(null));
    }
    
}