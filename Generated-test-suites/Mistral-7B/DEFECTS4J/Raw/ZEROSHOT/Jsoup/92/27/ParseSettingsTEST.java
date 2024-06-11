package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagNameLowerCased = htmlDefault.normalizeTag("TAG");
 String attributeNameLowerCased = htmlDefault.normalizeAttribute("ATTRIBUTE");
 assertEquals("tag", tagNameLowerCased);
 assertEquals("attribute", attributeNameLowerCased);
 }

 @Test
 public void testPreserveTagCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagNamePreserved = preserveCase.normalizeTag("TAG");
 String attributeNameLowerCased = htmlDefault.normalizeAttribute("ATTRIBUTE");
 assertEquals("TAG", tagNamePreserved);
 assertEquals("attribute", attributeNameLowerCased);
 }

 @Test
 public void testPreserveAttributeCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagNameLowerCased = htmlDefault.normalizeTag("TAG");
 String attributeNamePreserved = preserveCase.normalizeAttribute("ATTRIBUTE");
 assertEquals("tag", tagNameLowerCased);
 assertEquals("ATTRIBUTE", attributeNamePreserved);
 }

 @Test
 public void testPreserveBothCase() {
 ParseSettings preserveBoth = new ParseSettings(true, true);
 String tagNamePreserved = preserveBoth.normalizeTag("TAG");
 String attributeNamePreserved = preserveBoth.normalizeAttribute("ATTRIBUTE");
 assertEquals("TAG", tagNamePreserved);
 assertEquals("ATTRIBUTE", attributeNamePreserved);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings preserveBoth = new ParseSettings(true, true);
 Attributes attributes = new Attributes();
 attributes.put("TAG", "VALUE");
 attributes.put("TAG-1", "VALUE-1");
 Attributes normalizedAttributes = preserveBoth.normalizeAttributes(attributes);
 assertEquals("TAG", normalizedAttributes.get("tag"));
 assertEquals("VALUE", normalizedAttributes.get("tag").getValue());
 assertEquals("TAG-1", normalizedAttributes.get("tag-1").getName());
 assertEquals("VALUE-1", normalizedAttributes.get("tag-1").getValue());
 }
}