package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;

import org.junit.Test;

/**
 * Test class for ParseSettings.
 */
public class ParseSettingsTest {

 private ParseSettings htmlDefault;
 private ParseSettings preserveCase;

 @BeforeClass
 public static void setup() {
 ParseSettings.htmlDefault = new ParseSettings(false, false);
 ParseSettings.preserveCase = new ParseSettings(true, true);
 }

 @Before
 public void setUp() {
 htmlDefault = ParseSettings.htmlDefault;
 preserveCase = ParseSettings.preserveCase;
 }

 /**
 * Test constructor.
 */
 @Test
 public void testParseSettingsConstructor() {
 ParseSettings parseSettings1 = new ParseSettings(true, false);
 ParseSettings parseSettings2 = new ParseSettings(false, true);

 assertEquals(true, parseSettings1.preserveTagCase());
 assertEquals(false, parseSettings1.preserveAttributeCase());

 assertEquals(false, parseSettings2.preserveTagCase());
 assertEquals(true, parseSettings2.preserveAttributeCase());
 }

 /**
 * Test normalizeTag method.
 */
 @Test
 public void testNormalizeTag() {
 // Test lower case tag name
 String tagNameLowerCase = "div";
 String normalizedTagNameLowerCase = htmlDefault.normalizeTag(tagNameLowerCase);
 assertEquals(tagNameLowerCase, normalizedTagNameLowerCase);

 // Test upper case tag name
 String tagNameUpperCase = "DIV";
 String normalizedTagNameUpperCase = htmlDefault.normalizeTag(tagNameUpperCase);
 assertEquals(tagNameLowerCase, normalizedTagNameUpperCase);

 // Test tag name with preserve tag case
 String tagNamePreserveCase = "DIV";
 String normalizedTagNamePreserveCase = preserveCase.normalizeTag(tagNamePreserveCase);
 assertEquals(tagNamePreserveCase, normalizedTagNamePreserveCase);
 }

 /**
 * Test normalizeAttribute method.
 */
 @Test
 public void testNormalizeAttribute() {
 // Test lower case attribute name
 String attributeNameLowerCase = "class";
 String normalizedAttributeNameLowerCase = htmlDefault.normalizeAttribute(attributeNameLowerCase);
 assertEquals(attributeNameLowerCase, normalizedAttributeNameLowerCase);

 // Test upper case attribute name
 String attributeNameUpperCase = "CLASS";
 String normalizedAttributeNameUpperCase = htmlDefault.normalizeAttribute(attributeNameUpperCase);
 assertEquals(attributeNameLowerCase, normalizedAttributeNameUpperCase);

 // Test attribute name with preserve attribute case
 String attributeNamePreserveCase = "CLASS";
 String normalizedAttributeNamePreserveCase = preserveCase.normalizeAttribute(attributeNamePreserveCase);
 assertEquals(attributeNamePreserveCase, normalizedAttributeNamePreserveCase);
 }

 /**
 * Test normalizeAttributes method.
 */
 @Test
 public void testNormalizeAttributes() {
 // Test normalizeAttributes with lower case attribute names
 Attributes attributesLowerCase = new Attributes();
 attributesLowerCase.put("class", "test");
 attributesLowerCase.put("id", "testId");

 Attributes normalizedAttributesLowerCase = new ParseSettings(htmlDefault).normalizeAttributes(attributesLowerCase);
 assertEquals("test", normalizedAttributesLowerCase.get("class"));
 assertEquals("testid", normalizedAttributesLowerCase.get("id"));

 // Test normalizeAttributes with preserve attribute case
 Attributes attributesPreserveCase = new Attributes();
 attributesPreserveCase.put("CLASS", "test");
 attributesPreserveCase.put("ID", "testId");

 Attributes normalizedAttributesPreserveCase = new ParseSettings(preserveCase).normalizeAttributes(attributesPreserveCase);
 assertEquals("TEST", normalizedAttributesPreserveCase.get("CLASS"));
 assertEquals("TESTID", normalizedAttributesPreserveCase.get("ID"));
 }

 /**
 * Test null arguments.
 */
 @Test(expected = NullPointerException.class)
 public void testNormalizeTagNullName() {
 ParseSettings parseSettings = new ParseSettings(true, true);
 parseSettings.normalizeTag(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNormalizeAttributeNullName() {
 ParseSettings parseSettings = new ParseSettings(true, true);
 parseSettings.normalizeAttribute(null);
 }

 @Test(expected = NullPointerException.class)
 public void testNormalizeAttributesNullAttributes() {
 ParseSettings parseSettings = new ParseSettings(true, true);
 parseSettings.normalizeAttributes(null);
 }

}