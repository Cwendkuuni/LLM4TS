import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.BeforeClass;
import org.junit.Test;

public class LangTest {
    
    private static Lang lang;

    @BeforeClass
    public static void setup() {
        lang = Lang.instance(NameType.SIMPLIFIED_CHINESE);
    }

    @Test
    public void testGuessLanguage() {
        String language = lang.guessLanguage("你好");
        assertEquals("cmn", language);
        
        language = lang.guessLanguage("hello");
        assertEquals("eng", language);
        
        language = lang.guessLanguage("こんにちは");
        assertEquals("jpn", language);
    }

    @Test
    public void testGuessLanguages() {
        Languages.LanguageSet languageSet = lang.guessLanguages("你好");
        assertTrue(languageSet.contains("cmn"));
        assertTrue(!languageSet.contains("eng"));
        assertTrue(!languageSet.contains("jpn"));
        
        languageSet = lang.guessLanguages("hello");
        assertTrue(!languageSet.contains("cmn"));
        assertTrue(languageSet.contains("eng"));
        assertTrue(!languageSet.contains("jpn"));
        
        languageSet = lang.guessLanguages("こんにちは");
        assertTrue(!languageSet.contains("cmn"));
        assertTrue(!languageSet.contains("eng"));
        assertTrue(languageSet.contains("jpn"));
    }
}