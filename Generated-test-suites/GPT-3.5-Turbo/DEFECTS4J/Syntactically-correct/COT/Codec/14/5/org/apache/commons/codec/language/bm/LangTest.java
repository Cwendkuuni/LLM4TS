
import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String result = lang.guessLanguage("hello");
        Assert.assertEquals("eng", result);
    }
    
    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet result = lang.guessLanguages("hello");
        Languages.LanguageSet expected = Languages.LanguageSet.from(new HashSet<>(Arrays.asList("eng")));
        Assert.assertEquals(expected, result);
    }
}
