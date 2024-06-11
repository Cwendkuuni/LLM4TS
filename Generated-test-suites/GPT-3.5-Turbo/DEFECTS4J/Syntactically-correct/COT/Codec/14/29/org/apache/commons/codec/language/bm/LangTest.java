import org.junit.Test;

public class LangTest {

    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC_NAME);
        String text = "hello";
        String result = lang.guessLanguage(text);
        String expected = "eng";
        assertEquals(expected, result);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC_NAME);
        String text = "hello";
        Languages.LanguageSet result = lang.guessLanguages(text);
        Languages.LanguageSet expected = Languages.LanguageSet.from("eng");
        assertEquals(expected, result);
    }

    @Test
    public void testLoadFromResource() {
        String resourceName = "org/apache/commons/codec/language/bm/lang.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC_NAME);
        Lang result = Lang.loadFromResource(resourceName, languages);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromResourceInvalidLineFormat() {
        String resourceName = "org/apache/commons/codec/language/bm/lang_invalid.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC_NAME);
        Lang.loadFromResource(resourceName, languages);
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceNotFound() {
        String resourceName = "org/apache/commons/codec/language/bm/lang_not_found.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC_NAME);
        Lang.loadFromResource(resourceName, languages);
    }
}