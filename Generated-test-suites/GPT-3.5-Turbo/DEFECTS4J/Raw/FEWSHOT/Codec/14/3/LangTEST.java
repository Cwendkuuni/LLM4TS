import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("en", lang.guessLanguage("hello"));
        assertEquals("es", lang.guessLanguage("hola"));
        assertEquals("fr", lang.guessLanguage("bonjour"));
        assertEquals("de", lang.guessLanguage("hallo"));
        assertEquals("it", lang.guessLanguage("ciao"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertEquals("[en]", lang.guessLanguages("hello").toString());
        assertEquals("[es]", lang.guessLanguages("hola").toString());
        assertEquals("[fr]", lang.guessLanguages("bonjour").toString());
        assertEquals("[de]", lang.guessLanguages("hallo").toString());
        assertEquals("[it]", lang.guessLanguages("ciao").toString());
    }
}