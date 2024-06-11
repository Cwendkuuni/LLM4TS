import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    private Lang lang;

    @Before
    public void setUp() {
        lang = Lang.instance(NameType.LANGUAGE);
    }

    @Test
    public void testGuessLanguage() {
        String text = "hello";
        String expected = "eng";
        String actual = lang.guessLanguage(text);
        assertEquals(expected, actual);
    }

    @Test
    public void testGuessLanguages() {
        String text = "hello";
        String[] expected = {"eng"};
        Languages.LanguageSet expectedSet = Languages.LanguageSet.from(expected);
        Languages.LanguageSet actualSet = lang.guessLanguages(text);
        assertEquals(expectedSet, actualSet);
    }
}