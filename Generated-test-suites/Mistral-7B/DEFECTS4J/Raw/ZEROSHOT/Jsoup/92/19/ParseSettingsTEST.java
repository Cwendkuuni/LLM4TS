package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.parser.ParseSettings.*;
import static org.jsoup.internal.Normalizer.lowerCase;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "HTML";
 String expectedLowerCaseTagName = "html";
 String attributeName = "id";
 String expectedLowerCaseAttributeName = "id";

 String tagNameAfterNormalize = htmlDefault.normalizeTag(tagName);
 String attributeNameAfterNormalize = htmlDefault.normalizeAttribute(attributeName);

 assertEquals(expectedLowerCaseTagName, tagNameAfterNormalize);
 assertEquals(expectedLowerCaseAttributeName, attributeNameAfterNormalize);
 }

 @Test
 public void testPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagName = "DIV";
 String expectedTagName = "DIV";
 String attributeName = "Class";
 String expectedAttributeName = "Class";

 String tagNameAfterNormalize = preserveCase.normalizeTag(tagName);
 String attributeNameAfterNormalize = preserveCase.normalizeAttribute(attributeName);

 assertEquals(expectedTagName, tagNameAfterNormalize);
 assertEquals(expectedAttributeName, attributeNameAfterNormalize);
 }

 @Test
 public void testNormalizeTag_lowerCase_input() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "html";
 String expectedTagName = "html";

 String tagNameAfterNormalize = htmlDefault.normalizeTag(tagName);

 assertEquals(expectedTagName, tagNameAfterNormalize);
 }

 @Test
 public void testNormalizeTag_upperCase_input() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "HTML";
 String expectedTagName = "html";

 String tagNameAfterNormalize = htmlDefault.normalizeTag(tagName);

 assertEquals(expectedTagName, tagNameAfterNormalize);
 }

 @Test
 public void testNormalizeTag_mixedCase_input() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "HiTML";
 String expectedTagName = "html";

 String tagNameAfterNormalize = htmlDefault.normalizeTag(tagName);

 assertEquals(expectedTagName, tagNameAfterNormalize);
 }

 @Test
 public void testNormalizeAttribute_lowerCase_input() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String attributeName = "id";
 String expectedAttributeName = "id";

 String attributeNameAfterNormalize = htmlDefault.normalizeAttribute(attributeName);

 assertEquals(expectedAttributeName, attributeNameAfterNormalize);
 }

 @Test
 public void testNormalizeAttribute_upperCase_input() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String attributeName = "ID";
 String expectedAttributeName = "id";

 String attributeNameAfterNormalize = htmlDefault.normalizeAttribute(attributeName);

 assertEquals(expectedAttributeName, attributeNameAfterNormalize);
 }

 @Test
 public void testNormalizeAttribute_mixedCase_input() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String attributeName = "Id";
 String expectedAttributeName = "id";

 String attributeNameAfterNormalize = htmlDefault.normalizeAttribute(attributeName);

 assertEquals(expectedAttributeName, attributeNameAfterNormalize);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 Attributes attributes = new Attributes();
 attributes.put("ID", "value1");
 attributes.put("class", "value2");
 attributes.put("Class", "value3");

 Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);
 String idAttributeName = normalizedAttributes.get("id");
 String classNameAttributeName = normalizedAttributes.get("class");

 assertEquals("id", idAttributeName);
 assertEquals("class", classNameAttributeName);
 }

 @Test
 public void testNormalizeAttributes_preserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes attributes = new Attributes();
 attributes.put("ID", "value1");
 attributes.put("class", "value2");
 attributes.put("Class", "value3");

 Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
 String idAttributeName = normalizedAttributes.get("ID");
 String classNameAttributeName = normalizedAttributes.get("class");

 assertEquals("ID", idAttributeName);
 assertEquals("class", classNameAttributeName);
 }

}