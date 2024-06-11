import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Assert;
import org.junit.Test;

public class LangTest {

 private static final Languages languages = Languages.getInstance();

 @Test
 public void testEnglishLanguage() {
 String text = "The quick brown fox jumps over the lazy dog.";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String expectedLanguage = Languages.ENGLISH.getName();
 String actualLanguage = langInstance.guessLanguage(text);
 Assert.assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testGermanLanguage() {
 String text = "Der schnelle grüne Fuchs springt über den faulen Hund.";
 Lang langInstance = Lang.instance(NameType.GERMAN);
 String expectedLanguage = Languages.GERMAN.getName();
 String actualLanguage = langInstance.guessLanguage(text);
 Assert.assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testUnknownLanguage() {
 String text = "यह रात्रि का चाondon बाग में है."; // Hindi
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String expectedLanguage = Languages.ANY.getName();
 String actualLanguage = langInstance.guessLanguage(text);
 Assert.assertEquals(expectedLanguage, actualLanguage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullInput() {
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String text = null;
 langInstance.guessLanguage(text);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyInput() {
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 String text = "";
 langInstance.guessLanguage(text);
 }

 @Test
 public void testMixedLanguageInput() {
 String text = "The quick brown fox jumps over the λαζύ δογ.";
 Lang langInstance = Lang.instance(NameType.ENGLISH);
 Languages.LanguageSet expectedLanguages = Languages.LanguageSet.of(Languages.ENGLISH, Languages.Greek);
 Set<String> actualLanguages = langInstance.guessLanguages(text).getLanguages();
 Assert.assertEquals(expectedLanguages, Languages.LanguageSet.from(actualLanguages));
 }
}