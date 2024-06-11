import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LangTest {

    private Lang lang;

    @Before
    public void setUp() {
        lang = Lang.instance(NameType.GENERIC);
    }

    @Test
    public void testGuessLanguage() {
        String word = "hello";
        String expectedLanguage = "en";
        String actualLanguage = lang.guessLanguage(word);
        assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguages() {
        String word = "hello";
        Languages.LanguageSet expectedLanguages = Languages.getInstance(NameType.GENERIC).getLanguages();
        Languages.LanguageSet actualLanguages = lang.guessLanguages(word);
        assertEquals(expectedLanguages, actualLanguages);
    }

}