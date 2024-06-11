import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParseSettingsTest {
  
  private ParseSettings parseSettingsDefault;
  private ParseSettings parseSettingsPreserveCase;

  @Before
  public void setUp() {
    parseSettingsDefault = new ParseSettings(false, false);
    parseSettingsPreserveCase = new ParseSettings(true, true);
  }

  @Test
  public void testDefaultPreserveTagCase() {
    assertFalse(parseSettingsDefault.preserveTagCase());
  }

  @Test
  public void testDefaultPreserveAttributeCase() {
    assertFalse(parseSettingsDefault.preserveAttributeCase());
  }

  @Test
  public void testDefaultNormalizeTag() {
    String tagName = "DIv";
    assertEquals("div", parseSettingsDefault.normalizeTag(tagName));
  }

  @Test
  public void testDefaultNormalizeAttribute() {
    String attributeName = "ClAsS";
    assertEquals("class", parseSettingsDefault.normalizeAttribute(attributeName));
  }

  @Test
  public void testDefaultNormalizeAttributes() {
    Attributes attributes = new Attributes();
    attributes.put("ClAsS", "myClass");
    attributes.put("iD", "myId");
    attributes.put("NaMe", "myName");

    Attributes normalizedAttributes = parseSettingsDefault.normalizeAttributes(attributes);

    assertTrue(normalizedAttributes.hasKey("class"));
    assertTrue(normalizedAttributes.hasKey("id"));
    assertTrue(normalizedAttributes.hasKey("name"));

    assertEquals("myClass", normalizedAttributes.get("class"));
    assertEquals("myId", normalizedAttributes.get("id"));
    assertEquals("myName", normalizedAttributes.get("name"));
  }

  @Test
  public void testPreserveCasePreserveTagCase() {
    assertTrue(parseSettingsPreserveCase.preserveTagCase());
  }

  @Test
  public void testPreserveCasePreserveAttributeCase() {
    assertTrue(parseSettingsPreserveCase.preserveAttributeCase());
  }

  @Test
  public void testPreserveCaseNormalizeTag() {
    String tagName = "DIv";
    assertEquals("DIv", parseSettingsPreserveCase.normalizeTag(tagName));
  }

  @Test
  public void testPreserveCaseNormalizeAttribute() {
    String attributeName = "ClAsS";
    assertEquals("ClAsS", parseSettingsPreserveCase.normalizeAttribute(attributeName));
  }

  @Test
  public void testPreserveCaseNormalizeAttributes() {
    Attributes attributes = new Attributes();
    attributes.put("ClAsS", "myClass");
    attributes.put("iD", "myId");
    attributes.put("NaMe", "myName");

    Attributes normalizedAttributes = parseSettingsPreserveCase.normalizeAttributes(attributes);

    assertTrue(normalizedAttributes.hasKey("ClAsS"));
    assertTrue(normalizedAttributes.hasKey("iD"));
    assertTrue(normalizedAttributes.hasKey("NaMe"));

    assertEquals("myClass", normalizedAttributes.get("ClAsS"));
    assertEquals("myId", normalizedAttributes.get("iD"));
    assertEquals("myName", normalizedAttributes.get("NaMe"));
  }
}