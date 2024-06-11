import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Before;
import org.junit.Test;

public class LangTest {

    private Lang lang;

    @Before
    public void setUp() {
        lang = Lang.instance(NameType.GENERIC);
    }

    @Test
    public void testGuessLanguage_SingleMatch() {
        String word = "hello";
        String expectedLanguage = "en";
        
        String guessedLanguage = lang.guessLanguage(word);
        
        assertEquals(expectedLanguage, guessedLanguage);
    }

    @Test
    public void testGuessLanguage_MultipleMatches() {
        String word = "team";
        
        String guessedLanguage = lang.guessLanguage(word);
        
        assertNotNull(guessedLanguage);
        assertTrue(guessedLanguage.length() > 0);
    }
    
    @Test
    public void testGuessLanguage_NoMatch() {
        String word = "xyz";
        
        String guessedLanguage = lang.guessLanguage(word);
        
        assertEquals(Languages.ANY, guessedLanguage);
    }

    @Test
    public void testGuessLanguage_CaseSensitiveMatch() {
        String word = "WORLD";
        String expectedLanguage = "en";
        
        String guessedLanguage = lang.guessLanguage(word);
        
        assertEquals(expectedLanguage, guessedLanguage);
    }

    @Test
    public void testGuessLanguages_SingleMatch() {
        String word = "hello";
        String expectedLanguage = "en";
        
        Languages.LanguageSet guessedLanguages = lang.guessLanguages(word);
        
        assertNotNull(guessedLanguages);
        assertEquals(1, guessedLanguages.size());
        assertTrue(guessedLanguages.containsLanguage(expectedLanguage));
    }

    @Test
    public void testGuessLanguages_MultipleMatches() {
        String word = "team";
        
        Languages.LanguageSet guessedLanguages = lang.guessLanguages(word);
        
        assertNotNull(guessedLanguages);
        assertTrue(guessedLanguages.size() > 1);
    }

    @Test
    public void testGuessLanguages_NoMatch() {
        String word = "xyz";
        
        Languages.LanguageSet guessedLanguages = lang.guessLanguages(word);
        
        assertNotNull(guessedLanguages);
        assertFalse(guessedLanguages.equals(Languages.NO_LANGUAGES));
    }

    @Test
    public void testGuessLanguages_CaseSensitiveMatch() {
        String word = "WORLD";
        String expectedLanguage = "en";
        
        Languages.LanguageSet guessedLanguages = lang.guessLanguages(word);
        
        assertNotNull(guessedLanguages);
        assertEquals(1, guessedLanguages.size());
        assertTrue(guessedLanguages.containsLanguage(expectedLanguage));
    }

    @Test
    public void testLoadFromResource_ValidResourceName() {
        String resourceName = "org/apache/commons/codec/language/bm/lang.txt";
        
        Lang loadedLang = Lang.loadFromResource(resourceName, Languages.getInstance(NameType.GENERIC));
        
        assertNotNull(loadedLang);
    }
    
    @Test(expected=IllegalStateException.class)
    public void testLoadFromResource_InvalidResourceName() {
        String resourceName = "invalid/resource/name.txt";
        
        Lang.loadFromResource(resourceName, Languages.getInstance(NameType.GENERIC));
        
        // The test is expected to throw an IllegalStateException, so if it reached this line, the test failed
        // because the exception was not thrown
        assertFalse(true);
    }

    @Test
    public void testLoadFromResource_NullResourceName() {
        String resourceName = null;
        
        Lang loadedLang = Lang.loadFromResource(resourceName, Languages.getInstance(NameType.GENERIC));
        
        assertNull(loadedLang);
    }

    @Test
    public void testLoadFromResource_EmptyResourceName() {
        String resourceName = "";
        
        Lang loadedLang = Lang.loadFromResource(resourceName, Languages.getInstance(NameType.GENERIC));
        
        assertNull(loadedLang);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLoadFromResource_InvalidRuleFormat() {
        String resourceName = "invalid_rules.txt";
        
        Lang.loadFromResource(resourceName, Languages.getInstance(NameType.GENERIC));
        
        // The test is expected to throw an IllegalArgumentException, so if it reached this line, the test failed
        // because the exception was not thrown
        assertFalse(true);
    }

    @Test
    public void testInstance_ValidNameType() {
        NameType nameType = NameType.GENERIC;
        
        Lang instance = Lang.instance(nameType);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testInstance_NullNameType() {
        NameType nameType = null;
        
        Lang instance = Lang.instance(nameType);
        
        assertNull(instance);
    }

    @Test
    public void testInstance_InvalidNameType() {
        NameType nameType = NameType.UNKNOWN;
        
        Lang instance = Lang.instance(nameType);
        
        assertNull(instance);
    }

    @Test
    public void testGuessLanguage_EmptyInput() {
        String word = "";
        
        String guessedLanguage = lang.guessLanguage(word);
        
        assertEquals(Languages.ANY, guessedLanguage);
    }
    
    @Test
    public void testGuessLanguages_EmptyInput() {
        String word = "";
        
        Languages.LanguageSet guessedLanguages = lang.guessLanguages(word);
        
        assertNotNull(guessedLanguages);
        assertFalse(guessedLanguages.equals(Languages.NO_LANGUAGES));
    }

    @Test
    public void testGuessLanguage_NullInput() {
        String word = null;
        
        String guessedLanguage = lang.guessLanguage(word);
        
        assertEquals(Languages.ANY, guessedLanguage);
    }
    
    @Test
    public void testGuessLanguages_NullInput() {
        String word = null;
        
        Languages.LanguageSet guessedLanguages = lang.guessLanguages(word);
        
        assertNotNull(guessedLanguages);
        assertFalse(guessedLanguages.equals(Languages.NO_LANGUAGES));
    }

}