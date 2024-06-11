import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        lang = Lang.instance(NameType.GENERIC);
    }

    @Test
    public void testGuessLanguage() {
        assertEquals("english", lang.guessLanguage("hello"));
        assertEquals("german", lang.guessLanguage("hallo"));
        assertEquals("spanish", lang.guessLanguage("hola"));
        assertEquals("french", lang.guessLanguage("bonjour"));
        assertEquals("italian", lang.guessLanguage("ciao"));
    }

    @Test
    public void testGuessLanguages() {
        Languages.LanguageSet languages = lang.guessLanguages("hello");
        assertTrue(languages.contains("english"));
        assertFalse(languages.contains("german"));
        assertFalse(languages.contains("spanish"));
        assertFalse(languages.contains("french"));
        assertFalse(languages.contains("italian"));

        languages = lang.guessLanguages("hallo");
        assertFalse(languages.contains("english"));
        assertTrue(languages.contains("german"));
        assertFalse(languages.contains("spanish"));
        assertFalse(languages.contains("french"));
        assertFalse(languages.contains("italian"));

        languages = lang.guessLanguages("hola");
        assertFalse(languages.contains("english"));
        assertFalse(languages.contains("german"));
        assertTrue(languages.contains("spanish"));
        assertFalse(languages.contains("french"));
        assertFalse(languages.contains("italian"));

        languages = lang.guessLanguages("bonjour");
        assertFalse(languages.contains("english"));
        assertFalse(languages.contains("german"));
        assertFalse(languages.contains("spanish"));
        assertTrue(languages.contains("french"));
        assertFalse(languages.contains("italian"));

        languages = lang.guessLanguages("ciao");
        assertFalse(languages.contains("english"));
        assertFalse(languages.contains("german"));
        assertFalse(languages.contains("spanish"));
        assertFalse(languages.contains("french"));
        assertTrue(languages.contains("italian"));
    }
}