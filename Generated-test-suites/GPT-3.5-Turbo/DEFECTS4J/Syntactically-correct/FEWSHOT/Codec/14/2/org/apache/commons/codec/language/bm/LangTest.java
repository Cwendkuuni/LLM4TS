import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertNotNull(lang);

        String text = "hello";
        String language = lang.guessLanguage(text);
        assertNotNull(language);
        assertEquals(Languages.ANY, language);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        assertNotNull(lang);

        String input = "hello";
        Languages.LanguageSet languages = lang.guessLanguages(input);
        assertNotNull(languages);
        assertEquals(Languages.ANY_LANGUAGE, languages);
    }
}