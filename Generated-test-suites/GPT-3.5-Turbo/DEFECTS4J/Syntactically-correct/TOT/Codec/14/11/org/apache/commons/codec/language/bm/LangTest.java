import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Typical use case
        String word = "hello";
        String expectedLanguage = "en";
        String actualLanguage = lang.guessLanguage(word);
        Assert.assertEquals(expectedLanguage, actualLanguage);

        // Edge case where word is empty
        word = "";
        expectedLanguage = "und";
        actualLanguage = lang.guessLanguage(word);
        Assert.assertEquals(expectedLanguage, actualLanguage);

        // Error scenario where word is null
        word = null;
        expectedLanguage = "und";
        actualLanguage = lang.guessLanguage(word);
        Assert.assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);

        // Typical use case
        String word = "apple";
        Languages.LanguageSet expectedLanguages = Languages.LanguageSet.from("en");
        Languages.LanguageSet actualLanguages = lang.guessLanguages(word);
        Assert.assertEquals(expectedLanguages, actualLanguages);

        // Edge case where word is empty
        word = "";
        expectedLanguages = Languages.ANY_LANGUAGE;
        actualLanguages = lang.guessLanguages(word);
        Assert.assertEquals(expectedLanguages, actualLanguages);

        // Error scenario where word is null
        word = null;
        expectedLanguages = Languages.ANY_LANGUAGE;
        actualLanguages = lang.guessLanguages(word);
        Assert.assertEquals(expectedLanguages, actualLanguages);
    }
}