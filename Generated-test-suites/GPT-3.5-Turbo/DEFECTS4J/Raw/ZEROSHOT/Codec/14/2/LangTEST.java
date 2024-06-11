import org.junit.*;
import java.util.*;
import java.util.regex.*;

public class LangTest {
    @Test
    public void testGuessLanguage() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String input = "hello";
        String expected = "eng";
        String result = lang.guessLanguage(input);
        assertEquals(expected, result);
    }

    @Test
    public void testGuessLanguages() {
        Lang lang = Lang.instance(NameType.GENERIC);
        String input = "hello";
        Languages.LanguageSet expected = Languages.LanguageSet.from("eng");
        Languages.LanguageSet result = lang.guessLanguages(input);
        assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        Pattern pattern = Pattern.compile("he");
        Set<String> languages = new HashSet<>(Arrays.asList("eng"));
        boolean acceptOnMatch = true;
        Lang.LangRule rule = new Lang.LangRule(pattern, languages, acceptOnMatch);
        String input = "hello";
        boolean expected = true;
        boolean result = rule.matches(input);
        assertEquals(expected, result);
    }

    @Test
    public void testLoadFromResource() {
        String languageRulesResourceName = "org/apache/commons/codec/language/bm/lang.txt";
        Languages languages = Languages.getInstance(NameType.GENERIC);
        Lang lang = Lang.loadFromResource(languageRulesResourceName, languages);
        assertNotNull(lang);
    }
}