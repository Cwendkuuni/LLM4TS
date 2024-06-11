import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
   
   @Test
   public void testGuessLanguage() {
      Lang lang = Lang.instance(NameType.GENERIC);
      
      //Typical use case
      String englishText = "Hello";
      String expectedLanguage = "en";
      String actualLanguage = lang.guessLanguage(englishText);
      assertEquals(expectedLanguage, actualLanguage);
      
      //Edge case: Empty input
      String emptyText = "";
      expectedLanguage = Languages.ANY;
      actualLanguage = lang.guessLanguage(emptyText);
      assertEquals(expectedLanguage, actualLanguage);
      
      //Error scenario: Unrecognizable language
      String unrecognizedText = "fsajfdioao";
      expectedLanguage = Languages.ANY;
      actualLanguage = lang.guessLanguage(unrecognizedText);
      assertEquals(expectedLanguage, actualLanguage);
   }
   
   @Test
   public void testGuessLanguages() {
      Lang lang = Lang.instance(NameType.GENERIC);
      
      //Typical use case
      String germanText = "Guten Tag";
      Languages.LanguageSet expectedLanguages = Languages.LanguageSet.from("de");
      Languages.LanguageSet actualLanguages = lang.guessLanguages(germanText);
      assertEquals(expectedLanguages, actualLanguages);
      
      //Edge case: Empty input
      String emptyText = "";
      expectedLanguages = Languages.ANY_LANGUAGE;
      actualLanguages = lang.guessLanguages(emptyText);
      assertEquals(expectedLanguages, actualLanguages);
      
      //Error scenario: Unrecognizable languages
      String unrecognizedText = "fsajfdioao";
      expectedLanguages = Languages.ANY_LANGUAGE;
      actualLanguages = lang.guessLanguages(unrecognizedText);
      assertEquals(expectedLanguages, actualLanguages);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testLoadFromResourceInvalidFile() {
      String invalidResource = "invalid_resource.txt";
      Languages languages = Languages.getInstance(NameType.GENERIC);
      Lang lang = Lang.loadFromResource(invalidResource, languages);
   }
   
   @Test
   public void testLoadFromResourceValidFile() {
      String validResource = "test_lang.txt";
      Languages languages = Languages.getInstance(NameType.GENERIC);
      Lang lang = Lang.loadFromResource(validResource, languages);
      
      //Typical use case: valid file
      String text = "Hello";
      String expectedLanguage = "en";
      String actualLanguage = lang.guessLanguage(text);
      assertEquals(expectedLanguage, actualLanguage);
      
      //Edge case: empty file
      validResource = "empty_file.txt";
      lang = Lang.loadFromResource(validResource, languages);
   }
}