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
    ParseSettings settings = new ParseSettings(true, false);
    String name = "DIV";
    String expected = "DIV";
    assertEquals(expected, settings.normalizeTag(name));
  }
  
  @Test
  public void testNormalizeAttribute() {
    ParseSettings settings = new ParseSettings(false, true);
    String name = "CLASS";
    String expected = "class";
    assertEquals(expected, settings.normalizeAttribute(name));
  }
  
  @Test
  public void testNormalizeAttributes() {
    ParseSettings settings = new ParseSettings(false, true);
    Attributes attributes = new Attributes();
    attributes.put("class", "myClass");
    Attributes normalized = settings.normalizeAttributes(attributes);
    assertTrue(normalized.hasKey("class"));
    assertEquals("class", normalized.get("class"));
  }
}