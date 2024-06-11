import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String result = lang.guessLanguage("hello");
        assertEquals("ENGLISH", result);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SPANISH);
        Languages.LanguageSet result = lang.guessLanguages("hola");
        assertTrue(result.contains("SPANISH"));
    }

    @Test
    public void testGuessLanguageEmptyInput() {
        Lang lang = Lang.instance(NameType.FRENCH);
        String result = lang.guessLanguage("");
        assertEquals("ANY", result);
    }

    @Test
    public void testGuessLanguagesNoMatch() {
        Lang lang = Lang.instance(NameType.GERMAN);
        Languages.LanguageSet result = lang.guessLanguages("asdf");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.PORTUGUESE));
        assertNotNull(lang);
    }
}