import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;

public class LangTest {
    private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";
    
    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertTrue(lang instanceof Lang);
    }
    
    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.GENERIC));
        assertTrue(lang instanceof Lang);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceNonExistent() {
        Lang.loadFromResource("nonExistentResource", Languages.getInstance(NameType.GENERIC));
    }
    
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.GENERIC));
        String language = lang.guessLanguage("hello");
        assertEquals("eng", language);
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.GENERIC));
        Languages.LanguageSet languageSet = lang.guessLanguages("hello");
        assertTrue(languageSet.getLanguages().contains("eng"));
    }
    
    @Test
    public void testGuessLanguageNotUnique() {
        Lang lang = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.GENERIC));
        String language = lang.guessLanguage("abc");
        assertEquals("und", language);
    }
    
    @Test
    public void testGuessLanguagesNotUnique() {
        Lang lang = Lang.loadFromResource(LANGUAGE_RULES_RN, Languages.getInstance(NameType.GENERIC));
        Languages.LanguageSet languageSet = lang.guessLanguages("abc");
        assertFalse(languageSet.isSingleton());
    }
}