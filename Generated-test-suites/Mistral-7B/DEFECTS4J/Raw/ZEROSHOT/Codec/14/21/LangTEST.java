import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 private static final String LANGUAGE_RULES_RN = "org/apache/commons/codec/language/bm/lang.txt";

 @Test
 public void testLangInstance() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 assertEquals(NameType.ENGLISH, englishLang.getNameType());
 }

 @Test
 public void testLoadFromResource() {
 Languages enLangs = Languages.getInstance(NameType.ENGLISH);
 Lang englishLang = Lang.loadFromResource(LANGUAGE_RULES_RN, enLangs);
 assertEquals(englishLang, Lang.instance(NameType.ENGLISH));
 }

 @Test
 public void testGuessLanguageEnglish() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String word = "hello";
 String expectedLanguage = "en";
 String actualLanguage = englishLang.guessLanguage(word);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testGuessLanguageOtherLanguage() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String word = "hola";
 String expectedLanguage = "es";
 String actualLanguage = englishLang.guessLanguage(word);
 assertNotEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testGuessLanguagesEnglish() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String word = "hello";
 Languages.LanguageSet expectedLanguages = Languages.languageSet("en");
 Languages.LanguageSet actualLanguages = englishLang.guessLanguages(word);
 assertEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testGuessLanguagesOtherLanguage() {
 Lang englishLang = Lang.instance(NameType.ENGLISH);
 String word = "hola";
 Languages.LanguageSet expectedLanguages = Languages.languageSet("es");
 Languages.LanguageSet actualLanguages = englishLang.guessLanguages(word);
 assertNotEquals(expectedLanguages, actualLanguages);
 }

 @Test
 public void testLoadFromResourceWithCustomLang() {
 // Assuming you have a custom lang file named "customlang.txt" in the classpath
 String customLangResourceName = "org/apache/commons/codec/language/bm/customlang.txt";
 Languages customLangs = Languages.getInstance(NameType.CUSTOM);
 Lang customLang = Lang.loadFromResource(customLangResourceName, customLangs);
 // Perform tests for customLang instance here
 }
}