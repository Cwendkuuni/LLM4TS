package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 assertEquals(false, htmlDefault.preserveTagCase());
 assertEquals(false, htmlDefault.preserveAttributeCase());
 }

 @Test
 public void testPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 assertEquals(true, preserveCase.preserveTagCase());
 assertEquals(true, preserveCase.preserveAttributeCase());
 }

 @Test
 public void testConstructPreserveTagCase() {
 ParseSettings settings = new ParseSettings(true, false);
 assertEquals(true, settings.preserveTagCase());
 assertEquals(false, settings.preserveAttributeCase());
 }

 @Test
 public void testConstructPreserveAttributeCase() {
 ParseSettings settings = new ParseSettings(false, true);
 assertEquals(false, settings.preserveTagCase());
 assertEquals(true, settings.preserveAttributeCase());
 }

 @Test
 public void testConstructPreserveBoth() {
 ParseSettings settings = new ParseSettings(true, true);
 assertEquals(true, settings.preserveTagCase());
 assertEquals(true, settings.preserveAttributeCase());
 }

 @Test
 public void testNormalizeTag() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = "TAG";
 String normalizedTag = settings.normalizeTag(tagName);
 assertEquals(tagName, normalizeTag); // Since preserveTagCase is true, tagName should not be modified.

 tagName = tagName.toLowerCase();
 settings = new ParseSettings(false, false);
 normalizedTag = settings.normalizeTag(tagName);
 assertEquals("tag", normalizedTag); // Since preserveTagCase is false, tagName should be lower-cased.
 }

 @Test
 public void testNormalizeAttribute() {
 ParseSettings settings = new ParseSettings(true, true);
 String attributeName = "Attribute";
 String normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertEquals(attributeName, normalizedAttribute); // Since preserveAttributeCase is true, attributeName should not be modified.

 attributeName = attributeName.toLowerCase();
 settings = new ParseSettings(false, false);
 normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertEquals("attribute", normalizedAttribute); // Since preserveAttributeCase is false, attributeName should be lower-cased.
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("Attribute1", "Value1");
 attributes.put("Attribute2", "Value2");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());

 String normalizedAttribute1 = normalizedAttributes.get("attribute1");
 assertEquals("value1", normalizedAttribute1);

 String normalizedAttribute2 = normalizedAttributes.get("attribute2");
 assertEquals("value2", normalizedAttribute2);
 }

 @Test
 public void testNormalizeAttributesPreserveCase() {
 ParseSettings settings = new ParseSettings(true, true);
 Attributes attributes = new Attributes();
 attributes.put("Attribute1", "Value1");
 attributes.put("Attribute2", "Value2");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());

 String normalizedAttribute1 = normalizedAttributes.get("Attribute1");
 assertEquals("Value1", normalizedAttribute1);

 String normalizedAttribute2 = normalizedAttributes.get("Attribute2");
 assertEquals("Value2", normalizedAttribute2);
 }

 @Test
 public void testNormalizeAttributesMixedCase() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("Attribute1", "Value1");
 attributes.put("ATTRIBUTE2", "Value2");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());

 String normalizedAttribute1 = normalizedAttributes.get("attribute1");
 assertEquals("value1", normalizedAttribute1);

 String normalizedAttribute2 = normalizedAttributes.get("attribute2");
 assertEquals("value2", normalizedAttribute2);
 }

 @Test
 public void testNormalizeAttributesPreserveTagCaseAndAttributeCase() {
 ParseSettings settings = new ParseSettings(true, true);
 Attributes attributes = new Attributes();
 attributes.put("Attribute1", "Value1");
 attributes.put("ATTRIBUTE2", "Value2");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());

 String normalizedAttribute1 = normalizedAttributes.get("Attribute1");
 assertEquals("Value1", normalizedAttribute1);

 String normalizedAttribute2 = normalizedAttributes.get("ATTRIBUTE2");
 assertEquals("ATTRIBUTE2", normalizedAttribute2);
 }
}