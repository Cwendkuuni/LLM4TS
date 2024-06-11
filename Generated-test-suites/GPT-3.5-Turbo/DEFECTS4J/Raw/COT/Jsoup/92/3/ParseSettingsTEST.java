import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

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
    assertEquals("div", settings.normalizeTag("DIV"));
  }

  @Test
  public void testNormalizeAttribute() {
    ParseSettings settings = new ParseSettings(false, false);
    assertEquals("class", settings.normalizeAttribute("CLASS"));
  }

  @Test
  public void testNormalizeAttributes() {
    ParseSettings settings = new ParseSettings(false, false);
    Attributes attributes = new Attributes();
    attributes.put("class", "example");
    Attributes normalized = settings.normalizeAttributes(attributes);
    assertTrue(normalized.hasKey("class"));
    assertEquals("example", normalized.get("class"));
  }
}