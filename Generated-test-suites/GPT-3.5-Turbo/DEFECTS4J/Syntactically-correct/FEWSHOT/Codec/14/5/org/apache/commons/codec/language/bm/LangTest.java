import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.COUNTRY_NAME);
        assertEquals("english", lang.guessLanguage("England"));
        assertEquals("french", lang.guessLanguage("France"));
        assertEquals("german", lang.guessLanguage("Deutschland"));
        assertEquals("spanish", lang.guessLanguage("España"));
        assertEquals("italian", lang.guessLanguage("Italia"));

        lang = Lang.instance(NameType.GENERIC_NAME);
        assertEquals("english", lang.guessLanguage("Hello"));
        assertEquals("french", lang.guessLanguage("Bonjour"));
        assertEquals("german", lang.guessLanguage("Guten Tag"));
        assertEquals("spanish", lang.guessLanguage("Hola"));
        assertEquals("italian", lang.guessLanguage("Ciao"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.COUNTRY_NAME);
        assertTrue(lang.guessLanguages("England").contains("english"));
        assertTrue(lang.guessLanguages("France").contains("french"));
        assertTrue(lang.guessLanguages("Deutschland").contains("german"));
        assertTrue(lang.guessLanguages("España").contains("spanish"));
        assertTrue(lang.guessLanguages("Italia").contains("italian"));

        lang = Lang.instance(NameType.GENERIC_NAME);
        assertTrue(lang.guessLanguages("Hello").contains("english"));
        assertTrue(lang.guessLanguages("Bonjour").contains("french"));
        assertTrue(lang.guessLanguages("Guten Tag").contains("german"));
        assertTrue(lang.guessLanguages("Hola").contains("spanish"));
        assertTrue(lang.guessLanguages("Ciao").contains("italian"));
    }
}