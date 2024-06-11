import org.junit.Test;

import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("eng", lang.guessLanguage("hello"));
        assertEquals("deu", lang.guessLanguage("Hallo"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet expected1 = Languages.LanguageSet.from("eng");
        assertEquals(expected1, lang.guessLanguages("hello"));

        Languages.LanguageSet expected2 = Languages.LanguageSet.from("deu", "eng");
        assertEquals(expected2, lang.guessLanguages("Hallo"));
    }
}