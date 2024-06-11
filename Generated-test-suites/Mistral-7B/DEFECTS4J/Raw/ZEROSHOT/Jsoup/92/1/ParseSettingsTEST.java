package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
 public void testNewParseSettings() {
 ParseSettings preserveTag = new ParseSettings(true, false);
 assertEquals(true, preserveTag.preserveTagCase());
 assertEquals(false, preserveTag.preserveAttributeCase());

 ParseSettings preserveBoth = new ParseSettings(true, true);
 assertEquals(true, preserveBoth.preserveTagCase());
 assertEquals(true, preserveBoth.preserveAttributeCase());
 }

 @Test
 public void testNormalizeTag() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = "TAG";
 String normalizedTagName = settings.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);

 settings = ParseSettings.htmlDefault;
 normalizedTagName = settings.normalizeTag(tagName);
 assertNotSame(tagName, normalizedTagName);
 assertEquals("tag", normalizedTagName);
 }

 @Test
 public void testNormalizeAttribute() {
 ParseSettings settings = new ParseSettings(false, true);
 String attributeName = "ATTRIBUTE";
 String normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertEquals(attributeName, normalizedAttributeName);

 settings = ParseSettings.htmlDefault;
 normalizedAttributeName = settings.normalizeAttribute(attributeName);
 assertNotSame(attributeName, normalizedAttributeName);
 assertEquals("attribute", normalizedAttributeName);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("TAG", "VALUE");
 attributes.put("ATTRIBUTE", "VALUE");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(2, normalizedAttributes.size());

 String tagValue = normalizedAttributes.get("tag");
 assertNotNull(tagValue);
 assertEquals("tag", tagValue);

 String attributeValue = normalizedAttributes.get("attribute");
 assertNotNull(attributeValue);
 assertEquals("attribute", attributeValue);
 }
}