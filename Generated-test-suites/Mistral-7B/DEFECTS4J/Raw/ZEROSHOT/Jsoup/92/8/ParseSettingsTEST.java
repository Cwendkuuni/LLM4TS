package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParseSettingsTest {

 @Test
 public void testParseSettingsHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 assertNotNull(htmlDefault);
 assertEquals(false, htmlDefault.preserveTagCase());
 assertEquals(false, htmlDefault.preserveAttributeCase());
 }

 @Test
 public void testParseSettingsPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 assertNotNull(preserveCase);
 assertEquals(true, preserveCase.preserveTagCase());
 assertEquals(true, preserveCase.preserveAttributeCase());
 }

 @Test
 public void testNormalizeTagLowercase() {
 ParseSettings settings = new ParseSettings(false, false);
 String tagName = "HTML";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals("html", normalizedTagName);
 }

 @Test
 public void testNormalizeTagPreserveCase() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = "HTML";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals("HTML", normalizedTagName);
 }

 @Test
 public void testNormalizeTagNull() {
 ParseSettings settings = new ParseSettings(false, false);
 String tagName = null;
 String normalizedTagName = settings.normalizeTag(tagName);
 assertNull(normalizedTagName);
 }

 @Test
 public void testNormalizeAttributeLowercase() {
 ParseSettings settings = new ParseSettings(false, false);
 String attributeName = "CLASS";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals("class", normalizedAttributeName);
 }

 @Test
 public void testNormalizeAttributePreserveCase() {
 ParseSettings settings = new ParseSettings(true, true);
 String attributeName = "CLASS";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals("CLASS", normalizedAttributeName);
 }

 @Test
 public void testNormalizeAttributeNull() {
 ParseSettings settings = new ParseSettings(false, false);
 String attributeName = null;
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertNull(normalizedAttributeName);
 }

 @Test
 public void testNormalizeAttributesLowercase() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.add("CLASS", "test");
 attributes.add("ID", "testId");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("class", normalizedAttributes.get("class"));
 assertEquals("test", normalizedAttributes.get("class"));
 assertEquals("id", normalizedAttributes.get("id"));
 assertEquals("testid", normalizedAttributes.get("id"));
 }

 @Test
 public void testNormalizeAttributesPreserveCase() {
 ParseSettings settings = new ParseSettings(true, true);
 Attributes attributes = new Attributes();
 attributes.add("CLASS", "test");
 attributes.add("ID", "testId");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("CLASS", normalizedAttributes.get("class"));
 assertEquals("test", normalizedAttributes.get("class"));
 assertEquals("ID", normalizedAttributes.get("id"));
 assertEquals("testId", normalizedAttributes.get("id"));
 }
}