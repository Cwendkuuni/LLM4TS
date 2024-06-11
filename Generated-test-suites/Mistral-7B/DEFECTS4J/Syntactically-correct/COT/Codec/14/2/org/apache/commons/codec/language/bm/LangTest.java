import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 private Lang lang;

 @Before
 public void setUp() {
 lang = Lang.instance(NameType.DEFAULT);
 }

 @Test
 public void testGuessLanguage_English() {
 String input = "the quick brown fox jumps over the lazy dog";
 String expected = Languages.ENGLISH.getLanguageName();
 String actual = lang.guessLanguage(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testGuessLanguage_Spanish() {
 String input = "el gato lavo la mano a la perro";
 String expected = Languages.SPANISH.getLanguageName();
 String actual = lang.guessLanguage(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testGuessLanguage_Unsupported() {
 String input = "こんにちは";
 String expected = Languages.ANY.getLanguageName();
 String actual = lang.guessLanguage(input);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGuessLanguage_NullInput() {
 String input = null;
 String actual = lang.guessLanguage(input);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testGuessLanguages_English() {
 String input = "the quick brown fox jumps over the lazy dog";
 Languages.LanguageSet expected = Languages.newLanguageSet(Languages.ENGLISH);
 Languages.LanguageSet actual = lang.guessLanguages(input);
 assertTrue(expected.isEqual(actual));
 }

 @Test
 public void testGuessLanguages_MultipleLanguages() {
 String input = "le renard rapide brun saute par-dessus le chien paresseux";
 Languages.LanguageSet expected = Languages.newLanguageSet(Languages.FRENCH, Languages.ENGLISH);
 Languages.LanguageSet actual = lang.guessLanguages(input);
 assertTrue(expected.isEqual(actual));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGuessLanguages_NullInput2() {
 String input = null;
 Languages.LanguageSet actual = lang.guessLanguages(input);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test
 public void testLoadFromResource_English() {
 InputStream inputStream = Lang.class.getClassLoader().getResourceAsStream("org/apache/commons/codec/language/bm/lang_en.txt");
 Lang englishLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_en.txt", Languages.ENGLISH);
 assertEquals(10, englishLang.getRulesCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testLoadFromResource_MissingResource() {
 String resourceName = "org/apache/commons/codec/language/bm/lang_non_existent.txt";
 try {
 Lang lang = Lang.loadFromResource(resourceName, Languages.getInstance(NameType.DEFAULT));
 fail("Expected IllegalArgumentException but did not get one.");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

}