import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 private static final Lang ENGLISH_LANGUAGE_RULES = Lang.instance(NameType.ENGLISH);

 @Test
 public void testEnglishGuessLanguage() {
 String text = "The quick brown fox jumps over the lazy dog";
 String expected = Languages.ENGLISH.getShortName();
 String actual = ENGLISH_LANGUAGE_RULES.guessLanguage(text);
 assertEquals(expected, actual);
 }

 @Test
 public void testEnglishGuessLanguages() {
 String text = "The quick brown fox jumps over the lazy dog";
 Set<String> expected = new HashSet<>(Arrays.asList(Languages.ENGLISH.getShortName(), Languages.DUTCH.getShortName()));
 Set<String> actual = ENGLISH_LANGUAGE_RULES.guessLanguages(text);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRuleFormat() {
 List<LangRule> rules = Arrays.asList(new LangRule(Pattern.compile(".*"), null, true));
 try {
 new Lang(rules, Languages.getInstance(NameType.ENGLISH));
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testMissingRuleForInput() {
 String text = "Hola";
 Set<String> actual = ENGLISH_LANGUAGE_RULES.guessLanguages(text);
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testEmptyInput() {
 String text = "";
 Set<String> actual = ENGLISH_LANGUAGE_RULES.guessLanguages(text);
 assertTrue(actual.isEmpty());
 }

 @Test
 public void testNullInput() {
 String text = null;
 Set<String> actual = ENGLISH_LANGUAGE_RULES.guessLanguages(text);
 assertTrue(actual.isEmpty());
 }
}