import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;

public class LangTest {
    
    private Lang lang;

    @Before
    public void setUp() {
        lang = Lang.instance(Languages.NameType.ORIGIN);
    }

    @Test
    public void testGuessLanguage_WithValidWord_ReturnsCorrectLanguage() {
        String word = "hello";
        String expectedLanguage = "English";
        
        String actualLanguage = lang.guessLanguage(word);
        
        assertEquals(expectedLanguage, actualLanguage);
    }
    
    @Test
    public void testGuessLanguage_WithEmptyWord_ReturnsAny() {
        String word = "";
        String expectedLanguage = "Any";
        
        String actualLanguage = lang.guessLanguage(word);
        
        assertEquals(expectedLanguage, actualLanguage);
    }
    
    @Test
    public void testGuessLanguage_WithNullWord_ReturnsAny() {
        String word = null;
        String expectedLanguage = "Any";
        
        String actualLanguage = lang.guessLanguage(word);
        
        assertEquals(expectedLanguage, actualLanguage);
    }
    
    @Test
    public void testGuessLanguage_WithNonExistentWord_ReturnsAny() {
        String word = "abcdefg";
        String expectedLanguage = "Any";
        
        String actualLanguage = lang.guessLanguage(word);
        
        assertEquals(expectedLanguage, actualLanguage);
    }
    
    @Test
    public void testGuessLanguages_WithValidWord_ReturnsCorrectLanguageSet() {
        String word = "hello";
        String[] expectedLanguages = {"English"};
        
        Languages.LanguageSet expectedLanguageSet = Languages.LanguageSet.from(expectedLanguages);
        Languages.LanguageSet actualLanguageSet = lang.guessLanguages(word);
        
        assertEquals(expectedLanguageSet, actualLanguageSet);
    }
    
    @Test
    public void testGuessLanguages_WithEmptyWord_ReturnsAnyLanguageSet() {
        String word = "";
        String[] expectedLanguages = {"Any"};
        
        Languages.LanguageSet expectedLanguageSet = Languages.LanguageSet.from(expectedLanguages);
        Languages.LanguageSet actualLanguageSet = lang.guessLanguages(word);
        
        assertEquals(expectedLanguageSet, actualLanguageSet);
    }
    
    @Test
    public void testGuessLanguages_WithNullWord_ReturnsAnyLanguageSet() {
        String word = null;
        String[] expectedLanguages = {"Any"};
        
        Languages.LanguageSet expectedLanguageSet = Languages.LanguageSet.from(expectedLanguages);
        Languages.LanguageSet actualLanguageSet = lang.guessLanguages(word);
        
        assertEquals(expectedLanguageSet, actualLanguageSet);
    }
    
    @Test
    public void testGuessLanguages_WithNonExistentWord_ReturnsAnyLanguageSet() {
        String word = "abcdefg";
        String[] expectedLanguages = {"Any"};
        
        Languages.LanguageSet expectedLanguageSet = Languages.LanguageSet.from(expectedLanguages);
        Languages.LanguageSet actualLanguageSet = lang.guessLanguages(word);
        
        assertEquals(expectedLanguageSet, actualLanguageSet);
    }
}