import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("english", lang.guessLanguage("hello"));
        assertEquals("french", lang.guessLanguage("bonjour"));
        assertEquals("german", lang.guessLanguage("hallo"));
        assertEquals(Languages.ANY, lang.guessLanguage("1234567890"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals(Languages.LanguageSet.from("english"), lang.guessLanguages("hello"));
        assertEquals(Languages.LanguageSet.from("french"), lang.guessLanguages("bonjour"));
        assertEquals(Languages.LanguageSet.from("german"), lang.guessLanguages("hallo"));
        assertEquals(Languages.ANY_LANGUAGE, lang.guessLanguages("1234567890"));
    }

    @Test
    public void testGuessLanguageWithEdgeCases() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals(Languages.ANY, lang.guessLanguage(""));
        assertEquals("english", lang.guessLanguage("a"));
        assertEquals("english", lang.guessLanguage("A"));
        assertEquals("english", lang.guessLanguage("Hello"));
        assertEquals(Languages.ANY, lang.guessLanguage("こんにちは"));
    }

    @Test
    public void testGuessLanguagesWithEdgeCases() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals(Languages.ANY_LANGUAGE, lang.guessLanguages(""));
        assertEquals(Languages.LanguageSet.from("english"), lang.guessLanguages("a"));
        assertEquals(Languages.LanguageSet.from("english"), lang.guessLanguages("A"));
        assertEquals(Languages.LanguageSet.from("english"), lang.guessLanguages("Hello"));
        assertEquals(Languages.ANY_LANGUAGE, lang.guessLanguages("こんにちは"));
    }

    @Test
    public void testLoadFromResource() {
        Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.GENERIC));
        assertNotNull(lang);
    }
  
    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceWithInvalidResourceName() {
        Lang.loadFromResource("invalid/resource/name", Languages.getInstance(NameType.GENERIC));
    }

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertNotNull(lang);
    }

}
