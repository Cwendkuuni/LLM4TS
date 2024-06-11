package org.jsoup.parser;
 import static org.junit.Assert.*;
 import org.jsoup.nodes.Attributes;
 import org.junit.Test;

 /**
 * Test class for ParseSettings.
 */
 public class ParseSettingsTest {

 /**
 * Test constructor.
 */
 @Test
 public void testParseSettingsConstructor() {
 ParseSettings htmlDefault = new ParseSettings(false, false);
 ParseSettings preserveCase = new ParseSettings(true, true);
 assertNotSame(htmlDefault, preserveCase);
 }

 /**
 * Test preserveTagCase() method.
 */
 @Test
 public void testPreserveTagCase() {
 ParseSettings parseSettings = new ParseSettings(true, false);
 assertTrue(parseSettings.preserveTagCase());
 }

 /**
 * Test preserveAttributeCase() method.
 */
 @Test
 public void testPreserveAttributeCase() {
 ParseSettings parseSettings = new ParseSettings(false, true);
 assertTrue(parseSettings.preserveAttributeCase());
 }

 /**
 * Test normalizeTag() method.
 */
 @Test
 public void testNormalizeTag() {
 ParseSettings parseSettings = new ParseSettings(true, false);
 String tagName = "MyTag";
 String normalizedTagName = parseSettings.normalizeTag(tagName);
 assertNull(normalizedTagName); // since preserveTagCase is true, no normalization should be done

 String lowerCaseTagName = parseSettings.normalizeTag("TAG");
 assertEquals("tag", lowerCaseTagName);

 String tagNameWithLeadingWhitespace = " \tMyTag ";
 String normalizedTagNameWithLeadingWhitespace = parseSettings.normalizeTag(tagNameWithLeadingWhitespace);
 assertEquals("mytag", normalizedTagNameWithLeadingWhitespace);
 }

 /**
 * Test normalizeAttribute() method.
 */
 @Test
 public void testNormalizeAttribute() {
 ParseSettings parseSettings = new ParseSettings(false, true);
 String attributeName = "MyAttribute";
 String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
 assertNull(normalizedAttributeName); // since preserveAttributeCase is false, no normalization should be done

 String lowerCaseAttributeName = parseSettings.normalizeAttribute("ATTRIBUTE");
 assertEquals("attribute", lowerCaseAttributeName);

 String attributeNameWithLeadingWhitespace = " \tMyAttribute ";
 String normalizedAttributeNameWithLeadingWhitespace = parseSettings.normalizeAttribute(attributeNameWithLeadingWhitespace);
 assertEquals("myattribute", normalizedAttributeNameWithLeadingWhitespace);
 }

 /**
 * Test normalizeAttributes() method.
 */
 @Test
 public void testNormalizeAttributes() {
 ParseSettings parseSettings = new ParseSettings(false, true);
 Attributes attributes = new Attributes();
 attributes.put("myAttribute", "value");
 attributes.put("MyAttribute", "anotherValue");

 Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());
 assertEquals("myattribute", normalizedAttributes.get("myattribute"));
 assertEquals("anothervalue", normalizedAttributes.get("myattribute"));
 }

 /**
 * Test normalizeAttributes() method with preserveAttributeCase set to true.
 */
 @Test
 public void testNormalizeAttributesPreserveAttributeCase() {
 ParseSettings parseSettings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("myattribute", "value");
 attributes.put("MyAttribute", "anotherValue");

 Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());
 assertEquals("myattribute", normalizedAttributes.get("myattribute").getName());
 assertEquals("anothervalue", normalizedAttributes.get("MyAttribute").getValue());
 }
 }