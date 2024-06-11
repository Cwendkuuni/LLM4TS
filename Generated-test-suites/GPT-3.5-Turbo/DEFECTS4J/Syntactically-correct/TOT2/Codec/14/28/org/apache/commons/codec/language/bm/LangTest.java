import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

    @Test
    public void testGuessLanguage_NormalCase() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String result = lang.guessLanguage("Hello");
        assertEquals("English", result);
    }

    @Test
    public void testGuessLanguage_EmptyInput() {
        Lang lang = Lang.instance(NameType.ENGLISH);
        String result = lang.guessLanguage("");
        assertEquals("any", result);
    }

    @Test
    public void testGuessLanguages_MultipleMatches() {
        Lang lang = Lang.instance(NameType.FRENCH);
        Languages.LanguageSet result = lang.guessLanguages("Bonjour");
        assertFalse(result.isSingleton());
    }

    @Test
    public void testGuessLanguages_NoMatch() {
        Lang lang = Lang.instance(NameType.SPANISH);
        Languages.LanguageSet result = lang.guessLanguages("こんにちは");
        assertEquals(Languages.NO_LANGUAGES, result);
    }

    @Test
    public void testLoadFromResource_InvalidResource() {
        try {
            Lang.loadFromResource("invalid_resource.txt", Languages.getInstance(NameType.GERMAN));
            fail("Should throw IllegalStateException for invalid resource");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testLangRuleMatches() {
        Pattern pattern = Pattern.compile("[a-z]+");
        Set<String> languages = new HashSet<>(Arrays.asList("English"));
        Lang.LangRule rule = new Lang.LangRule(pattern, languages, true);
        assertTrue(rule.matches("hello"));
    }

}