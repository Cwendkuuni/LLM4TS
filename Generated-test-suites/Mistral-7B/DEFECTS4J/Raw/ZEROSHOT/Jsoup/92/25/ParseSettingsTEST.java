package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

 @Test
 public void testParseSettingsHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagNameLowerCased = htmlDefault.normalizeTag("tagName");
 String attributeNameLowerCased = htmlDefault.normalizeAttribute("attributeName");

 String expectedTagNameLowerCased = "tagname";
 String expectedAttributeNameLowerCased = "attributename";

 assertEquals(expectedTagNameLowerCased, tagNameLowerCased);
 assertEquals(expectedAttributeNameLowerCased, attributeNameLowerCased.getName());
 }

 @Test
 public void testParseSettingsPreserveCaseTag() {
 ParseSettings preserveCaseTag = new ParseSettings(true, false);
 String tagNamePreserved = preserveCaseTag.normalizeTag("TagName");

 String expectedTagNamePreserved = "TagName";

 assertEquals(expectedTagNamePreserved, tagNamePreserved);
 }

 @Test
 public void testParseSettingsPreserveCaseTagAndAttribute() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagNamePreserved = preserveCase.normalizeTag("TagName");
 String attributeNamePreserved = preserveCase.normalizeAttribute("AttributeName");
 Attributes attributes = new Attributes();
 attributes.put("AttributeName", "value");
 Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);

 String expectedTagNamePreserved = "TagName";
 String expectedAttributeNamePreserved = "AttributeName";
 String expectedAttributeValue = "value";

 assertEquals(expectedTagNamePreserved, tagNamePreserved);
 assertEquals(expectedAttributeNamePreserved, normalizedAttributes.get("AttributeName").getName());
 assertEquals(expectedAttributeValue, normalizedAttributes.get("AttributeName").getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseSettingsInvalidConstructor() {
 ParseSettings invalidSettings = new ParseSettings(true, "invalid");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseSettingsInvalidConstructorAttribute() {
 ParseSettings invalidSettings = new ParseSettings(true, new Attributes());
 }
}