import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import java.util.Locale;

public class LangTest {
    private Lang lang;

    @Before
    public void setup() {
        lang = Lang.instance(NameType.GENERIC);
    }

    @Test
    public void testGuessLanguage() {
        assertEquals("English", lang.guessLanguage("hello"));
        assertEquals("French", lang.guessLanguage("bonjour"));
        assertEquals("Spanish", lang.guessLanguage("hola"));
        assertEquals("German", lang.guessLanguage("hallo"));
        assertEquals("Italian", lang.guessLanguage("ciao"));
    }

    @Test
    public void testGuessLanguages() {
        Set<String> expected = new HashSet<String>();
        expected.add("English");
        expected.add("French");
        expected.add("Spanish");
        expected.add("German");
        expected.add("Italian");

        assertEquals(expected, lang.guessLanguages("hello"));
    }
}