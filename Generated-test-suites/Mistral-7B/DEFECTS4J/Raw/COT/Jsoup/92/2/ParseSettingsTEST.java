import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 /**
 * Test constructor with preserveTagCase and preserveAttributeCase set to false
 */
 @Test
 public void testParseSettingsConstructorWithFalseFalse() {
 ParseSettings settings = new ParseSettings(false, false);
 assertTrue(settings.preserveTagCase());
 assertFalse(settings.preserveAttributeCase());
 }

 /**
 * Test constructor with preserveTagCase and preserveAttributeCase set to true
 */
 @Test
 public void testParseSettingsConstructorWithTrueTrue() {
 ParseSettings settings = new ParseSettings(true, true);
 assertFalse(settings.preserveTagCase());
 assertTrue(settings.preserveAttributeCase());
 }

 /**
 * Test normalizeTag method with a tag name that is already lower cased
 */
 @Test
 public void testNormalizeTagWithLowerCaseName() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String tagName = "div";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);
 }

 /**
 * Test normalizeTag method with a tag name that is not lower cased
 */
 @Test
 public void testNormalizeTagWithUpperCaseName() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String tagName = "DIV";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);
 }

 /**
 * Test normalizeTag method with a tag name that is mixed cased
 */
 @Test
 public void testNormalizeTagWithMixedCasedName() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String tagName = "DIv";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals("div", normalizedTagName);
 }

 /**
 * Test normalizeAttribute method with an attribute name that is already lower cased
 */
 @Test
 public void testNormalizeAttributeWithLowerCaseName() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String attributeName = "class";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals(attributeName, normalizedAttributeName);
 }

 /**
 * Test normalizeAttribute method with an attribute name that is not lower cased
 */
 @Test
 public void testNormalizeAttributeWithUpperCaseName() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String attributeName = "CLASS";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals(attributeName, normalizedAttributeName);
 }

 /**
 * Test normalizeAttribute method with an attribute name that is mixed cased
 */
 @Test
 public void testNormalizeAttributeWithMixedCasedName() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String attributeName = "CLaSs";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals("class", normalizedAttributeName);
 }

 /**
 * Test normalizeAttributes method with attributes that have lower case names
 */
 @Test
 public void testNormalizeAttributesWithLowerCaseNames() {
 ParseSettings settings = ParseSettings.htmlDefault;
 Attributes attributes = new Attributes();
 attributes.put("class", "classValue");
 attributes.put("id", "idValue");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("class", normalizedAttributes.getName("class"));
 assertEquals("id", normalizedAttributes.getName("id"));
 }

 /**
 * Test normalizeAttributes method with attributes that have upper case names
 */
 @Test
 public void testNormalizeAttributesWithUpperCaseNames() {
 ParseSettings settings = ParseSettings.htmlDefault;
 Attributes attributes = new Attributes();
 attributes.put("CLASS", "classValue");
 attributes.put("ID", "idValue");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("class", normalizedAttributes.getName("CLASS"));
 assertEquals("id", normalizedAttributes.getName("ID"));
 }

 /**
 * Test normalizeAttributes method with attributes that have mixed cased names
 */
 @Test
 public void testNormalizeAttributesWithMixedCasedNames() {
 ParseSettings settings = ParseSettings.htmlDefault;
 Attributes attributes = new Attributes();
 attributes.put("CLaSs", "classValue");
 attributes.put("ID", "idValue");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("class", normalizedAttributes.getName("CLaSs"));
 assertEquals("id", normalizedAttributes.getName("ID"));
 }

 /**
 * Test normalizeAttributes method with attributes that have null values
 */
 @Test(expected = NullPointerException.class)
 public void testNormalizeAttributesWithNullAttributes() {
 ParseSettings settings = ParseSettings.htmlDefault;
 Attributes attributes = null;
 settings.normalizeAttributes(attributes);
 }

}