import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LanguageSet;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.Languages;
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
        assertEquals("eng", lang.guessLanguage("hello world"));
        assertEquals("rus", lang.guessLanguage("привет мир"));
        assertEquals("fra", lang.guessLanguage("bonjour le monde"));
        assertEquals("spa", lang.guessLanguage("hola mundo"));
        assertEquals("deu", lang.guessLanguage("hallo welt"));
        assertEquals("ita", lang.guessLanguage("ciao mondo"));
        assertEquals("jpn", lang.guessLanguage("こんにちは世界"));
    }
    
    @Test
    public void testGuessLanguages() {
        LanguageSet languages = lang.guessLanguages("hello world");
        assertTrue(languages.contains("eng"));
        assertFalse(languages.contains("rus"));
        assertFalse(languages.contains("fra"));
        assertFalse(languages.contains("spa"));
        assertFalse(languages.contains("deu"));
        assertFalse(languages.contains("ita"));
        assertFalse(languages.contains("jpn"));
        
         languages = lang.guessLanguages("привет мир");
        assertFalse(languages.contains("eng"));
        assertTrue(languages.contains("rus"));
        assertFalse(languages.contains("fra"));
        assertFalse(languages.contains("spa"));
        assertFalse(languages.contains("deu"));
        assertFalse(languages.contains("ita"));
        assertFalse(languages.contains("jpn"));
        
         languages = lang.guessLanguages("bonjour le monde");
        assertFalse(languages.contains("eng"));
        assertFalse(languages.contains("rus"));
        assertTrue(languages.contains("fra"));
        assertFalse(languages.contains("spa"));
        assertFalse(languages.contains("deu"));
        assertFalse(languages.contains("ita"));
        assertFalse(languages.contains("jpn"));
        
         languages = lang.guessLanguages("hola mundo");
        assertFalse(languages.contains("eng"));
        assertFalse(languages.contains("rus"));
        assertFalse(languages.contains("fra"));
        assertTrue(languages.contains("spa"));
        assertFalse(languages.contains("deu"));
        assertFalse(languages.contains("ita"));
        assertFalse(languages.contains("jpn"));
        
         languages = lang.guessLanguages("hallo welt");
        assertFalse(languages.contains("eng"));
        assertFalse(languages.contains("rus"));
        assertFalse(languages.contains("fra"));
        assertFalse(languages.contains("spa"));
        assertTrue(languages.contains("deu"));
        assertFalse(languages.contains("ita"));
        assertFalse(languages.contains("jpn"));
        
         languages = lang.guessLanguages("ciao mondo");
        assertFalse(languages.contains("eng"));
        assertFalse(languages.contains("rus"));
        assertFalse(languages.contains("fra"));
        assertFalse(languages.contains("spa"));
        assertFalse(languages.contains("deu"));
        assertTrue(languages.contains("ita"));
        assertFalse(languages.contains("jpn"));
        
         languages = lang.guessLanguages("こんにちは世界");
        assertFalse(languages.contains("eng"));
        assertFalse(languages.contains("rus"));
        assertFalse(languages.contains("fra"));
        assertFalse(languages.contains("spa"));
        assertFalse(languages.contains("deu"));
        assertFalse(languages.contains("ita"));
        assertTrue(languages.contains("jpn"));
    }
}