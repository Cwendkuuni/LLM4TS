import org.junit.Test;
import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.NameType;

import static org.junit.Assert.assertEquals;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals("english", lang.guessLanguage("hello"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals(1, lang.guessLanguages("hello").size());
    }

    @Test
    public void testGuessLanguageWithEmptyInput() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals("any", lang.guessLanguage(""));
    }

    @Test
    public void testGuessLanguagesWithInvalidInput() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals(0, lang.guessLanguages(":!@#$").size());
    }

    @Test
    public void testGuessLanguagesWithSpecialCharacters() {
        Lang lang = Lang.instance(NameType.RUSSIAN);
        assertEquals(1, lang.guessLanguages("Привет").size());
    }

    @Test
    public void testGuessLanguagesWithEdgeCase() {
        Lang lang = Lang.instance(NameType.SPANISH);
        assertEquals(1, lang.guessLanguages("¿Hola?").size());
    }

}