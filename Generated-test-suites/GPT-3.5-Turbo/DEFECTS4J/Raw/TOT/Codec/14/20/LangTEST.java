import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LangTest {
  
  private Lang lang;
  
  @Before
  public void setUp() {
    lang = Lang.instance(NameType.GENERIC);
  }
  
  @Test
  public void testGuessLanguage() {
    // Typical use case
    String result = lang.guessLanguage("hello");
    assertEquals("eng", result);
    
    // Edge case: empty input
    result = lang.guessLanguage("");
    assertEquals("any", result);
    
    // Edge case: input with special characters
    result = lang.guessLanguage("öäü");
    assertEquals("deu", result);
    
    // Error scenario: null input
    result = lang.guessLanguage(null);
    assertNull(result);
  }
  
  @Test
  public void testGuessLanguages() {
    // Typical use case
    Languages.LanguageSet result = lang.guessLanguages("hello");
    assertTrue(result.contains("eng"));
    
    // Edge case: empty input
    result = lang.guessLanguages("");
    assertTrue(result.isAny());
    
    // Edge case: input with special characters
    result = lang.guessLanguages("öäü");
    assertTrue(result.contains("deu"));
    
    // Error scenario: null input
    result = lang.guessLanguages(null);
    assertTrue(result.isNone());
  }
  
  @Test
  public void testLoadFromResource() {
    // Typical use case
    Lang result = Lang.loadFromResource("path/to/resource", new Languages());
    assertNotNull(result);
    
    // Error scenario: resource not found
    try {
      result = Lang.loadFromResource("nonexistent/resource", new Languages());
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      assertEquals("Unable to resolve required resource: nonexistent/resource", e.getMessage());
    }
  }
}