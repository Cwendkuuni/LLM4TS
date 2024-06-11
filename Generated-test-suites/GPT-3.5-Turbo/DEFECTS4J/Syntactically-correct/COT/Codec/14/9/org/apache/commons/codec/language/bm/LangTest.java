import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("eng", lang.guessLanguage("hello"));
        assertEquals("spa", lang.guessLanguage("hola"));
        assertEquals("deu", lang.guessLanguage("hallo"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet ls = lang.guessLanguages("hello");
        assertTrue(ls.contains("eng"));
        assertEquals(1, ls.size());
        assertFalse(ls.isSingleton());

        ls = lang.guessLanguages("hola");
        assertTrue(ls.contains("spa"));
        assertEquals(1, ls.size());
        assertFalse(ls.isSingleton());

        ls = lang.guessLanguages("hallo");
        assertTrue(ls.contains("deu"));
        assertEquals(1, ls.size());
        assertFalse(ls.isSingleton());
    }

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertNotNull(lang);
    }

    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource(Lang.LANGUAGE_RULES_RN, Languages.getInstance(NameType.GENERIC));
        assertNotNull(lang);
    }
}