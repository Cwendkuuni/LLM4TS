package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Map<Character, String> fullEntities = Entities.fullByVal;
 for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();
 assertTrue("Named entity \"" + value + "\" not found in full entities map", Entities.isNamedEntity(value));
 }

 Character unknownEntity = 'ñ';
 assertFalse("Unknown entity \"" + unknownEntity + "\" is named entity", Entities.isNamedEntity(Character.toString(unknownEntity)));
 }

 @Test
 public void testIsBaseNamedEntity() {
 Map<String, Character> baseEntities = Entities.base;
 for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
 String key = entry.getKey();
 Character value = entry.getValue();
 assertTrue("Named entity \"" + key + "\" not found in base entities map", Entities.isBaseNamedEntity(key));
 }

 String unknownEntityName = "ñampe";
 assertFalse("Unknown entity name \"" + unknownEntityName + "\" is base named entity", Entities.isBaseNamedEntity(unknownEntityName));
 }

 @Test
 public void testGetCharacterByName() {
 Map<Character, String> fullEntities = Entities.fullByVal;
 for (Map.Entry<Character, String> entry : fullEntities.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();
 Character result = Entities.getCharacterByName(value);
 assertEquals("Named entity \"" + value + "\" does not map to correct character \"" + result + "\"", key, result);
 }

 Character unknownEntity = 'ñ';
 assertNull("Unknown entity \"" + unknownEntity + "\" should return null", Entities.getCharacterByName(Character.toString(unknownEntity)));
 }

 @Test
 public void testEscape() {
 StringBuilder accumulator = new StringBuilder();
 String input = "Test < & > \" & ' \n\r tab \u00A0 \u03B4 \uD800\uDC00 &amp; &lt; &gt; &quot; &#13; &#10; &nbsp; &amp;#x26; &amp;#x3C; &amp;#x3E; &amp;#xA0; &#x03B4; &#xD800&#xDC00;";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.setEscapeMode(Entities.EscapeMode.base);
 Entities.escape(accumulator, input, outputSettings, false, false, false);

 String expectedOutput = "Test &amp; &lt; &amp; &gt; &amp;quot; &amp; &apos; &amp;#13; &amp;#10; &nbsp; &amp;#x26; &amp;#x3C; &amp;#x3E; &amp;#xA0; &amp;#x03B4; &amp;#xD800&amp;#xDC00;";
 String output = accumulator.toString();
 assertEquals("Escaped String output does not match expected output", expectedOutput, output);
 }

 @Test
 public void testUnescape() {
 String input = "Test &amp; &lt; &amp; &gt; &amp;quot; &#13; &#10; &nbsp; &amp;#x26; &amp;#x3C; &amp;#x3E; &amp;#xA0; &amp;#x03B4; &amp;#xD800&amp;#xDC00;";
 String expectedOutput = "Test < & > \" \n\r \u00A0 \u03B4 \uD800\uDC00";
 String output = Entities.unescape(input, false);
 assertEquals("Unescaped String output does not match expected output", expectedOutput, output);
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingResourceException() {
 Entities.loadEntities("nonExistentFile.properties");
 }
}