import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(Lang.NameType.GENERIC_NAME);
        String result = lang.guessLanguage("hello");
        assertEquals("eng", result);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(Lang.NameType.GENERIC_NAME);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        assertEquals(Languages.getInstance(Lang.NameType.GENERIC_NAME).getLanguageSet("eng"), result);
    }
}