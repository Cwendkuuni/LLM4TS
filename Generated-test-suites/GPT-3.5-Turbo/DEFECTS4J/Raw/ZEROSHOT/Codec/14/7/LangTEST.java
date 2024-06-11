import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LangTest {
    private Lang lang;

    @Before
    public void setup() {
        lang = Lang.instance(NameType.GENERIC);
    }

    @Test
    public void testGuessLanguage() {
        String input = "hello";
        String expected = "eng";
        String result = lang.guessLanguage(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGuessLanguages() {
        String input = "hello";
        String[] expected = {"eng"};
        Languages.LanguageSet result = lang.guessLanguages(input);
        Assert.assertArrayEquals(expected, result.getLanguages().toArray());
    }

    @Test
    public void testGuessLanguageNoMatch() {
        String input = "xyz";
        String expected = "und";
        String result = lang.guessLanguage(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGuessLanguagesNoMatch() {
        String input = "xyz";
        int expected = 0;
        Languages.LanguageSet result = lang.guessLanguages(input);
        Assert.assertEquals(expected, result.getLanguages().size());
    }
}