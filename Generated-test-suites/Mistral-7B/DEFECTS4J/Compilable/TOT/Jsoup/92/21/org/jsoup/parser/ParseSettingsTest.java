package org.jsoup.parser;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "DIV";
 String attributeName1 = "class";
 String attributeName2 = "data-test";

 String normalizedTag = htmlDefault.normalizeTag(tagName);
 String normalizedAttribute1 = htmlDefault.normalizeAttribute(attributeName1);
 String normalizedAttribute2 = htmlDefault.normalizeAttribute(attributeName2);

 assertEquals(tagName, normalizedTag); // checks if tag name is not changed
 assertEquals(attributeName1, normalizedAttribute1); // checks if attribute name is lowercased
 assertEquals(attributeName2, normalizedAttribute2); // checks if attribute name with hyphen is lowercased

 Attributes attributes = new Attributes();
 attributes.put(attributeName1, "value1");
 attributes.put(attributeName2, "value2");

 Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());
 assertEquals(attributeName1, normalizedAttributes.get(attributeName1));
 assertEquals("value1", normalizedAttributes.get(attributeName1));
 assertEquals(attributeName2, normalizedAttributes.get(attributeName2));
 assertEquals("value2", normalizedAttributes.get(attributeName2));
 }

 @Test
 public void testPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagName = "DIV";
 String attributeName1 = "CLASS";
 String attributeName2 = "DATA-TEST";

 String normalizedTag = preserveCase.normalizeTag(tagName);
 String normalizedAttribute1 = preserveCase.normalizeAttribute(attributeName1);
 String normalizedAttribute2 = preserveCase.normalizeAttribute(attributeName2);

 assertEquals(tagName, normalizedTag); // checks if tag name is not changed
 assertEquals(attributeName1, normalizedAttribute1); // checks if attribute name is preserved
 assertEquals(attributeName2, normalizedAttribute2); // checks if attribute name with hyphen is preserved

 Attributes attributes = new Attributes();
 attributes.put(attributeName1, "value1");
 attributes.put(attributeName2, "value2");

 Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());
 assertEquals(attributeName1, normalizedAttributes.get(attributeName1));
 assertEquals("value1", normalizedAttributes.get(attributeName1));
 assertEquals(attributeName2, normalizedAttributes.get(attributeName2));
 assertEquals("value2", normalizedAttributes.get(attributeName2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTagName() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = null;

 String normalizedTag = htmlDefault.normalizeTag(tagName);
 fail("Expected IllegalArgumentException but was: " + normalizedTag);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullAttributeName() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String attributeName = null;

 String normalizedAttribute = htmlDefault.normalizeAttribute(attributeName);
 fail("Expected IllegalArgumentException but was: " + normalizedAttribute);
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributes() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 Attributes attributes = null;

 Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);
 fail("Expected NullPointerException but was: " + normalizedAttributes);
 }
}