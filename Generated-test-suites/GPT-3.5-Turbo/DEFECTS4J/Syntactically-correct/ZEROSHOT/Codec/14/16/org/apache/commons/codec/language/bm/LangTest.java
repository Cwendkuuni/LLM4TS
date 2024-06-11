import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String language = lang.guessLanguage("hello");
        Assert.assertEquals("eng", language);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        Languages.LanguageSet languageSet = lang.guessLanguages("hello");
        Set<String> expectedSet = new HashSet<>();
        expectedSet.add("eng");
        Assert.assertEquals(expectedSet, languageSet.getLanguages());
    }

}