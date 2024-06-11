import org.junit.Assert;
import org.junit.Test;

public class LangTest {

  @Test
  public void testGuessLanguage_SingleMatch() {
    // Typical use case - word in English
    String word = "hello";
    Lang lang = Lang.instance(NameType.GENERIC);
    String expected = "eng";
    String actual = lang.guessLanguage(word);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGuessLanguage_MultipleMatches() {
    // Typical use case - word in multiple languages
    String word = "hola";
    Lang lang = Lang.instance(NameType.GENERIC);
    String expected = Languages.ANY;
    String actual = lang.guessLanguage(word);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGuessLanguage_NoMatch() {
    // Edge case - word with no match
    String word = "abcdefg";
    Lang lang = Lang.instance(NameType.GENERIC);
    String expected = Languages.ANY;
    String actual = lang.guessLanguage(word);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGuessLanguages_SingleMatch() {
    // Typical use case - word in English
    String word = "hello";
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet expected = Languages.LanguageSet.from("eng");
    Languages.LanguageSet actual = lang.guessLanguages(word);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGuessLanguages_MultipleMatches() {
    // Typical use case - word in multiple languages
    String word = "hola";
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet expected = Languages.ANY_LANGUAGE;
    Languages.LanguageSet actual = lang.guessLanguages(word);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGuessLanguages_NoMatch() {
    // Edge case - word with no match
    String word = "abcdefg";
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet expected = Languages.ANY_LANGUAGE;
    Languages.LanguageSet actual = lang.guessLanguages(word);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testLoadFromResource_ValidResource() {
    // Typical use case - loading language rules from a valid resource
    String resource = "org/apache/commons/codec/language/bm/lang.txt";
    Languages languages = Languages.getInstance(NameType.GENERIC);
    Lang lang = Lang.loadFromResource(resource, languages);
    Assert.assertNotNull(lang);
  }

  @Test(expected = IllegalStateException.class)
  public void testLoadFromResource_InvalidResource() {
    // Error scenario - loading language rules from an invalid resource
    String resource = "org/apache/commons/codec/language/bm/invalid.txt";
    Languages languages = Languages.getInstance(NameType.GENERIC);
    Lang.loadFromResource(resource, languages);
  }
  
}