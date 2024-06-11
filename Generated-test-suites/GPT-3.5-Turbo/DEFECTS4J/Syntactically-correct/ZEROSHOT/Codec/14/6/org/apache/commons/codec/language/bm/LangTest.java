import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Test a word with a unique language match
        String word1 = "hello";
        String expected1 = "en";
        assertEquals(expected1, lang.guessLanguage(word1));
        
        // Test a word with multiple potential matches
        String word2 = "hola";
        String expected2 = "und";
        assertTrue(lang.guessLanguages(word2).contains(expected2));
    }

    @Test
    public void testLoadFromResource() {
        String resourceName = "org/apache/commons/codec/language/bm/lang.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC);
        Lang lang = Lang.loadFromResource(resourceName, languages);
        
        assertNotNull(lang);
    }
}