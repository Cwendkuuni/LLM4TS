import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
    
    // Helper method to check if two sets are equal
    public static void assertEqualsSet(Set<String> expected, Set<String> actual){
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }
    
    // Test guessLanguage() method
    @Test
    public void testGuessLanguage(){
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test with null input
        String result = lang.guessLanguage(null);
        assertEquals(Languages.ANY, result);
        
        // Test with empty input
        result = lang.guessLanguage("");
        assertEquals(Languages.ANY, result);
        
        // Test with non-matching input
        result = lang.guessLanguage("hello");
        assertEquals(Languages.ANY, result);
        
        // Test with matching input
        result = lang.guessLanguage("bonjour");
        assertEquals("fr", result);
    }
    
    // Test guessLanguages() method
    @Test
    public void testGuessLanguages(){
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test with null input
        Languages.LanguageSet result = lang.guessLanguages(null);
        assertEquals(Languages.ANY_LANGUAGE, result);
        
        // Test with empty input
        result = lang.guessLanguages("");
        assertEquals(Languages.ANY_LANGUAGE, result);
        
        // Test with non-matching input
        result = lang.guessLanguages("hello");
        assertEquals(Languages.ANY_LANGUAGE, result);
        
        // Test with matching input
        result = lang.guessLanguages("bonjour");
        Set<String> expected = new HashSet<>();
        expected.add("fr");
        assertEqualsSet(expected, result.getLanguages());
    }
}