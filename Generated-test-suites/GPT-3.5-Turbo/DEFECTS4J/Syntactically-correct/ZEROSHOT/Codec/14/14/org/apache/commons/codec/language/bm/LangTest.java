import static org.junit.Assert.*;
import org.junit.Test;

public class LangTest {
  
  @Test
  public void testGuessLanguage() {
    Lang lang = Lang.instance(NameType.GENERIC);
    
    // Test with single language match
    String result = lang.guessLanguage("hello");
    assertEquals("eng", result);
    
    // Test with multiple language matches
    result = lang.guessLanguage("buenos dias");
    assertEquals("spa", result);
    
    // Test with no unique match
    result = lang.guessLanguage("abcdefg");
    assertEquals("und", result);
  }
  
  @Test
  public void testGuessLanguages() {
    Lang lang = Lang.instance(NameType.GENERIC);
    
    // Test with single language match
    Languages.LanguageSet result = lang.guessLanguages("hello");
    assertTrue(result.contains("eng"));
    
    // Test with multiple language matches
    result = lang.guessLanguages("buenos dias");
    assertTrue(result.contains("spa") && result.contains("cat"));
    
    // Test with no unique match
    result = lang.guessLanguages("abcdefg");
    assertTrue(result.isEmpty());
  }
  
  @Test
  public void testInstance() {
    Lang lang = Lang.instance(NameType.GENERIC);
    
    assertNotNull(lang);
  }
  
  @Test
  public void testLoadFromResource() {
    Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.GENERIC));
    
    assertNotNull(lang);
  }
}