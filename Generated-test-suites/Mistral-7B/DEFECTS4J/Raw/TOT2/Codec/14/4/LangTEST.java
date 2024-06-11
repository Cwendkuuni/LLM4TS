import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 // Test for basic functionality of Lang class construction
 @Test
 public void testLangInstance() {
 Lang lang = Lang.instance(NameType.ENGLISH);
 assertNotNull(lang);
 }

 // Test for basic functionality of guessLanguage method with English language rules
 @Test
 public void testGuessLanguageEnglish() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String englishWord = "hello";
 String expectedLanguage = Languages.ENGLISH;
 String actualLanguage = englishLang.guessLanguage(englishWord);
 assertEquals(expectedLanguage, actualLanguage);
 }

 // Test for basic functionality of guessLanguage method with other language rules
 @Test
 public void testGuessLanguageOther() {
 Lang otherLang = Lang.instance(NameType.FRENCH);
 String otherWord = "bonjour";
 String expectedLanguage = Languages.FRENCH;
 String actualLanguage = otherLang.guessLanguage(otherWord);
 assertEquals(expectedLanguage, actualLanguage);
 }

 // Test for edge cases: empty string
 @Test
 public void testGuessLanguageEmptyString() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String emptyString = "";
 String actualLanguage = englishLang.guessLanguage(emptyString);
 assertEquals(Languages.ANY, actualLanguage);
 }

 // Test for edge cases: null input
 @Test(expected = NullPointerException.class)
 public void testGuessLanguageNullInput() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String nullInput = null;
 englishLang.guessLanguage(nullInput);
 }

 // Test for edge cases: invalid rules in language resource file
 @Test(expected = IllegalArgumentException.class)
 public void testGuessLanguageInvalidRules() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String invalidWord = "invalid_rule";
 englishLang.guessLanguage(invalidWord);
 }

 // Test for exception handling: unsupported NameType
 @Test(expected = IllegalArgumentException.class)
 public void testGuessLanguageUnsupportedNameType() {
 Lang unsupportedLang = Lang.instance(new NameType("UNSUPPORTED"));
 String unsupportedWord = "unsupported_word";
 unsupportedLang.guessLanguage(unsupportedWord);
 }
}