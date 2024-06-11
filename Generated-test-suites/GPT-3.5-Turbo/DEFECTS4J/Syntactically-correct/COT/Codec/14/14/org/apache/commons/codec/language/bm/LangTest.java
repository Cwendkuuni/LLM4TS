import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Basic test case
        String language = lang.guessLanguage("hello");
        Assert.assertEquals("eng", language);

        // Edge case: empty input
        language = lang.guessLanguage("");
        Assert.assertEquals("und", language);

        // Edge case: input with special characters
        language = lang.guessLanguage("!@#$");
        Assert.assertEquals("und", language);

        // Edge case: input with numbers
        language = lang.guessLanguage("123");
        Assert.assertEquals("und", language);

        // Exception handling: null input
        language = lang.guessLanguage(null);
        Assert.assertEquals("und", language);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Basic test case
        Languages.LanguageSet languageSet = lang.guessLanguages("hello");
        Assert.assertEquals(1, languageSet.size());
        Assert.assertTrue(languageSet.contains("eng"));

        // Edge case: empty input
        languageSet = lang.guessLanguages("");
        Assert.assertEquals(0, languageSet.size());

        // Edge case: input with special characters
        languageSet = lang.guessLanguages("!@#$");
        Assert.assertEquals(0, languageSet.size());

        // Edge case: input with numbers
        languageSet = lang.guessLanguages("123");
        Assert.assertEquals(0, languageSet.size());

        // Exception handling: null input
        languageSet = lang.guessLanguages(null);
        Assert.assertEquals(0, languageSet.size());
    }
}