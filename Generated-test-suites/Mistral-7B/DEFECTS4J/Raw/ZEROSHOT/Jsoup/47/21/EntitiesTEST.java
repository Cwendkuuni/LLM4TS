package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Entities class.
 */
public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities.full.entrySet().forEach(e -> {
 String key = e.getKey().toString();
 Character value = e.getValue();
 assertTrue("Named entity '" + key + "' is not recognized", Entities.isNamedEntity(key));
 });
 }

 @Test
 public void testIsBaseNamedEntity() {
 Entities.base.entrySet().forEach(e -> {
 String key = e.getKey().toString();
 Character value = e.getValue();
 assertTrue("Base named entity '" + key + "' is not recognized", Entities.isBaseNamedEntity(key));
 });

 Entities.xhtmlByVal.entrySet().forEach(e -> {
 Character key = e.getKey();
 String value = e.getValue();
 assertFalse("XHTML restricted named entity '" + key + "' is recognized as base named entity", Entities.isBaseNamedEntity(value));
 });

 Entities.fullByVal.entrySet().forEach(e -> {
 Character key = e.getKey();
 String value = e.getValue();
 assertFalse("Extended named entity '" + value + "' is recognized as base named entity", Entities.isBaseNamedEntity(value));
 });
 }

 @Test
 public void testGetCharacterByName() {
 Entities.full.entrySet().forEach(e -> {
 String key = e.getKey().toString();
 Character value = e.getValue();
 assertEquals("Named entity '" + key + "' does not return the correct character", value, Entities.getCharacterByName(key));
 });
 }

 @Test
 public void testEscape() {
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.encodeForHtml(true);

 String input = "&lt;test&gt;&amp;amp;'&quot;";
 String expected = "&amp;lt;test&amp;amp;&amp;amp;&#x27;";
 String result = Entities.escape(input, settings);
 assertEquals("Escaped string is not as expected", expected, result);

 input = "&apos;test&apos;&amp;lt;foo&amp;gt;bar&amp;#032;";
 expected = "&#x27;test&#x26;lt;foo&#x26;gt;bar&#x032;";
 result = Entities.escape(input, settings);
 assertEquals("Escaped string is not as expected", expected, result);

 input = "test&lt;foo&gt;bar&apos;";
 expected = "test&lt;foo&gt;bar&#x27;";
 result = Entities.escape(input, settings);
 assertEquals("Escaped string is not as expected", expected, result);
 }

 @Test
 public void testUnescape() {
 String input = "&amp;lt;test&amp;amp;&amp;amp;&#x27;";
 String expected = "&lt;test&amp;&amp;&apos;";
 String result = Entities.unescape(input);
 assertEquals("Unescaped string is not as expected", expected, result);

 input = "&#x27;test&#x26;lt;foo&#x26;gt;bar&#x032;";
 expected = "test<foo>bar ";
 result = Entities.unescape(input);
 assertEquals("Unescaped string is not as expected", expected, result);
 }

 @Test
 public void testCanEncode() {
 CoreCharset coreCharset = CoreCharset.utf;
 CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

 for (int i = 0; i < 256; i++) {
 char c = (char) i;
 assertTrue("Character '" + c + "' can be encoded", Entities.canEncode(coreCharset, c, encoder));
 }

 for (int i = 0x10000; i < 0x10ffff; i++) {
 char c = (char) i;
 assertFalse("Character '" + Character.toChars(c)[0] + "' cannot be encoded", Entities.canEncode(coreCharset, c, encoder));
 }
 }
}