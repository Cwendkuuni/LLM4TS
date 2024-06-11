import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LangTest {
  
  @Test
  public void testGuessLanguage_basic() {
    Lang lang = Lang.instance(NameType.COMMON);
    
    assertEquals("en", lang.guessLanguage("hello"));
    assertEquals("fr", lang.guessLanguage("bonjour"));
    assertEquals("es", lang.guessLanguage("hola"));
  }
  
  @Test
  public void testGuessLanguage_edgeCases() {
    // Empty input should return ANY language
    Lang lang = Lang.instance(NameType.COMMON);
    assertEquals("##ANY", lang.guessLanguage(""));
    
    // Non-lowercase input should be converted to lowercase for matching
    assertEquals("en", lang.guessLanguage("Hello"));
  }
  
  @Test
  public void testGuessLanguages_basic() {
    Lang lang = Lang.instance(NameType.COMMON);
    
    Languages.LanguageSet ls1 = lang.guessLanguages("hello");
    assertTrue(ls1.contains("en"));
    
    Languages.LanguageSet ls2 = lang.guessLanguages("bonjour");
    assertTrue(ls2.contains("fr"));
  }
  
  @Test
  public void testGuessLanguages_edgeCases() {
    // Non-lowercase input should be converted to lowercase for matching
    Lang lang = Lang.instance(NameType.COMMON);
    
    Languages.LanguageSet ls = lang.guessLanguages("Hello");
    assertTrue(ls.contains("en"));
  }
  
  @Test
  public void testGuessLanguages_exceptionHandling() {
    // Test for null input
    Lang lang = Lang.instance(NameType.COMMON);
    Languages.LanguageSet ls = lang.guessLanguages(null);
    
    // The guessLanguages method should return ANY_LANGUAGE for null input
    assertTrue(ls.equals(Languages.ANY_LANGUAGE));
  }
  
  @Test
  public void testLoadFromResource() {
    // Test loading language rules from a resource
    Lang lang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang.txt", Languages.getInstance(NameType.COMMON));
    
    // Verify that the loaded language rules can correctly guess languages
    assertEquals("en", lang.guessLanguage("hello"));
    assertEquals("fr", lang.guessLanguage("bonjour"));
  }
}