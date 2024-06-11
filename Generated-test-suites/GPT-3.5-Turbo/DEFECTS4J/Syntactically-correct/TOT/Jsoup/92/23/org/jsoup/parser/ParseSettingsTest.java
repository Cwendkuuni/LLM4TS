import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.*;

public class ParseSettingsTest {
  
  private ParseSettings parseSettings;
  
  @Before
  public void setUp() {
    parseSettings = new ParseSettings(true, true);
  }
  
  @Test
  public void testPreserveTagCase() {
    assertTrue(parseSettings.preserveTagCase());
    
    parseSettings = new ParseSettings(false, true);
    assertFalse(parseSettings.preserveTagCase());
  }
  
  @Test
  public void testPreserveAttributeCase() {
    assertTrue(parseSettings.preserveAttributeCase());
    
    parseSettings = new ParseSettings(true, false);
    assertFalse(parseSettings.preserveAttributeCase());
  }
  
  @Test
  public void testNormalizeTag() {
    String tagName = "div";
    assertEquals("div", parseSettings.normalizeTag(tagName));
    
    parseSettings = new ParseSettings(false, true);
    assertEquals("div", parseSettings.normalizeTag(tagName));
    
    tagName = "DIV";
    assertEquals("div", parseSettings.normalizeTag(tagName));
    
    parseSettings = new ParseSettings(true, false);
    assertEquals("DIV", parseSettings.normalizeTag(tagName));
  }
  
  @Test
  public void testNormalizeAttribute() {
    String attributeName = "class";
    assertEquals("class", parseSettings.normalizeAttribute(attributeName));
    
    parseSettings = new ParseSettings(false, true);
    assertEquals("class", parseSettings.normalizeAttribute(attributeName));
    
    attributeName = "CLASS";
    assertEquals("class", parseSettings.normalizeAttribute(attributeName));
    
    parseSettings = new ParseSettings(true, false);
    assertEquals("CLASS", parseSettings.normalizeAttribute(attributeName));
  }
  
  @Test
  public void testNormalizeAttributes() {
    Attributes attributes = new Attributes();
    attributes.put("class", "example");
    attributes.put("ID", "001");
    
    Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
    
    assertEquals("class", normalizedAttributes.asList().get(0).getKey());
    assertEquals("example", normalizedAttributes.asList().get(0).getValue());
    
    assertEquals("ID", normalizedAttributes.asList().get(1).getKey());
    assertEquals("001", normalizedAttributes.asList().get(1).getValue());
    
    parseSettings = new ParseSettings(false, true);
    normalizedAttributes = parseSettings.normalizeAttributes(attributes);
    
    assertEquals("class", normalizedAttributes.asList().get(0).getKey());
    assertEquals("example", normalizedAttributes.asList().get(0).getValue());
    
    assertEquals("id", normalizedAttributes.asList().get(1).getKey());
    assertEquals("001", normalizedAttributes.asList().get(1).getValue());
    
    parseSettings = new ParseSettings(true, false);
    normalizedAttributes = parseSettings.normalizeAttributes(attributes);
    
    assertEquals("CLASS", normalizedAttributes.asList().get(0).getKey());
    assertEquals("example", normalizedAttributes.asList().get(0).getValue());
    
    assertEquals("ID", normalizedAttributes.asList().get(1).getKey());
    assertEquals("001", normalizedAttributes.asList().get(1).getValue());
  }
}