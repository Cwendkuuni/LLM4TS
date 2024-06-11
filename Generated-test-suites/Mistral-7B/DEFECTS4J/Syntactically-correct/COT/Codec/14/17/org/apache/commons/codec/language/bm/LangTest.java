import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class LangTest {

    @Test
    public void testInstance() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        Assert.assertNotNull(lang);
    }

    @Test
    public void testGuessLanguage_English() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String text = "The quick brown fox jumps over the lazy dog";
        String expectedLanguage = "en";

        String actualLanguage = lang.guessLanguage(text);
        Assert.assertEquals(expectedLanguage, actualLanguage);
    }

    @Test
    public void testGuessLanguage_MultipleLanguages() {
        Lang lang = Lang.instance(NameType.GERMAN);
        String text = "Das ist ein Beispiel";
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList("de", "en"));

        Languages.LanguageSet actualLanguages = lang.guessLanguages(text);
        Assert.assertEquals(expectedLanguages, actualLanguages);
    }

    @Test
    public void testGuessLanguage_NoMatch() {
        Lang lang = Lang.instance(NameType.GERMAN);
        String text = "This is a test";
        Set<String> expectedLanguages = new HashSet<>(Arrays.asList(Languages.ANY));

        Languages.LanguageSet actualLanguages = lang.guessLanguages(text);
        Assert.assertEquals(expectedLanguages, actualLanguages);
    }

    @Test(expected = IllegalStateException.class)
    public void testInstance_InvalidNameType() {
        Lang lang = Lang.instance(new NameType("INVALID"));
    }

    @Test
    public void testLoadFromResource_EmptyRuleFile() {
        List<LangRule> rules = Lang.loadFromResource("", Languages.getInstance(NameType.ENGLISH));
        Assert.assertTrue(rules.isEmpty());
    }

    @Test
    public void testLoadFromResource_BasicRule() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_basic.txt";
        List<LangRule> rules = Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(NameType.ENGLISH));

        LangRule rule = rules.get(0);
        Pattern pattern = rule.getPattern();
        Set<String> languages = rule.getLanguages();
        boolean acceptOnMatch = rule.isAcceptOnMatch();

        Assert.assertEquals("a.*", pattern.pattern());
        Assert.assertEquals(Arrays.asList("en", "de"), languages.toArray(new String[0]));
        Assert.assertTrue(acceptOnMatch);
    }

    @Test
    public void testLoadFromResource_MultipleRules() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_multiple.txt";
        List<LangRule> rules = Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(NameType.ENGLISH));

        LangRule rule1 = rules.get(0);
        LangRule rule2 = rules.get(1);

        Pattern pattern1 = rule1.getPattern();
        Set<String> languages1 = rule1.getLanguages();
        boolean acceptOnMatch1 = rule1.isAcceptOnMatch();

        Pattern pattern2 = rule2.getPattern();
        Set<String> languages2 = rule2.getLanguages();
        boolean acceptOnMatch2 = rule2.isAcceptOnMatch();

        Assert.assertEquals("a.*", pattern1.pattern());
        Assert.assertEquals(Arrays.asList("en", "de"), languages1.toArray(new String[0]));
        Assert.assertTrue(acceptOnMatch1);

        Assert.assertEquals("b.*", pattern2.pattern());
        Assert.assertEquals(Arrays.asList("fr"), languages2.toArray(new String[0]));
        Assert.assertFalse(acceptOnMatch2);
    }

    @Test
    public void testGuessLanguage_EdgeCases() {
        Lang lang = Lang.instance(NameType.GERMAN);

        String text1 = "Das ist ein Beispiel mit ÄöÜäöüSSß und ßSSSS";
        Set<String> expectedLanguages1 = new HashSet<>(Arrays.asList("de", "en"));

        Languages.LanguageSet actualLanguages1 = lang.guessLanguages(text1);
        Assert.assertEquals(expectedLanguages1, actualLanguages1);

        String text2 = "Das ist ein Beispiel mit ÄöÜäöüSSß und ßSSSS unter strichgef├╝gter Schreibweise";
        Set<String> expectedLanguages2 = new HashSet<>(Arrays.asList("de"));

        Languages.LanguageSet actualLanguages2 = lang.guessLanguages(text2);
        Assert.assertEquals(expectedLanguages2, actualLanguages2);

        String text3 = "Das ist ein Beispiel mit äöüÄÖÜßß und ßSSSS unter strichgef├╝gter Schreibweise";
        Set<String> expectedLanguages3 = new HashSet<>(Arrays.asList("de", "en"));

        Languages.LanguageSet actualLanguages3 = lang.guessLanguages(text3);
        Assert.assertEquals(expectedLanguages3, actualLanguages3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadFromResource_MalformedRuleLine() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang_malformed.txt";
        Lang.loadFromResource(languageRulesResourceName, Languages.getInstance(NameType.ENGLISH));
    }
}