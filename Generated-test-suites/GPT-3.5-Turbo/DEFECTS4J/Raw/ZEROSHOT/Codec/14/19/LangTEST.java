import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {
  
  @Test
  public void testGuessLanguage() {
    Lang lang = Lang.instance(NameType.GENERIC);
    String result = lang.guessLanguage("hello");
    assertEquals("eng", result);
    
    result = lang.guessLanguage("bonjour");
    assertEquals("fre", result);
    
    result = lang.guessLanguage("hola");
    assertEquals("spa", result);
  }
  
  @Test
  public void testGuessLanguages() {
    Lang lang = Lang.instance(NameType.GENERIC);
    Languages.LanguageSet result = lang.guessLanguages("hello");
    assertEquals(1, result.getLanguages().size());
    assertTrue(result.getLanguages().contains("eng"));
    
    result = lang.guessLanguages("bonjour");
    assertEquals(1, result.getLanguages().size());
    assertTrue(result.getLanguages().contains("fre"));
    
    result = lang.guessLanguages("hola");
    assertEquals(1, result.getLanguages().size());
    assertTrue(result.getLanguages().contains("spa"));
  }
  
  @Test
  public void testInstance() {
    Lang lang = Lang.instance(NameType.GENERIC);
    assertNotNull(lang);
  }
  
  @Test
  public void testLoadFromResource() {
    Lang lang = Lang.loadFromResource(Lang.LANGUAGE_RULES_RN, Languages.getInstance(NameType.GENERIC));
    assertNotNull(lang);
  }
}