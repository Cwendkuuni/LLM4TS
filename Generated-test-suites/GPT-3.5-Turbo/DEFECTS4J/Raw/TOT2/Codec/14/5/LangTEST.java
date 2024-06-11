import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        
        assertEquals("english", lang.guessLanguage("hello"));
        assertEquals("ANY", lang.guessLanguage("123"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SPANISH);
        
        assertEquals("spanish", lang.guessLanguages("hola").getAny());
        assertFalse(lang.guessLanguages("").isSingleton());
    }

    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.GERMAN));
        
        assertNotNull(lang);
        assertEquals("german", lang.guessLanguage("hallo"));
    }
}