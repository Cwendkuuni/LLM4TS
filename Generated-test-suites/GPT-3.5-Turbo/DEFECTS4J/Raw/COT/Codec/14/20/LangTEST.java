
import static org.junit.Assert.*;

import org.junit.Test;

public class LangTest {
    
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Basic test case
        String word = "hello";
        String expected = "eng";
        String actual = lang.guessLanguage(word);
        assertEquals(expected, actual);
        
        // Test case with special characters
        word = "@#$%^&*()_+=-";
        expected = "und";
        actual = lang.guessLanguage(word);
        assertEquals(expected, actual);
        
        // Test case with numbers
        word = "12345";
        expected = "und";
        actual = lang.guessLanguage(word);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        
        // Basic test case
        String word = "hello";
        Languages.LanguageSet expected = Languages.LanguageSet.from("eng");
        Languages.LanguageSet actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
        
        // Test case with special characters
        word = "@#$%^&*()_+=-";
        expected = Languages.LanguageSet.from("und");
        actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
        
        // Test case with numbers
        word = "12345";
        expected = Languages.LanguageSet.from("und");
        actual = lang.guessLanguages(word);
        assertEquals(expected, actual);
    }
}
