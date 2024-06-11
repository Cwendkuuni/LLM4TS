package org.jsoup.parser.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

 private final String TAG_NAME = "HTML";
 private final String attributeName1 = "class";
 private final String attributeName2 = "ID";
 private final String attributeValue = "testValue";

 @Test
 public void testHtmlDefault() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String tagName = settings.normalizeTag(TAG_NAME);
 Assert.assertEquals("tag name should be lower-cased in HTML default settings", "html", tagName);
 }

 @Test
 public void testPreserveCase_PreserveTagCase() {
 ParseSettings settings = ParseSettings.preserveCase;
 String tagName = settings.normalizeTag(TAG_NAME);
 Assert.assertEquals("tag name should be preserved in preserveCase settings", "HTML", tagName);
 }

 @Test
 public void testPreserveCase_PreserveAttributeCase() {
 ParseSettings settings = ParseSettings.preserveCase;
 String attributeName1Normalized = settings.normalizeAttribute(attributeName1);
 String attributeName2Normalized = settings.normalizeAttribute(attributeName2);

 Assert.assertEquals("attribute name1 should be preserved", attributeName1, attributeName1Normalized);
 Assert.assertEquals("attribute name2 should be preserved", attributeName2, attributeName2Normalized);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInputForNormalizeTag() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String nullInput = null;
 settings.normalizeTag(nullInput);
 }

 @Test(expected = NullPointerException.class)
 public void testNullInputForNormalizeAttribute() {
 ParseSettings settings = ParseSettings.htmlDefault;
 String nullInput = null;
 settings.normalizeAttribute(nullInput);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings settings = ParseSettings.htmlDefault;
 Attributes attributes = new Attributes();
 attributes.put(attributeName1, attributeValue);
 attributes.put(attributeName2, attributeValue);

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 String tagName = normalizedAttributes.getTagName();
 String attributeName1Normalized = normalizedAttributes.get(attributeName1);
 String attributeName2Normalized = normalizedAttributes.get(attributeName2);

 Assert.assertEquals("tag name should be lower-cased in HTML default settings", "tag", tagName);
 Assert.assertEquals("attribute name1 should be lower-cased in HTML default settings", "class", attributeName1Normalized);
 Assert.assertEquals("attribute name2 should be lower-cased in HTML default settings", "id", attributeName2Normalized);
 }

 @Test
 public void testPreserveCase_NormalizeAttributes() {
 ParseSettings settings = ParseSettings.preserveCase;
 Attributes attributes = new Attributes();
 attributes.put(attributeName1, attributeValue);
 attributes.put(attributeName2, attributeValue);

 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 String tagName = normalizedAttributes.getTagName();
 String attributeName1Normalized = normalizedAttributes.get(attributeName1);
 String attributeName2Normalized = normalizedAttributes.get(attributeName2);

 Assert.assertEquals("tag name should be preserved", "HTML", tagName);
 Assert.assertEquals("attribute name1 should be preserved", "class", attributeName1Normalized);
 Assert.assertEquals("attribute name2 should be preserved", "ID", attributeName2Normalized);
 }
}