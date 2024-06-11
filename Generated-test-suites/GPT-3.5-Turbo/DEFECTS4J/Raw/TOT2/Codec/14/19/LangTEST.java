import static org.junit.Assert.*;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.SIMPLIFIED);
        assertEquals("eng", lang.guessLanguage("hello")); // typical use case
        assertEquals(Languages.ANY, lang.guessLanguage("")); // edge case: empty input
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SIMPLIFIED);
        Languages.LanguageSet languageSet = lang.guessLanguages("world");
        assertTrue(languageSet.contains("none")); // typical use case
        assertTrue(languageSet.getLanguages().size() == 1); // edge case: single language in result
    }

    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("custom_lang.txt", Languages.getInstance(NameType.SIMPLIFIED));
        assertNotNull(lang); // typical use case
        assertThrows(IllegalStateException.class, () -> Lang.loadFromResource("non_existent.txt", Languages.getInstance(NameType.SIMPLIFIED))); // error scenario: resource not found
    }

}