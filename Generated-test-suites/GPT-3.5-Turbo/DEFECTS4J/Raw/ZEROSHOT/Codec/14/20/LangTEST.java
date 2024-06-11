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
        String text = "hello";
        String expected = "eng";
        String result = lang.guessLanguage(text);
        assertEquals(expected, result);
    }

    @Test
    public void testGuessLanguages() {
        String input = "hello";
        Languages.LanguageSet expected = Languages.LanguageSet.from("eng");
        Languages.LanguageSet result = lang.guessLanguages(input);
        assertEquals(expected, result);
    }
}