import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;
import org.junit.Assert.*;

public class LangTest {

 // Test case for the static factory method instance()
 @Test
 public void testInstance() {
 Lang lang = Lang.instance(NameType.ENGLISH);
 Assert.assertNotNull(lang);
 }

 // Test case for the loadFromResource() method with English language rules
 @Test
 public void testLoadFromResourceEnglishRules() {
 Lang englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.getInstance(NameType.ENGLISH));
 List<LangRule> englishRules = englishLang.getRules();
 // Basic functionality test for the first rule
 String text1 = "Hello";
 String expected1 = "English";
 assertTrue(englishLang.guessLanguage(text1).equals(expected1));
 // Test for edge cases and exception handling: empty string
 String text2 = "";
 String expected2 = Languages.ANY;
 assertEquals(expected2, englishLang.guessLanguage(text2));
 // Test for edge cases and exception handling: long text
 String text3 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
 String expected3 = Languages.ANY;
 assertEquals(expected3, englishLang.guessLanguage(text3));
 }

 // Test case for the loadFromResource() method with German language rules
 @Test
 public void testLoadFromResourceGermanRules() {
 Lang germanLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_de.txt", Languages.getInstance(NameType.GERMAN));
 List<LangRule> germanRules = germanLang.getRules();
 // Basic functionality test for the first rule
 String text4 = "Hallo";
 String expected4 = "German";
 assertTrue(germanLang.guessLanguage(text4).equals(expected4));
 // Test for edge cases and exception handling: empty string
 String text5 = "";
 String expected5 = Languages.ANY;
 assertEquals(expected5, germanLang.guessLanguage(text5));
 // Test for edge cases and exception handling: long text
 String text6 = "Hier ist ein sehr langer Text in der deutschen Sprache.";
 String expected6 = Languages.ANY;
 assertEquals(expected6, germanLang.guessLanguage(text6));
 }
}