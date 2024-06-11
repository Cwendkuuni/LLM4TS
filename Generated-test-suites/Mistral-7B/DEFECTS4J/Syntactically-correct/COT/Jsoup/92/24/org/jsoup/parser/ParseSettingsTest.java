package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

 private ParseSettings htmlDefault;
 private ParseSettings preserveCase;

 @BeforeClass
 public static void setup() {
 htmlDefault = ParseSettings.htmlDefault;
 preserveCase = ParseSettings.preserveCase;
 }

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

 // Test preserveTagCase() method
 @Test
 public void testPreserveTagCase() {
 ParseSettings parseSettings = new ParseSettings(true, false);
 String tagName = "TAG";
 assertEquals(tagName, parseSettings.normalizeTag(tagName));
 }

 // Test preserveAttributeCase() method
 @Test
 public void testPreserveAttributeCase() {
 ParseSettings parseSettings = new ParseSettings(false, true);
 String attributeName = "ATTRIBUTE";
 assertEquals(attributeName, parseSettings.normalizeAttribute(attributeName));
 }

 // Test normalizeTag() method with lowerCase name
 @Test
 public void testNormalizeTagLowerCase() {
 ParseSettings parseSettings = new ParseSettings(false, false);
 String tagName = "TAG";
 assertEquals(lowerCase(tagName), parseSettings.normalizeTag(tagName));
 }

 // Test normalizeTag() method with upperCase name
 @Test
 public void testNormalizeTagUpperCase() {
 ParseSettings parseSettings = new ParseSettings(true, false);
 String tagName = "TAG";
 assertEquals(tagName, parseSettings.normalizeTag(tagName));
 }

 // Test normalizeTag() method with empty name
 @Test
 public void testNormalizeTagEmptyName() {
 ParseSettings parseSettings = new ParseSettings(false, false);
 String tagName = "";
 assertEquals("", parseSettings.normalizeTag(tagName));
 }

 // Test normalizeTag() method with null name
 @Test(expected = NullPointerException.class)
 public void testNormalizeTagNullName() {
 ParseSettings parseSettings = new ParseSettings(false, false);
 String tagName = null;
 parseSettings.normalizeTag(tagName);
 }

 // Test normalizeAttributes() method with preserveAttributeCase set to false
 @Test
 public void testNormalizeAttributesFalse() {
 ParseSettings parseSettings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("name1", "value1");
 attributes.put("name2", "VALUE2");
 Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
 assertFalse(normalizedAttributes.hasKey("name1"));
 assertFalse(normalizedAttributes.hasKey("name2"));
 }

 // Test normalizeAttributes() method with preserveAttributeCase set to true
 @Test
 public void testNormalizeAttributesTrue() {
 ParseSettings parseSettings = new ParseSettings(true, true);
 Attributes attributes = new Attributes();
 attributes.put("name1", "value1");
 attributes.put("name2", "VALUE2");
 Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
 assertTrue(normalizedAttributes.hasKey("name1"));
 assertTrue(normalizedAttributes.hasKey("name2"));
 assertEquals("VALUE2", normalizedAttributes.get("name2"));
 }
}