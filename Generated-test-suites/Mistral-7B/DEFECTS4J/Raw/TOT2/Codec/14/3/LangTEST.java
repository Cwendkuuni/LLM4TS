import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String text = "The quick brown fox jumps over the lazy dog.";
 Lang lang = Lang.instance(Lang.NameType.ENGLISH);
 assertEquals(Lang.NameType.ENGLISH.toString(), lang.guessLanguage(text));
 }

 @Test
 public void testGermanLanguage() {
 String text = "Das schnelle rote Fuchs springt über den faulen Hund.";
 Lang lang = Lang.instance(Lang.NameType.GERMAN);
 assertEquals(Lang.NameType.GERMAN.toString(), lang.guessLanguage(text));
 }

 @Test
 public void testMixedLanguage() {
 String text = "Das schnelle rote Fuchs springt über den faulen Hund. The quick brown fox jumps over the lazy dog.";
 Lang lang = Lang.instance(Lang.NameType.ENGLISH);
 Languages.LanguageSet languages = lang.guessLanguages(text);
 assertEquals(Lang.NameType.ENGLISH.toString(), languages.getAny());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 Lang lang = Lang.instance(Lang.NameType.ENGLISH);
 lang.guessLanguage(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 Lang lang = Lang.instance(Lang.NameType.ENGLISH);
 lang.guessLanguage("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidRuleFile() {
 try {
 Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/invalidLang.txt", Languages.getInstance(Lang.NameType.ENGLISH));
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidFormat() {
 try {
 String invalidFormat = "pattern languages acceptOnMatch";
 LangRule rule = new LangRule(Pattern.compile(invalidFormat), new HashSet<String>(), true);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMissingLanguage() {
 Lang lang = Lang.instance(Lang.NameType.MISSING_LANGUAGE);
 }
}