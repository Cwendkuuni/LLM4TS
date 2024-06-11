package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

 @Test
 public void testParseSettingsHtmlDefault() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String tagName = "HTML";
 String expectedTagName = "html";
 String attributeName = "CLASS";
 String expectedAttributeName = "class";

 String tag = settings.normalizeTag(tagName);
 String attribute = settings.normalizeAttribute(attributeName);

 assertEquals(expectedTagName, tag);
 assertEquals(expectedAttributeName, attribute);
 }

 @Test
 public void testParseSettingsPreserveCase() {
 ParseSettings settings = ParseSettings.preserveCase;
 String tagName = "DIV";
 String expectedTagName = "DIV";
 String attributeName = "id";
 String expectedAttributeName = "ID";

 String tag = settings.normalizeTag(tagName);
 String attribute = settings.normalizeAttribute(attributeName);

 assertEquals(expectedTagName, tag);
 assertEquals(expectedAttributeName, attribute);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings settings = ParseSettings.htmlDefault;
 Attributes attributes = new Attributes();
 attributes.put("id", "testID");
 attributes.put("class", "testClass");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 String id = normalizedAttributes.get("id");
 String className = normalizedAttributes.get("class");

 assertEquals("testID", id);
 assertEquals("testClass", className);
 }

 @Test
 public void testNormalizeAttributesPreserveCase() {
 ParseSettings settings = ParseSettings.preserveCase;
 Attributes attributes = new Attributes();
 attributes.put("id", "TEST_ID");
 attributes.put("class", "TEST_CLASS");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 String id = normalizedAttributes.get("id");
 String className = normalizedAttributes.get("class");

 assertEquals("TEST_ID", id);
 assertEquals("TEST_CLASS", className);
 }
}