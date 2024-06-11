
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.language.bm.Lang;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(Lang.NameType.WORD);
        String language = lang.guessLanguage("hello");
        Assert.assertEquals("eng", language);

        language = lang.guessLanguage("hola");
        Assert.assertEquals("spa", language);

        language = lang.guessLanguage("bonjour");
        Assert.assertEquals("fre", language);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(Lang.NameType.WORD);
        Lang.LanguageSet languageSet = lang.guessLanguages("hello");
        Assert.assertEquals("eng", languageSet.getAny());

        languageSet = lang.guessLanguages("hola");
        Assert.assertEquals("spa", languageSet.getAny());

        languageSet = lang.guessLanguages("bonjour");
        Assert.assertEquals("fre", languageSet.getAny());
    }
}
