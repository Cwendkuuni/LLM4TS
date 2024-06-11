import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

 private static final Map<Character, Character> expectedXhtmlMap = new HashMap<>();
 static {
 expectedXhtmlMap.put((char) 0x00022, "quot");
 expectedXhtmlMap.put((char) 0x00026, "amp");
 expectedXhtmlMap.put((char) 0x00027, "apos");
 expectedXhtmlMap.put((char) 0x0003C, "lt");
 expectedXhtmlMap.put((char) 0x0003E, "gt");
 }

 @Test
 public void testIsNamedEntity() {
 for (Character entity : full.keySet()) {
 assertTrue("Entity " + entity + " should be named entity", isNamedEntity(String.valueOf(entity)));
 }
 for (Character entity : new HashSet<>(base.keySet())) {
 assertTrue("Entity " + entity + " should be named entity in base set", isNamedEntity(String.valueOf(entity)));
 }
 for (Character entity : Character.UnicodeBlock.BASIC_LATIN.getCodes()) {
 if (!full.containsKey(String.valueOf(entity))) {
 assertFalse("Entity " + entity + " should not be named entity", isNamedEntity(String.valueOf(entity)));
 }
 }
 }

 @Test
 public void testGetCharacterByName() {
 for (Character entity : full.keySet()) {
 Character expected = getCharacterByName(String.valueOf(entity));
 assertEquals("Named entity " + entity + " should map to character " + expected, expected, getCharacterByName(String.valueOf(entity)));
 }
 }

 @Test
 public void testEscape() {
 String input = "Test < & > \" '";
 for (EscapeMode escapeMode : EscapeMode.values()) {
 String expected = "";
 switch (escapeMode) {
 case xhtml:
 for (Character entity : expectedXhtmlMap.keySet()) {
 expected += expectedXhtmlMap.get(entity) + escapeMode.name();
 }
 expected += input.replaceAll("[^\\x00-\\x7F]", "???");
 break;
 case base:
 expected += escape(input, baseByVal, escapeMode);
 break;
 case extended:
 expected += escape(input, fullByVal, escapeMode);
 break;
 }
 String output = escape(input, null, escapeMode);
 assertEquals("Escape mode " + escapeMode + " should produce output '" + expected + "'", expected, output);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeWithNullInput() {
 escape(null, null, EscapeMode.base);
 }

 @Test
 public void testEscapeWithEmptyInput() {
 String output = escape("", null, EscapeMode.base);
 assertEquals("Empty string should produce empty output", "", output);
 }

 @Test
 public void testUnescape() {
 String input = "&quot;Test &amp; &lt; &gt; &apos;";
 String expected = "\"Test & < > '";
 String output = unescape(input);
 assertEquals("Unescape should produce output '" + expected + "'", expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullInput() {
 unescape(null);
 }

 @Test
 public void testUnescapeWithEmptyInput() {
 String output = unescape("");
 assertEquals("Empty string should produce empty output", "", output);
 }

 @Test
 public void testUnescapeStrict() {
 String input = "&quot;Test &amp; &lt; &gt; &apos;";
 String output = unescape(input, true);
 assertEquals("Unescape strict should produce output '" + input + "'", input, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeStrictWithNullInput() {
 unescape(null, true);
 }

 @Test
 public void testUnescapeStrictWithEmptyInput() {
 String output = unescape("", true);
 assertEquals("Empty string should produce empty output", "", output);
 }

 @Test
 public void testIsNamedEntityWithInvalidName() {
 assertFalse("Invalid entity name should not be named entity", isNamedEntity(""));
 assertFalse("Invalid entity name should not be named entity", isNamedEntity(" "));
 assertFalse("Invalid entity name should not be named entity", isNamedEntity("abc"));
 }

 @Test
 public void testGetCharacterByNameWithInvalidName() {
 try {
 getCharacterByName("abc");
 fail("Invalid entity name should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

}