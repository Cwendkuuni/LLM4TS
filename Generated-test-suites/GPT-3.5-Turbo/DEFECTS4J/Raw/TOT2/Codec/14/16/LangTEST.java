import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals("en", lang.guessLanguage("hello"));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals("en", lang.guessLanguages("world").getAny());
    }

    @Test
    public void testGuessLanguagesWithNoMatch() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals("any", lang.guessLanguages("1234").getAny());
    }

    @Test
    public void testGuessLanguagesWithMultipleMatches() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        assertEquals(true, lang.guessLanguages("blue").getLanguages().contains("en"));
        assertEquals(true, lang.guessLanguages("bleu").getLanguages().contains("fr"));
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceWithInvalidResourceName() {
        Lang.loadFromResource("invalid_resource_name", Languages.getInstance(NameType.ENGLISH));
    }
}