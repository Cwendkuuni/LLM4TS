import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    // Test cases for guessLanguage() method
    
    @Test
    public void testGuessLanguage_WithValidInput_ReturnsLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        Assert.assertEquals("eng", result);
    }
    
    @Test
    public void testGuessLanguage_WithEmptyInput_ReturnsAny() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("");
        Assert.assertEquals("und", result);
    }
    
    @Test
    public void testGuessLanguage_WithNullInput_ReturnsAny() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage(null);
        Assert.assertEquals("und", result);
    }
    
    // Test cases for guessLanguages() method
    
    @Test
    public void testGuessLanguages_WithValidInput_ReturnsLanguageSet() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        Assert.assertTrue(result.contains("eng"));
    }
    
    @Test
    public void testGuessLanguages_WithEmptyInput_ReturnsAnyLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("");
        Assert.assertTrue(result.equals(Languages.ANY_LANGUAGE));
    }
    
    @Test
    public void testGuessLanguages_WithNullInput_ReturnsAnyLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages(null);
        Assert.assertTrue(result.equals(Languages.ANY_LANGUAGE));
    }
    
}