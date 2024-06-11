package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

 // Test for isNamedEntity method
 @Test
 public void testIsNamedEntity() {
  Map<String, Character> fullMap = Entities.full;
  for (Map.Entry<String, Character> entry : fullMap.entrySet()) {
   String name = entry.getKey();
   Character entity = entry.getValue();
   assertTrue(Entities.isNamedEntity(name));
   assertEquals(entity, Entities.getCharacterByName(name));
  }
 }

 // Test for getCharacterByName method with valid entities
 @Test
 public void testGetCharacterByName() {
  CharSequence entities = "&lt; &gt; &amp; &apos; &quot;";
  for (int i = 0; i < entities.length(); i++) {
   Character entity = Entities.getCharacterByName(Character.toString(entities.charAt(i)));
   switch (entities.charAt(i)) {
    case '<':
     assertEquals(Entities.Entities.xhtml.getMap().get(Entities.Entities.xhtml.lt), entity);
     break;
    case '>':
     assertEquals(Entities.Entities.xhtml.getMap().get(Entities.Entities.xhtml.gt), entity);
     break;
    case '&':
     assertEquals(Entities.Entities.xhtml.getMap().get(Entities.Entities.xhtml.amp), entity);
     break;
    case '\'':
     assertEquals(Entities.Entities.xhtml.getMap().get(Entities.Entities.xhtml.apos), entity);
     break;
    case '\"':
     assertEquals(Entities.Entities.xhtml.getMap().get(Entities.Entities.xhtml.quot), entity);
     break;
    default:
     fail("Unexpected entity: " + entities.charAt(i));
   }
  }
 }

 // Test for escape method with default encoding and xhtml EscapeMode
 @Test
 public void testEscapeWithDefaultEncodingAndXHTMLMode() {
  String input = "&amp;lt; &amp;#123; &amp;#x1F600; &amp;quot; &amp;apos; &amp;gt; &amp;#x1F601;";
  String expectedOutput = "&amp;lt;&amp;#123;&amp;#x2600;&amp;quot;&amp;apos;&amp;gt;&amp;#x2601;";

  String output = Entities.escape(input, StandardCharsets.UTF_8, Entities.Entities.xhtml);
  assertEquals(expectedOutput, output);
 }

 // Test for escape method with custom encoding and base EscapeMode
 @Test
 public void testEscapeWithCustomEncodingAndBaseMode() {
  Charset customEncoding = Charset.forName("US-ASCII");
  String input = "&lt; &gt; &amp; &apos; &quot;";
  String expectedOutput = "&#60; &#62; &#38; &#39; &#34;";

  String output = Entities.escape(input, customEncoding, Entities.Entities.base);
  assertEquals(expectedOutput, output);
 }

 // Test for unescape method with strict mode
 @Test
 public void testUnescapeWithStrictMode() {
  String input = "&lt;lt;&gt; &amp;amp;&apos; &quot;&quot;&gt;";
  String expectedOutput = "<lt><lt>";

  String output = Entities.unescape(input, true);
  assertEquals(expectedOutput, output);
 }

 // Test for unescape method with non-strict mode
 @Test
 public void testUnescapeWithNonStrictMode() {
  String input = "&lt;lt;&gt; &amp;amp;&apos; &quot;&quot;&gt;";
  String expectedOutput = "&lt;lt&gt; &amp;amp; &apos;&apos; &quot;&quot;";

  String output = Entities.unescape(input, false);
  assertEquals(expectedOutput, output);
 }

 // Test for unescape method with invalid entities
 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithInvalidEntities() {
  String input = "&invalidEntity;";
  Entities.unescape(input, false);
 }

}