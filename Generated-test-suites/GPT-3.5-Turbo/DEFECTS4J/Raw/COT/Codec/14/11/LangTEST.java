
import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.SIMPLE);
        // Test a word that is expected to be in English
        String englishWord = "hello";
        Assert.assertEquals("eng", lang.guessLanguage(englishWord));
        // Test a word that is expected to be in French
        String frenchWord = "bonjour";
        Assert.assertEquals("fra", lang.guessLanguage(frenchWord));
        // Test a word that is expected to be in German
        String germanWord = "Guten Tag";
        Assert.assertEquals("deu", lang.guessLanguage(germanWord));
        // Test a word that is expected to be in Spanish
        String spanishWord = "hola";
        Assert.assertEquals("spa", lang.guessLanguage(spanishWord));

        // Test a word with numbers
        String mixedWord = "h3ll0";
        Assert.assertEquals("eng", lang.guessLanguage(mixedWord));

        // Test a word with special characters
        String specialCharWord = "$hello$";
        Assert.assertEquals("eng", lang.guessLanguage(specialCharWord));

        // Test an empty word
        String emptyWord = "";
        Assert.assertEquals("und", lang.guessLanguage(emptyWord));
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.SIMPLE);
        // Test a word that is expected to be in English
        String englishWord = "hello";
        Assert.assertEquals("eng", lang.guessLanguages(englishWord).getAny());
        // Test a word that is expected to be in French
        String frenchWord = "bonjour";
        Assert.assertEquals("fra", lang.guessLanguages(frenchWord).getAny());
        // Test a word that is expected to be in German
        String germanWord = "Guten Tag";
        Assert.assertEquals("deu", lang.guessLanguages(germanWord).getAny());
        // Test a word that is expected to be in Spanish
        String spanishWord = "hola";
        Assert.assertEquals("spa", lang.guessLanguages(spanishWord).getAny());

        // Test a word with numbers
        String mixedWord = "h3ll0";
        Assert.assertEquals("eng", lang.guessLanguages(mixedWord).getAny());

        // Test a word with special characters
        String specialCharWord = "$hello$";
        Assert.assertEquals("eng", lang.guessLanguages(specialCharWord).getAny());

        // Test an empty word
        String emptyWord = "";
        Assert.assertEquals(Languages.ANY_LANGUAGE, lang.guessLanguages(emptyWord));
    }
}
