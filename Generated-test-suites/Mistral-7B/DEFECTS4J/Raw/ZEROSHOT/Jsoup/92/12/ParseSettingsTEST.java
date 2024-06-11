package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 assertTrue(htmlDefault.preserveTagCase() == false);
 assertTrue(htmlDefault.preserveAttributeCase() == false);
 }

 @Test
 public void testPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 assertTrue(preserveCase.preserveTagCase() == true);
 assertTrue(preserveCase.preserveAttributeCase() == true);
 }

 @Test
 public void testNormalizeTag() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = "TAG";
 String normalizedTag = settings.normalizeTag(tagName);
 assertEquals(tagName, normalizedTag);

 settings = new ParseSettings(false, false);
 normalizedTag = settings.normalizeTag(tagName);
 assertNotEquals(tagName, normalizedTag);
 assertNotNull(normalizedTag);
 }

 @Test
 public void testNormalizeTag_lowerCase() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = "TAG";
 String normalizedTag = settings.normalizeTag(tagName);
 assertNotEquals(tagName, normalizedTag);
 assertEquals("tag", normalizedTag);

 settings = new ParseSettings(false, false);
 normalizedTag = settings.normalizeTag(tagName);
 assertNotEquals(tagName, normalizedTag);
 assertEquals("tag", normalizedTag);
 }

 @Test
 public void testNormalizeTag_empty() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = "";
 String normalizedTag = settings.normalizeTag(tagName);
 assertNull(normalizedTag);
 }

 @Test
 public void testNormalizeTag_null() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagName = null;
 assertNull(settings.normalizeTag(tagName));
 }

 @Test
 public void testNormalizeAttribute() {
 ParseSettings settings = new ParseSettings(true, true);
 String attributeName = "ATTRIBUTE";
 String normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertEquals(attributeName, normalizedAttribute);

 settings = new ParseSettings(false, true);
 normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertNotEquals(attributeName, normalizedAttribute);
 assertNotNull(normalizedAttribute);
 }

 @Test
 public void testNormalizeAttribute_lowerCase() {
 ParseSettings settings = new ParseSettings(true, true);
 String attributeName = "ATTRIBUTE";
 String normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertNotEquals(attributeName, normalizedAttribute);
 assertEquals("attribute", normalizedAttribute);

 settings = new ParseSettings(false, true);
 normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertNotEquals(attributeName, normalizedAttribute);
 assertEquals("attribute", normalizedAttribute);
 }

 @Test
 public void testNormalizeAttribute_empty() {
 ParseSettings settings = new ParseSettings(true, true);
 String attributeName = "";
 String normalizedAttribute = settings.normalizeAttribute(attributeName);
 assertNull(normalizedAttribute);
 }

 @Test
 public void testNormalizeAttribute_null() {
 ParseSettings settings = new ParseSettings(true, true);
 String attributeName = null;
 assertNull(settings.normalizeAttribute(attributeName));
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("TAG", "VALUE");
 attributes.put("TAG2", "VALUE2");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(2, normalizedAttributes.size());

 String tag1 = normalizedAttributes.get("tag");
 assertNull(tag1);

 String tag1Value = normalizedAttributes.get("TAG");
 assertNotNull(tag1Value);
 assertNotNull(tag1Value.trim());
 assertEquals("tag", tag1Value.trim().toUpperCase());

 String tag2 = normalizedAttributes.get("TAG2");
 assertNotNull(tag2);
 assertNotNull(tag2.trim());
 assertEquals("tag2", tag2.trim().toUpperCase());
 }

 @Test
 public void testNormalizeAttributes_preserveTagCase() {
 ParseSettings settings = new ParseSettings(true, false);
 Attributes attributes = new Attributes();
 attributes.put("TAG", "VALUE");
 attributes.put("TAG2", "VALUE2");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(2, normalizedAttributes.size());

 String tag1 = normalizedAttributes.get("tag");
 assertNull(tag1);

 String tag1Value = normalizedAttributes.get("TAG");
 assertNotNull(tag1Value);
 assertNotNull(tag1Value.trim());
 assertEquals("TAG", tag1Value.trim());

 String tag2 = normalizedAttributes.get("TAG2");
 assertNotNull(tag2);
 assertNotNull(tag2.trim());
 assertEquals("TAG2", tag2.trim());
 }

 @Test
 public void testNormalizeAttributes_preserveAttributeCase() {
 ParseSettings settings = new ParseSettings(false, true);
 Attributes attributes = new Attributes();
 attributes.put("TAG", "VALUE");
 attributes.put("TAG2", "VALUE2");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(2, normalizedAttributes.size());

 String tag1 = normalizedAttributes.get("tag");
 assertNull(tag1);

 String tag1Value = normalizedAttributes.get("TAG");
 assertNotNull(tag1Value);
 assertNotNull(tag1Value.trim());
 assertEquals("tag", tag1Value.trim().toLowerCase());

 String tag2 = normalizedAttributes.get("TAG2");
 assertNotNull(tag2);
 assertNotNull(tag2.trim());
 assertEquals("TAG2", tag2.trim());
 }

 @Test
 public void testNormalizeAttributes_preserveTagAndAttributeCase() {
 ParseSettings settings = new ParseSettings(true, true);
 Attributes attributes = new Attributes();
 attributes.put("TAG", "VALUE");
 attributes.put("TAG2", "VALUE2");

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(2, normalizedAttributes.size());

 String tag1 = normalizedAttributes.get("tag");
 assertNull(tag1);

 String tag1Value = normalizedAttributes.get("TAG");
 assertNotNull(tag1Value);
 assertNotNull(tag1Value.trim());
 assertEquals("TAG", tag1Value.trim());

 String tag2 = normalizedAttributes.get("TAG2");
 assertNotNull(tag2);
 assertNotNull(tag2.trim());
 assertEquals("TAG2", tag2.trim());
 }
}