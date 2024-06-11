import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
    
    private Lang lang;

    @Before
    public void setUp() {
        lang = Lang.instance(NameType.GENERIC);
    }
    
    // Testing guessLanguage method
    
    @Test
    public void testGuessLanguage_NormalInput_ReturnsCorrectLanguage() {
        String text = "hello";
        String expected = "en";
        
        String actual = lang.guessLanguage(text);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGuessLanguage_EmptyInput_ReturnsAnyLanguage() {
        String text = "";
        
        String actual = lang.guessLanguage(text);
        
        assertEquals(Languages.ANY, actual);
    }
    
    // Testing guessLanguages method
    
    @Test
    public void testGuessLanguages_NormalInput_ReturnsCorrectLanguageSet() {
        String text = "hello";
        
        Languages.LanguageSet expected = Languages.LanguageSet.from(new HashSet<String>(Arrays.asList("en")));
        
        Languages.LanguageSet actual = lang.guessLanguages(text);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGuessLanguages_EmptyInput_ReturnsAnyLanguageSet() {
        String text = "";
        
        Languages.LanguageSet actual = lang.guessLanguages(text);
        
        assertEquals(Languages.ANY_LANGUAGE, actual);
    }
    
    // Testing loadFromResource method
    
    @Test
    public void testLoadFromResource_ExistingResource_ReturnsValidLangInstance() {
        String resourceName = "org/apache/commons/codec/language/bm/lang.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC);
        
        Lang actual = Lang.loadFromResource(resourceName, languages);
        
        assertNotNull(actual);
        assertEquals(languages, actual.getLanguages());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testLoadFromResource_NonExistingResource_ThrowsIllegalStateException() {
        String resourceName = "non/existing/resource.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC);
        
        Lang.loadFromResource(resourceName, languages);
    }
    
    // Testing instance method
    
    @Test
    public void testInstance_ExistingNameType_ReturnsValidLangInstance() {
        NameType nameType = NameType.GENERIC;
        
        Lang actual = Lang.instance(nameType);
        
        assertNotNull(actual);
        assertEquals(nameType, actual.getLanguages().getNameType());
    }
    
    @Test
    public void testInstance_NonExistingNameType_ReturnsNull() {
        NameType nameType = NameType.UNKNOWN;
        
        Lang actual = Lang.instance(nameType);
        
        assertNull(actual);
    }
    
    // Testing LangRule class
    
    @Test
    public void testLangRule_MatchesPattern_ReturnsTrue() {
        Pattern pattern = Pattern.compile("a+");
        Set<String> languages = new HashSet<>();
        boolean acceptOnMatch = true;
        LangRule langRule = new LangRule(pattern, languages, acceptOnMatch);
        
        boolean actual = langRule.matches("aaa");
        
        assertTrue(actual);
    }
    
    @Test
    public void testLangRule_DoesNotMatchPattern_ReturnsFalse() {
        Pattern pattern = Pattern.compile("a+");
        Set<String> languages = new HashSet<>();
        boolean acceptOnMatch = true;
        LangRule langRule = new LangRule(pattern, languages, acceptOnMatch);
        
        boolean actual = langRule.matches("bbb");
        
        assertFalse(actual);
    }
}