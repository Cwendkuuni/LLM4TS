import org.junit.Assert;
import org.junit.Test;

public class LangTest {

    Lang lang = Lang.instance(NameType.GENERIC_NAME);

    @Test
    public void testGuessLanguage() {
        String text = "hello";
        String result = lang.guessLanguage(text);
        Assert.assertEquals("eng", result);
    }

    @Test
    public void testGuessLanguages() {
        String text = "hello";
        Languages.LanguageSet result = lang.guessLanguages(text);
        Assert.assertEquals("eng", result.getAny());
    }

    @Test
    public void testLangRuleMatches() {
        Lang.LangRule rule = new Lang.LangRule(Pattern.compile("h.*"), Collections.singleton("eng"), true);
        boolean result = rule.matches("hello");
        Assert.assertTrue(result);
    }

    @Test
    public void testLangRuleMatchesFalse() {
        Lang.LangRule rule = new Lang.LangRule(Pattern.compile("h.*"), Collections.singleton("eng"), true);
        boolean result = rule.matches("world");
        Assert.assertFalse(result);
    }

    @Test
    public void testLoadFromResource() {
        Lang result = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.GENERIC_NAME));
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testLoadFromResourceInvalidResource() {
        Lang result = Lang.loadFromResource("invalid/resource/path", Languages.getInstance(NameType.GENERIC_NAME));
    }
}