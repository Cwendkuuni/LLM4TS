package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EntitiesTest {

 // Test isNamedEntity method
 @Test
 public void testIsNamedEntity() {
  Map<String, Character> full = Entities.full;
  for (Map.Entry<String, Character> entry : full.entrySet()) {
   String key = entry.getKey();
   Character value = entry.getValue();
   assertTrue("Named entity '" + key + "' not found in full map", Entities.isNamedEntity(key));
   assertEquals("Named entity '" + key + "' has incorrect character value", value, Entities.getCharacterByName(key));
  }
 }

 // Test isBaseNamedEntity method
 @Test
 public void testIsBaseNamedEntity() {
  Map<String, Character> base = Entities.base;
  for (Map.Entry<String, Character> entry : base.entrySet()) {
   String key = entry.getKey();
   Character value = entry.getValue();
   assertTrue("Named entity '" + key + "' not found in base map", Entities.isBaseNamedEntity(key));
   assertEquals("Named entity '" + key + "' has incorrect character value", value, Entities.getCharacterByName(key));
  }
 }

 // Test getCharacterByName method
 @Test
 public void testGetCharacterByName() {
  assertEquals("lt", Character.toString(Entities.getCharacterByName("lt")));
  assertEquals("quot", Character.toString(Entities.getCharacterByName("quot")));
  assertEquals("amp", Character.toString(Entities.getCharacterByName("amp")));
  assertEquals("gt", Character.toString(Entities.getCharacterByName("gt")));
 }

 // Test escape method with default EscapeMode
 @Test
 public void testEscapeWithDefaultEscapeMode() {
  String input = "& <> \"";
  String expectedOutput = "&amp; &lt; &gt; &quot;";
  assertEquals("Input: " + input + ", Output: " + Entities.escape(input, null), expectedOutput, Entities.escape(input, null));
 }

 // Test escape method with xhtml EscapeMode
 @Test
 public void testEscapeWithXhtmlEscapeMode() {
  String input = "& <> \"";
  String expectedOutput = "&amp; &lt; &gt;";
  assertEquals("Input: " + input + ", Output: " + Entities.escape(input, new Document.OutputSettings(Entities.EscapeMode.xhtml)), expectedOutput, Entities.escape(input, new Document.OutputSettings(Entities.EscapeMode.xhtml)));
 }

 // Test escape method with extended EscapeMode
 @Test
 public void testEscapeWithExtendedEscapeMode() {
  String input = "&#x1F600; & <> \"";
  String expectedOutput = "&#x1F600; &amp; &lt; &gt; &quot;";
  assertEquals("Input: " + input + ", Output: " + Entities.escape(input, new Document.OutputSettings(Entities.EscapeMode.extended)), expectedOutput, Entities.escape(input, new Document.OutputSettings(Entities.EscapeMode.extended)));
 }

 // Test unescape method with default strictness level
 @Test
 public void testUnescapeWithDefaultStrictness() {
  String input = "&amp; &lt; &gt; &quot;";
  String expectedOutput = "& <> \"";
  assertEquals("Input: " + input + ", Output: " + Entities.unescape(input), expectedOutput, Entities.unescape(input));
 }

 // Test unescape method with strict strictness level
 @Test
 public void testUnescapeWithStrictStrictness() {
  String input = "&amp; &lt; &gt; &quot;&#x0;";
  String expectedOutput = "& <> \"";
  assertEquals("Input: " + input + ", Output: " + Entities.unescape(input, true), expectedOutput, Entities.unescape(input, true));
 }
}