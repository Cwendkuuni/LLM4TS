import org.junit.Test;
import org.junit.Assert;
import org.jsoup.parser.ParseSettings;
import static org.jsoup.internal.Normalizer.lowerCase;

public class ParseSettingsTest {

 private ParseSettings htmlDefault;
 private ParseSettings preserveCase;

 @Before
 public void setup() {
 htmlDefault = ParseSettings.htmlDefault;
 preserveCase = ParseSettings.preserveCase;
 }

 // Test basic functionality of constructor
 @Test
 public void testParseSettingsConstructor() {
 ParseSettings parseSettings1 = new ParseSettings(false, false);
 Assert.assertFalse(parseSettings1.preserveTagCase());
 Assert.assertFalse(parseSettings1.preserveAttributeCase());

 ParseSettings parseSettings2 = new ParseSettings(true, true);
 Assert.assertTrue(parseSettings2.preserveTagCase());
 Assert.assertTrue(parseSettings2.preserveAttributeCase());
 }

 // Test normalizeTag method
 @Test
 public void testNormalizeTag() {
 String tagName1 = "TAG";
 String tagName2 = "tag";
 String tagName3 = "Tag";
 String tagName4 = "TAG";

 ParseSettings settings1 = ParseSettings.htmlDefault;
 ParseSettings settings2 = ParseSettings.preserveCase;

 String normalizedTagName1 = settings1.normalizeTag(tagName1);
 String normalizedTagName2 = settings2.normalizeTag(tagName2);
 String normalizedTagName3 = settings1.normalizeTag(tagName3);
 String normalizedTagName4 = settings2.normalizeTag(tagName4);

 Assert.assertEquals(lowerCase(tagName1), normalizedTagName1);
 Assert.assertEquals(tagName2, normalizedTagName2);
 Assert.assertEquals(lowerCase(tagName3), normalizedTagName3);
 Assert.assertEquals(tagName4, normalizedTagName4);
 }

 // Test normalizeAttribute method
 @Test
 public void testNormalizeAttribute() {
 String attributeName1 = "ATTRIBUTE";
 String attributeName2 = "attribute";
 String attributeName3 = "Attribute";
 String attributeName4 = "ATTRIBUTE";

 ParseSettings settings1 = ParseSettings.htmlDefault;
 ParseSettings settings2 = ParseSettings.preserveCase;

 String normalizedAttributeName1 = settings1.normalizeAttribute(attributeName1);
 String normalizedAttributeName2 = settings2.normalizeAttribute(attributeName2);
 String normalizedAttributeName3 = settings1.normalizeAttribute(attributeName3);
 String normalizedAttributeName4 = settings2.normalizeAttribute(attributeName4);

 Assert.assertEquals(lowerCase(attributeName1), normalizedAttributeName1);
 Assert.assertEquals(attributeName2, normalizedAttributeName2);
 Assert.assertEquals(lowerCase(attributeName3), normalizedAttributeName3);
 Assert.assertEquals(attributeName4, normalizedAttributeName4);
 }

 // Test normalizeAttributes method
 @Test
 public void testNormalizeAttributes() {
 String attributeName1 = "attribute1";
 String attributeName2 = "ATTRIBUTE2";
 String attributeName3 = "attribute3";
 String attributeName4 = "Attribute4";

 Attributes attributes = new Attributes();
 attributes.put("attribute1", "value1");
 attributes.put("ATTRIBUTE2", "value2");
 attributes.put("attribute3", "value3");
 attributes.put("Attribute4", "value4");

 ParseSettings settings = ParseSettings.htmlDefault;

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

 Assert.assertEquals(4, normalizedAttributes.size());
 String normalizedAttributeName1 = normalizedAttributes.get("attribute1");
 String normalizedAttributeName2 = normalizedAttributes.get("ATTRIBUTE2");
 String normalizedAttributeName3 = normalizedAttributes.get("attribute3");
 String normalizedAttributeName4 = normalizedAttributes.get("Attribute4");

 Assert.assertEquals(attributeName1, normalizedAttributeName1);
 Assert.assertEquals(lowerCase(attributeName2), normalizedAttributeName2);
 Assert.assertEquals(attributeName3, normalizedAttributeName3);
 Assert.assertEquals(lowerCase(attributeName4), normalizedAttributeName4);
 }

 // Test edge cases and exceptions
 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 String tagName = null;
 ParseSettings settings = ParseSettings.htmlDefault;
 settings.normalizeTag(tagName);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributes() {
 Attributes attributes = null;
 ParseSettings settings = ParseSettings.htmlDefault;
 settings.normalizeAttributes(attributes);
 }

 @Test(expected = NullPointerException.class)
 public void testNullParseSettings() {
 ParseSettings settings = null;
 String tagName = "TAG";
 settings.normalizeTag(tagName);
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributesValues() {
 Attributes attributes = new Attributes();
 attributes.put(null, "value");
 ParseSettings settings = ParseSettings.htmlDefault;
 settings.normalizeAttributes(attributes);
 }
}