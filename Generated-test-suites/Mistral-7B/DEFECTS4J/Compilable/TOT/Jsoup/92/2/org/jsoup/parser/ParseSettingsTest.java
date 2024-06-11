package org.jsoup.parser;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultNormalizeTag() {
 String input = "<html><tag></tag></html>";
 ParseSettings settings = ParseSettings.htmlDefault;
 String expected = "tag";
 String actual = settings.normalizeTag("Tag");
 assertEquals(expected, actual);
 }

 @Test
 public void testHtmlDefaultNormalizeAttribute() {
 String input = "<html <Attribute='value'></html>";
 ParseSettings settings = ParseSettings.htmlDefault;
 String expected = "attribute";
 String actual = settings.normalizeAttribute("Attribute");
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveCaseNormalizeTag() {
 String input = "<HTML><TAG></TAG></HTML>";
 ParseSettings settings = ParseSettings.preserveCase;
 String expected = "TAG";
 String actual = settings.normalizeTag("TAG");
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveCaseNormalizeAttribute() {
 String input = "<html <ATTRIBUTE='value'></html>";
 ParseSettings settings = ParseSettings.preserveCase;
 String expected = "ATTRIBUTE";
 String actual = settings.normalizeAttribute("ATTRIBUTE");
 assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("Attribute1", "value1");
 attributes.put("ATTRIBUTE2", "value2");
 ParseSettings settings = ParseSettings.preserveCase;
 attributes = settings.normalizeAttributes(attributes);
 String expected1 = "Attribute1";
 String expected2 = "ATTRIBUTE2";
 assertEquals(expected1, attributes.get("Attribute1"));
 assertEquals(expected2, attributes.get("ATTRIBUTE2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalizeAttributesNullInput() {
 ParseSettings settings = ParseSettings.preserveCase;
 Attributes attributes = null;
 settings.normalizeAttributes(attributes);
 }
}