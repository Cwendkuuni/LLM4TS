
import static org.junit.Assert.*;
import org.junit.Test;

public class ParseSettingsTest {
  
  @Test
  public void testPreserveTagCase() {
    ParseSettings settings = new ParseSettings(true, false);
    assertTrue(settings.preserveTagCase());
  }
  
  @Test
  public void testPreserveAttributeCase() {
    ParseSettings settings = new ParseSettings(false, true);
    assertTrue(settings.preserveAttributeCase());
  }
  
  @Test
  public void testNormalizeTag() {
    ParseSettings settings = new ParseSettings(false, false);
    String tag = settings.normalizeTag("DIV");
    assertEquals("div", tag);
  }
  
  @Test
  public void testNormalizeAttribute() {
    ParseSettings settings = new ParseSettings(false, false);
    String attribute = settings.normalizeAttribute("HREF");
    assertEquals("href", attribute);
  }
  
  @Test
  public void testNormalizeAttributes() {
    ParseSettings settings = new ParseSettings(false, false);
    Attributes attributes = new Attributes();
    attributes.put("HREF", "https://example.com");
    
    Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
    assertEquals("https://example.com", normalizedAttributes.get("href"));
  }
}
