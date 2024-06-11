import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LangTest {

    // Test case for guessLanguage() method with a word that has a unique match
    @Test
    public void testGuessLanguage_UniqueMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String word = "bonjour";
        String expectedLanguage = "fr";
        String actualLanguage = lang.guessLanguage(word);
        Assert.assertEquals(expectedLanguage, actualLanguage);
    }

    // Test case for guessLanguage() method with a word that has no unique match
    @Test
    public void testGuessLanguage_NoUniqueMatch() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String word = "hello";
        String expectedLanguage = Languages.ANY;
        String actualLanguage = lang.guessLanguage(word);
        Assert.assertEquals(expectedLanguage, actualLanguage);
    }

    // Test case for guessLanguages() method with a word that has multiple potential matches
    @Test
    public void testGuessLanguages_MultipleMatches() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String word = "amigo";
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("es", "pt"));
        Languages.LanguageSet actualLanguages = lang.guessLanguages(word);
        Assert.assertEquals(expectedLanguages, actualLanguages.getLanguages());
    }

    // Test case for loadFromResource() method with valid resource name and languages
    @Test
    public void testLoadFromResource_ValidResource() {
        String resource = "org/apache/commons/codec/language/bm/lang.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC);
        Lang lang = Lang.loadFromResource(resource, languages);
        Assert.assertNotNull(lang);
    }

    // Test case for loadFromResource() method with invalid resource name
    @Test(expected = IllegalStateException.class)
    public void testLoadFromResource_InvalidResource() {
        String resource = "org/apache/commons/codec/language/bm/nonexistent.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC);
        Lang.loadFromResource(resource, languages);
    }

}