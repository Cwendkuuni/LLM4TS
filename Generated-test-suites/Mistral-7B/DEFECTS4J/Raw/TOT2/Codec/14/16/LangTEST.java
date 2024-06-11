import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.Languages;
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

 @Test
 public void testEnglishLanguage() {
 String text = "The quick brown fox jumps over the lazy dog.";
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 String expectedLanguage = Languages.ENGLISH;
 String actualLanguage = langInstance.guessLanguage(text);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testGermanLanguage() {
 String text = "Das schnelle rote Fuchs springt über den faulen Hund.";
 Lang langInstance = Lang.instance(Lang.NameType.GERMAN);
 String expectedLanguage = Languages.GERMAN;
 String actualLanguage = langInstance.guessLanguage(text);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testFrenchLanguage() {
 String text = "Le rapide renard rouge saute par-dessus le chien paresseux.";
 Lang langInstance = Lang.instance(Lang.NameType.FRENCH);
 String expectedLanguage = Languages.FRENCH;
 String actualLanguage = langInstance.guessLanguage(text);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testEmptyString() {
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 String actualLanguage = langInstance.guessLanguage("");
 assertEquals(Languages.ANY, actualLanguage);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullText() {
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 langInstance.guessLanguage(null);
 }

 @Test
 public void testMixedLanguage() {
 String text = "The quick brown fox jumps over the lazy dog. Die schnelle rote Fuchs springt über den faulen Hund.";
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 String expectedLanguage = Languages.MULTIPLE_LANGUAGES;
 String actualLanguage = langInstance.guessLanguage(text);
 assertEquals(expectedLanguage, actualLanguage);
 }

 @Test
 public void testRuleAcceptance() {
 String text = "Ich liebe dich."; // German: "I love you."
 Lang langInstance = Lang.instance(Lang.NameType.GERMAN);
 Set<String> expectedLanguages = new HashSet<>(Arrays.asList(Languages.GERMAN.getName()));
 Set<String> actualLanguages = langInstance.guessLanguages(text).getLanguages();
 assertTrue(expectedLanguages.containsAll(actualLanguages));
 }

 @Test
 public void testRuleRejection() {
 String text = "Ich liebe dich."; // German: "I love you."
 Lang langInstance = Lang.instance(Lang.NameType.ENGLISH);
 Set<String> expectedLanguages = new HashSet<>();
 Set<String> actualLanguages = langInstance.guessLanguages(text).getLanguages();
 assertTrue(expectedLanguages.isEmpty());
 }
}