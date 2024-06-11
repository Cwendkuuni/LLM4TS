import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String input = "hello world";
 Lang lang = Lang.instance(NameType.ENGLISH);
 assertEquals(Languages.ENGLISH, lang.guessLanguage(input));
 }

 @Test
 public void testGermanLanguage() {
 String input = "hallo welt";
 Lang lang = Lang.instance(NameType.GERMAN);
 assertEquals(Languages.GERMAN, lang.guessLanguage(input));
 }

 @Test
 public void testMixedLanguage() {
 String input = "hallo world";
 Lang lang = Lang.instance(NameType.GERMAN);
 Languages.LanguageSet languages = lang.guessLanguages(input);
 assertFalse(languages.isSingleton());
 assertTrue(languages.contains(Languages.GERMAN));
 assertTrue(languages.contains(Languages.ENGLISH));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidResource() {
 Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/invalidResource.txt", Languages.getInstance(NameType.ENGLISH));
 }

 @Test(expected = IllegalStateException.class)
 public void testMissingResource() {
 Lang lang = null;
 try {
 lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/missingResource.txt", Languages.getInstance(NameType.ENGLISH));
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(lang);
 }

 @Test
 public void testEmptyResource() {
 Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/emptyResource.txt", Languages.getInstance(NameType.ENGLISH));
 assertEquals(0, lang.rules.size());
 }

 @Test
 public void testMalformedRule() {
 // Add test for a malformed rule here
 }

 @Test
 public void testInvalidAcceptOnMatch() {
 // Add test for invalid acceptOnMatch value here
 }

 @Test
 public void testInvalidLanguageName() {
 // Add test for invalid language name here
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPattern() {
 // Add test for invalid pattern here
 }
}