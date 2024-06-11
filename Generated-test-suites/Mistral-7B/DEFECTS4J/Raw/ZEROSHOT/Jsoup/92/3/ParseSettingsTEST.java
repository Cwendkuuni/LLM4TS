package org.jsoup.parser;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;

public class ParseSettingsTest {

 @Test
 public void testParseSettingsHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagNameLower = htmlDefault.normalizeTag("HTML");
 String tagNamePreserved = htmlDefault.normalizeTag("HTML");
 String attributeNameLower = htmlDefault.normalizeAttribute("CLASS");
 String attributeNamePreserved = htmlDefault.normalizeAttribute("CLASS");

 assertTrue("htmlDefault preserves tag case as false", !htmlDefault.preserveTagCase());
 assertTrue("htmlDefault preserves attribute case as false", !htmlDefault.preserveAttributeCase());
 assertEquals("htmlDefault lowercases tag names", "html", tagNameLower);
 assertEquals("htmlDefault preserves tag names", "html", tagNamePreserved);
 assertEquals("htmlDefault lowercases attribute names", "class", attributeNameLower);
 assertEquals("htmlDefault preserves attribute names", "class", attributeNamePreserved);
 }

 @Test
 public void testParseSettingsPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagNameLower = preserveCase.normalizeTag("DIV");
 String tagNamePreserved = preserveCase.normalizeTag("DIV");
 String attributeNameLower = preserveCase.normalizeAttribute("ID");
 String attributeNamePreserved = preserveCase.normalizeAttribute("ID");

 assertTrue("preserveCase preserves tag case as true", preserveCase.preserveTagCase());
 assertTrue("preserveCase preserves attribute case as true", preserveCase.preserveAttributeCase());
 assertEquals("preserveCase lowercases tag names", "div", tagNameLower);
 assertEquals("preserveCase preserves tag names", "DIV", tagNamePreserved);
 assertEquals("preserveCase lowercases attribute names", "id", attributeNameLower);
 assertEquals("preserveCase preserves attribute names", "ID", attributeNamePreserved);
 }

 @Test
 public void testParseSettingsNormalizeTag() {
 ParseSettings settings = new ParseSettings(true, false);
 String tagNameLower = settings.normalizeTag("P");
 String tagNamePreserved = settings.normalizeTag("P");

 assertEquals("normalizeTag lowercases tag names when preserveTagCase is false", "p", tagNameLower);
 assertEquals("normalizeTag preserves tag names when preserveTagCase is true", "P", tagNamePreserved);
 }

 @Test
 public void testParseSettingsNormalizeAttribute() {
 ParseSettings settings = new ParseSettings(false, true);
 String attributeNameLower = settings.normalizeAttribute("COLOR");
 String attributeNamePreserved = settings.normalizeAttribute("COLOR");

 assertEquals("normalizeAttribute lowercases attribute names when preserveAttributeCase is false", "color", attributeNameLower);
 assertEquals("normalizeAttribute preserves attribute names when preserveAttributeCase is true", "COLOR", attributeNamePreserved);
 }

 @Test
 public void testParseSettingsNormalizeAttributes() {
 ParseSettings settings = new ParseSettings(false, false);
 Attributes attributes = new Attributes();
 attributes.put("ID", "valueID");
 attributes.put("CLASS", "valueCLASS");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);

 assertEquals("normalizeAttributes lowercases attribute names when preserveAttributeCase is false", "id", normalizedAttributes.get("id"));
 assertEquals("normalizeAttributes preserves attribute names when preserveAttributeCase is true", "CLASS", normalizedAttributes.get("CLASS"));
 }
}