import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
    
    private Lang lang;
    
    @Before
    public void setUp() {
        lang = new Lang(Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.ENGLISH)));
    }
    
    @Test
    public void testGuessLanguage() {
        assertEquals("english", lang.guessLanguage("hello"));
        assertEquals("french", lang.guessLanguage("bonjour"));
    }
    
    @Test
    public void testGuessLanguages() {
        Languages.LanguageSet ls = lang.guessLanguages("hello");
        assertTrue(ls.contains("english"));
        assertFalse(ls.contains("french"));
        
        ls = lang.guessLanguages("bonjour");
        assertFalse(ls.contains("english"));
        assertTrue(ls.contains("french"));
    }
    
}