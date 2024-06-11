import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);

        assertEquals("english", lang.guessLanguage("hello"));
        assertEquals("any", lang.guessLanguage("12345"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SPANISH);

        assertEquals("spanish", lang.guessLanguage("hola"));
        assertEquals("any", lang.guessLanguage("12345"));
    }

    @Test
    public void testLangRuleMatches() {
        LangRule rule = new LangRule(Pattern.compile("test"), Collections.singleton("test"), true);

        assertTrue(rule.matches("test"));
        assertFalse(rule.matches("hello"));
    }

    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.GERMAN));

        assertNotNull(lang);
    }

}