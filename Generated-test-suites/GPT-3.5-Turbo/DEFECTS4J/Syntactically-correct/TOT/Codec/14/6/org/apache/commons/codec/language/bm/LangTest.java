import org.junit.Assert;
import org.junit.Test;

public class LangTest {
  
  @Test
  public void testGuessLanguage_withValidText_returnsCorrectLanguage() {
    Lang lang = Lang.instance(NameType.SIMPLIFIED_CHINESE);
    String result = lang.guessLanguage("你好");
    Assert.assertEquals("zh", result);
  }
  
  @Test
  public void testGuessLanguage_withInvalidText_returnsAnyLanguage() {
    Lang lang = Lang.instance(NameType.ENGLISH);
    String result = lang.guessLanguage("abc123");
    Assert.assertEquals("*", result);
  }
  
  @Test
  public void testGuessLanguages_withValidText_returnsSetOfMatchingLanguages() {
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet result = lang.guessLanguages("hello");
    Assert.assertTrue(result.contains("en"));
    Assert.assertTrue(result.contains("es"));
  }
  
  @Test
  public void testGuessLanguages_withInvalidText_returnsAnyLanguage() {
    Lang lang = Lang.instance(NameType.JAPANESE);
    Languages.LanguageSet result = lang.guessLanguages("123");
    Assert.assertEquals(Languages.ANY_LANGUAGE, result);
  }
  
  @Test
  public void testGuessLanguages_withEmptyText_returnsAnyLanguage() {
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet result = lang.guessLanguages("");
    Assert.assertEquals(Languages.ANY_LANGUAGE, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testLoadFromResource_withInvalidResourceName_throwsIllegalArgumentException() {
    Lang.loadFromResource("invalid-resource-name.txt", Languages.getInstance(NameType.SIMPLIFIED_CHINESE));
  }
  
  @Test
  public void testLoadFromResource_withValidResourceName_returnsLangInstance() {
    Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.GENERIC));
    Assert.assertNotNull(lang);
  }
  
  @Test
  public void testInstance_withValidNameType_returnsLangInstance() {
    Lang lang = Lang.instance(NameType.GENERIC);
    Assert.assertNotNull(lang);
  }
  
  @Test
  public void testGuessLanguage_withNullText_returnsAnyLanguage() {
    Lang lang = Lang.instance(NameType.GENERIC);
    String result = lang.guessLanguage(null);
    Assert.assertEquals("*", result);
  }
  
  @Test
  public void testGuessLanguages_withNullText_returnsAnyLanguage() {
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet result = lang.guessLanguages(null);
    Assert.assertEquals(Languages.ANY_LANGUAGE, result);
  }
  
}