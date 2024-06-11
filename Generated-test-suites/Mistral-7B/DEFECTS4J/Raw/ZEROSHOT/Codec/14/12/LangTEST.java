import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";
 private static final Languages LANGUAGES = Languages.getInstance(NameType.DEFAULT);

 @Test
 public void testLangInstance() {
 Lang lang = Lang.instance(NameType.DEFAULT);
 assertNotNull(lang);
 }

 @Test
 public void testLoadFromResource() {
 Lang lang = Lang.loadFromResource(LANGUAGE_RULES_RN, LANGUAGES);
 assertNotNull(lang);
 }

 @Test
 public void testGuessLanguage() {
 Lang lang = Lang.instance(NameType.DEFAULT);
 String input = "bonjour";
 String expected = "fr";
 String result = lang.guessLanguage(input);
 assertEquals(expected, result);
 }

 @Test
 public void testGuessLanguages() {
 Lang lang = Lang.instance(NameType.DEFAULT);
 String input = "bonjour";
 Set<String> expected = new HashSet<>(Arrays.asList("fr", "ok"));
 Set<String> result = lang.guessLanguages(input);
 assertEquals(expected, result);
 }

 @Test
 public void testLangRule() {
 LangRule rule = new LangRule(Pattern.compile("^bon"), new HashSet<>(Arrays.asList("fr")), true);
 String input = "bonjour";
 Set<String> expected = new HashSet<>(Arrays.asList("fr"));
 Set<String> result = new Lang(Arrays.asList(rule), LANGUAGES).guessLanguages(input);
 assertEquals(expected, result);
 }

 @Test
 public void testLangRuleAcceptOnMatchFalse() {
 LangRule rule = new LangRule(Pattern.compile("^bon"), new HashSet<>(Arrays.asList("fr")), false);
 String input = "bonjour";
 Set<String> expected = new HashSet<>(Arrays.asList("en"));
 Set<String> result = new Lang(Arrays.asList(rule), LANGUAGES).guessLanguages(input);
 assertEquals(expected, result);
 }
}