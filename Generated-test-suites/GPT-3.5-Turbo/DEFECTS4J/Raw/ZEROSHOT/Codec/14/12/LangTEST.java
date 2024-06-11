import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LangTest {
    private Lang lang;

    @Before
    public void setup() {
        lang = Lang.instance(NameType.GENERIC);
    }

    @Test
    public void testGuessLanguage() {
        String text = "hello";
        String expected = "eng";
        assertEquals(expected, lang.guessLanguage(text));
    }

    @Test
    public void testGuessLanguages() {
        String text = "hello";
        int expectedSize = 1;
        assertEquals(expectedSize, lang.guessLanguages(text).size());
    }
}