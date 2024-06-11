import org.junit.Test;
import org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private static final Entities ENTITIES = new Entities();

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 assertTrue("Named entity '" + name + "' should be known", Entities.isNamedEntity(name));
 }
 }

 @Test
 public void testIsNamedEntityWithInvalidName() {
 assertFalse("Invalid named entity name should not be known", Entities.isNamedEntity(""));
 }

 @Test
 public void testGetCharacterByName() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertEquals("Named entity '" + name + "' should map to character '" + character + "'", character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testEscape() {
 for (Character c : Characters.UnicodeBlock.Basic_Multilingual_Plane) {
 String string = Character.toString(c);
 EscapeMode escapeMode = Entities.EscapeMode.base;
 String expected = Entities.escape(string, null, escapeMode);
 String actual = Entities.escape(string, null, escapeMode);
 assertEquals("Escaped string for character '" + c + "' should be '" + expected + "'", expected, actual);
 }
 }

 @Test
 public void testEscapeWithInvalidCharacter() {
 String string = "👻";
 EscapeMode escapeMode = Entities.EscapeMode.base;
 String expected = "&#x" + Integer.toHexString(Character.codePointAt(string, 0)) + ";";
 String actual = Entities.escape(string, null, escapeMode);
 assertEquals("Escaped string for invalid character should be '" + expected + "'", expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testUnescapeWithInvalidString() {
 String string = "";
 String actual = Entities.unescape(string);
 fail("Unescaped string should not be returned for empty input");
 }

 @Test
 public void testUnescape() {
 String string = "&lt;&amp;&apos;&gt;&quot;";
 String expected = "<&'>\n";
 String actual = Entities.unescape(string);
 assertEquals("Unescaped string should be '" + expected + "'", expected, actual);
 }

 @Test
 public void testUnescapeWithStrict() {
 String string = "&lt;&amp;&apos;&gt;&quot;";
 boolean strict = true;
 String expected = "<&'>\n";
 String actual = Entities.unescape(string, strict);
 assertEquals("Unescaped string (strict) should be '" + expected + "'", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithInvalidCharacter() {
 String string = "&foo;";
 String actual = Entities.unescape(string);
 fail("Invalid named entity 'foo' should throw an exception");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithUnknownCharacterEscape() {
 String string = "&#xfoo;";
 String actual = Entities.unescape(string);
 fail("Unknown character escape 'foo' should throw an exception");
 }

 @Test
 public void testUnescapeWithUnicodeCharacterEscape() {
 String string = "&#x1F600;";
 String expected = "👻";
 String actual = Entities.unescape(string);
 assertEquals("Unescaped string for unicode character should be '" + expected + "'", expected, actual);
 }

}