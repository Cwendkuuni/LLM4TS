package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String tagName = "HTML";
 String attributeName = "CLASS";
 String expectedTagName = "html";
 String expectedAttributeName = "class";

 String tag = settings.normalizeTag(tagName);
 String attribute = settings.normalizeAttribute(attributeName);

 assertEquals(expectedTagName, tag);
 assertEquals(expectedAttributeName, attribute);
 }

 @Test
 public void testPreserveTagCase() {
 ParseSettings settings = ParseSettings.preserveCase;
 String tagName = "DIV";
 String attributeName = "ID";
 String expectedTagName = "DIV";
 String expectedAttributeName = "ID";

 String tag = settings.normalizeTag(tagName);
 String attribute = settings.normalizeAttribute(attributeName);

 assertEquals(expectedTagName, tag);
 assertEquals(expectedAttributeName, attribute);
 }

 @Test
 public void testLowerCaseTag() {
 ParseSettings settings = new ParseSettings(false, false);
 String tagName = "DIV";
 String expectedTagName = "div";

 String tag = settings.normalizeTag(tagName);

 assertEquals(expectedTagName, tag);
 }

 @Test
 public void testLowerCaseAttribute() {
 ParseSettings settings = new ParseSettings(false, false);
 String attributeName = "ID";
 String expectedAttributeName = "id";

 String attribute = settings.normalizeAttribute(attributeName);

 assertEquals(expectedAttributeName, attribute);
 }

 @Test
 public void testPreserveAttributeCase() {
 ParseSettings settings = new ParseSettings(false, true);
 String attributeName = "ID";
 String expectedAttributeName = "ID";

 String attribute = settings.normalizeAttribute(attributeName);

 assertEquals(expectedAttributeName, attribute);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("id", "test");
 attributes.put("class", "testClass");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

 String id = normalizedAttributes.get("id");
 String className = normalizedAttributes.get("class");

 assertEquals("test", id);
 assertEquals("testclass", className);
 }

 @Test
 public void testPreserveAttributesCase() {
 ParseSettings settings = new ParseSettings(false, true);
 Attributes attributes = new Attributes();
 attributes.put("id", "test");
 attributes.put("class", "testClass");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

 String id = normalizedAttributes.get("id");
 String className = normalizedAttributes.get("class");

 assertEquals("test", id);
 assertEquals("testClass", className);
 }
}