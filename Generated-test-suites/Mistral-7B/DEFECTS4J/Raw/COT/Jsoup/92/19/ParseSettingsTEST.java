import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParseSettingsTest {

 // Test constructor
 @Test
 public void testParseSettingsConstructor() {
 ParseSettings parseSettings1 = new ParseSettings(false, false);
 ParseSettings parseSettings2 = new ParseSettings(true, true);
 assertEquals(false, parseSettings1.preserveTagCase());
 assertEquals(false, parseSettings1.preserveAttributeCase());
 assertEquals(true, parseSettings2.preserveTagCase());
 assertEquals(true, parseSettings2.preserveAttributeCase());
 }

 // Test getters
 @Test
 public void testGetPreserveTagCase() {
 ParseSettings parseSettings = new ParseSettings(true, false);
 assertEquals(true, parseSettings.preserveTagCase());
 }

 @Test
 public void testGetPreserveAttributeCase() {
 ParseSettings parseSettings = new ParseSettings(false, true);
 assertEquals(true, parseSettings.preserveAttributeCase());
 }

 // Test normalizeTag method
 @Test
 public void testNormalizeTag() {
 ParseSettings parseSettings = new ParseSettings(true, false);
 assertEquals("TAG", parseSettings.normalizeTag("TAG"));
 assertEquals("tag", parseSettings.normalizeTag("tag"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullNameInNormalizeTag() {
 ParseSettings parseSettings = new ParseSettings(true, false);
 parseSettings.normalizeTag(null);
 }

 // Test normalizeAttribute method
 @Test
 public void testNormalizeAttribute() {
 ParseSettings parseSettings = new ParseSettings(false, true);
 assertEquals("ATTRIBUTE", parseSettings.normalizeAttribute("ATTRIBUTE"));
 assertEquals("attribute", parseSettings.normalizeAttribute("attribute"));
 }

 @Test(expected = NullPointerException.class)
 public void testNullNameInNormalizeAttribute() {
 ParseSettings parseSettings = new ParseSettings(false, true);
 parseSettings.normalizeAttribute(null);
 }

 // Test normalizeAttributes method
 @Test
 public void testNormalizeAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("attribute1", "value1");
 attributes.put("Attribute2", "value2");
 ParseSettings parseSettings = new ParseSettings(false, false);
 Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
 assertEquals("attribute1", normalizedAttributes.get("attribute1"));
 assertEquals("Attribute2", normalizedAttributes.get("Attribute2"));
 }

 @Test
 public void testNormalizeAttributesPreserveCase() {
 Attributes attributes = new Attributes();
 attributes.put("attribute1", "value1");
 attributes.put("Attribute2", "value2");
 ParseSettings parseSettings = new ParseSettings(true, true);
 Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
 assertEquals("attribute1", normalizedAttributes.get("attribute1"));
 assertEquals("Attribute2", normalizedAttributes.get("Attribute2"));
 }
}