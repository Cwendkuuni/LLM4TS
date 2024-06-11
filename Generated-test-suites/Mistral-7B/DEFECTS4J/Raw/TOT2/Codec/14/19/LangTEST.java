import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testGuessLanguageEnglish() {
 String text = "hello world";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages expected = Languages.ENGLISH;
 Languages actual = langInstance.guessLanguages(text).getSingleton();
 assertEquals(expected, actual);
 }

 @Test
 public void testGuessLanguageSpanish() {
 String text = "hola mundo";
 Lang langInstance = Lang.instance(NameType.SPANISH);
 Languages expected = Languages.SPANISH;
 Languages actual = langInstance.guessLanguages(text).getSingleton();
 assertEquals(expected, actual);
 }

 @Test
 public void testGuessLanguageEmptyString() {
 String text = "";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages expected = Languages.ANY;
 Languages actual = langInstance.guessLanguages(text);
 assertEquals(expected, actual);
 }

 @Test(expected=IllegalArgumentException.class)
 public void testGuessLanguageInvalidResource() {
 String invalidResourceName = "org/apache/commons/codec/language/bm/invalid.txt";
 Lang langInstance = null;
 try {
 langInstance = Lang.loadFromResource(invalidResourceName, Languages.getInstance(NameType.ENGLISH));
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGuessLanguageInvalidPattern() {
 String text = "abcd";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 try {
 langInstance = new Lang(Arrays.asList(new LangRule(Pattern.compile(".*ab(cd|xyz)"), new HashSet<String>(Arrays.asList("en", "es")), true)), languages);
 fail("Expected an IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGuessLanguageMultipleMatches() {
 String text = "hola world";
 Lang langInstance = Lang.instance(NameType.ANY);
 Languages expected = new HashSet<String>(Arrays.asList("en", "es"));
 Languages actual = langInstance.guessLanguages(text);
 assertEquals(expected, actual);
 }

 @Test
 public void testGuessLanguageAcceptOnMatch() {
 String text = "hello there";
 Lang langInstance = new Lang(Arrays.asList(new LangRule(Pattern.compile("hello"), new HashSet<String>(Arrays.asList("en")), true)), Languages.getInstance(NameType.ENGLISH));
 Languages expected = Languages.ENGLISH;
 Languages actual = langInstance.guessLanguages(text).getSingleton();
 assertEquals(expected, actual);
 }

 @Test
 public void testGuessLanguageRejectOnMatch() {
 String text = "hola there";
 Lang langInstance = new Lang(Arrays.asList(new LangRule(Pattern.compile("hello"), new HashSet<String>(Arrays.asList("en")), false)), Languages.getInstance(NameType.ENGLISH));
 Languages expected = Languages.ANY;
 Languages actual = langInstance.guessLanguages(text);
 assertEquals(expected, actual);
 }

}