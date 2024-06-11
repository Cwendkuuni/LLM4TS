import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

  @Test
  public void testGuessLanguage() {
      Lang lang = Lang.instance(NameType.GENERIC);
      String text = "hello";
      String result = lang.guessLanguage(text);
      assertEquals("en", result);
  }

  @Test
  public void testGuessLanguages() {
      Lang lang = Lang.instance(NameType.GENERIC);
      String text = "hello world";
      Languages.LanguageSet result = lang.guessLanguages(text);
      assertTrue(result.contains("en"));
  }

  @Test
  public void testLoadFromResource() {
      Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.GENERIC));
      assertNotNull(lang);
  }
  
  // Edge case 1: Empty string
  @Test
  public void testEmptyString() {
      Lang lang = Lang.instance(NameType.GENERIC);
      String text = "";
      String result = lang.guessLanguage(text);
      assertEquals("und", result);
  }
  
  // Edge case 2: Input with special characters
  @Test
  public void testSpecialCharacters() {
      Lang lang = Lang.instance(NameType.GENERIC);
      String text = "h@@@@@@@3llo";
      String result = lang.guessLanguage(text);
      assertEquals("und", result);
  }
  
  // Exception handling scenario 1: Trying to guess language from null input
  @Test(expected = NullPointerException.class)
  public void testNullInput() {
      Lang lang = Lang.instance(NameType.GENERIC);
      String text = null;
      String result = lang.guessLanguage(text);
  }
  
  // Exception handling scenario 2: Trying to load language rules from non-existent resource
  @Test(expected = IllegalStateException.class)
  public void testInvalidResource() {
      Lang lang = Lang.loadFromResource("non/existent/resource.txt", Languages.getInstance(NameType.GENERIC));
  }

}