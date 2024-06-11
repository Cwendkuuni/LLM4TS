package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 assertFalse(htmlDefault.preserveTagCase());
 assertFalse(htmlDefault.preserveAttributeCase());
 }

 @Test
 public void testPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 assertTrue(preserveCase.preserveTagCase());
 assertTrue(preserveCase.preserveAttributeCase());
 }

 @Test
 public void testNewParseSettings() {
 ParseSettings parseSettings1 = new ParseSettings(false, false);
 assertFalse(parseSettings1.preserveTagCase());
 assertFalse(parseSettings1.preserveAttributeCase());

 ParseSettings parseSettings2 = new ParseSettings(true, true);
 assertTrue(parseSettings2.preserveTagCase());
 assertTrue(parseSettings2.preserveAttributeCase());
 }

 @Test
 public void testNormalizeTag() {
 String tagName = "MyTag";
 ParseSettings parseSettings = new ParseSettings(true, false);
 String normalizedTagName = parseSettings.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);

 ParseSettings parseSettingsLower = new ParseSettings(false, false);
 String normalizedTagNameLower = parseSettingsLower.normalizeTag(tagName);
 assertEquals("mytag", normalizedTagNameLower);
 }

 @Test
 public void testNormalizeAttribute() {
 String attributeName = "myAttribute";
 ParseSettings parseSettings = new ParseSettings(false, true);
 String normalizedAttributeName = parseSettings.normalizeAttribute(attributeName);
 assertEquals(attributeName, normalizedAttributeName);

 ParseSettings parseSettingsUpper = new ParseSettings(false, false);
 String normalizedAttributeNameUpper = parseSettingsUpper.normalizeAttribute(attributeName);
 assertEquals("myattribute", normalizedAttributeNameUpper);
 }

 @Test
 public void testNormalizeAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("myAttribute", "value");
 attributes.put("MyAttribute", "value");

 ParseSettings parseSettings = new ParseSettings(false, true);
 Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());
 assertNull(normalizedAttributes.get("MyAttribute"));
 assertEquals("myattribute", normalizedAttributes.get("myattribute").getName());
 assertEquals("value", normalizedAttributes.get("myattribute").getValue());
 }
}