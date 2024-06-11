package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private static final Entities entities = new Entities();

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String key = entry.getKey();
 Character value = entry.getValue();
 assertTrue("Named entity: " + key + " should be present", Entities.isNamedEntity(key));
 }
 }

 @Test
 public void testIsBaseNamedEntity() {
 Map<Character, String> baseByVal = Entities.base.getMap();
 for (Map.Entry<Character, String> entry : baseByVal.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();
 assertTrue("Base named entity: " + value + " should be present in base entity set", Entities.isBaseNamedEntity(value));
 }
 }

 @Test
 public void testGetCharacterByName() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String key = entry.getKey();
 Character value = entry.getValue();
 assertEquals("Named entity: " + key + " should have corresponding character: " + value, value, Entities.getCharacterByName(key));
 }
 }

 @Test
 public void testEscape() {
 String testString = "&lt;xml version=&quot;1.0&quot; encoding=&apos;UTF-8&apos;?&gt;";
 Map<Character, String> xhtmlByVal = Entities.EscapeMode.xhtml.getMap();
 Document.OutputSettings out = new Document.OutputSettings();
 out.escapeMode(Entities.EscapeMode.xhtml);
 String escapedString = Entities.escape(testString, out);
 String expectedEscapedString = "&lt;!DOCTYPE xml PUBLIC &quot;-//W3C//DTD XML 1.0//EN&quot; &quot;http://www.w3.org/TR/xml/schemas.dtd&quot;&gt; &amp;lt;xml version=&quot;1.0&quot; encoding=&apos;UTF-8&apos;?&gt;";
 assertEquals("Escaped string should be: " + expectedEscapedString, expectedEscapedString, escapedString);
 }

 @Test
 public void testUnescape() {
 String testString = "&lt;!DOCTYPE xml PUBLIC &quot;-//W3C//DTD XML 1.0//EN&quot; &quot;http://www.w3.org/TR/xml/schemas.dtd&quot;&gt; &amp;lt;xml version=&apos;1.0&apos; encoding=&apos;UTF-8&apos;?&gt;";
 String unescapedString = Entities.unescape(testString);
 String expectedUnescapedString = "&lt;!DOCTYPE xml PUBLIC \"-//W3C//DTD XML 1.0//EN\" \"http://www.w3.org/TR/xml/schemas.dtd\"&gt; <xml version=\"1.0\" encoding=\"UTF-8\">";
 assertEquals("Unescaped string should be: " + expectedUnescapedString, expectedUnescapedString, unescapedString);
 }

 @Test
 public void testCanEncode() {
 Map<Character, String> utf = Entities.full;
 for (Map.Entry<Character, String> entry : utf.entrySet()) {
 Character key = entry.getKey();
 String name = entry.getValue();
 assertTrue("Character: " + key + " should be encodable", Entities.canEncode(Entities.CoreCharset.utf, key, null));
 }
 }
}