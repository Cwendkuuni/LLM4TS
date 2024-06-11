package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

 private static Map<Character, String> getMap(EscapeMode mode) {
 return mode.getMap();
 }

 @Test
 public void testIsNamedEntity() {
 // Basic test
 assertTrue(Entities.isNamedEntity("lt"));
 // Edge case: empty string
 assertFalse(Entities.isNamedEntity(""));
 // Edge case: null
 assertFalse(Entities.isNamedEntity(null));
 // Exception handling: invalid input
 assertFalse(Entities.isNamedEntity("invalid"));
 }

 @Test
 public void testGetCharacterByName() {
 // Basic test
 assertEquals('<', Entities.getCharacterByName("lt"));
 // Edge case: null input
 assertNull(Entities.getCharacterByName(null));
 // Edge case: empty string
 assertNull(Entities.getCharacterByName(""));
 // Edge case: unknown entity
 assertNull(Entities.getCharacterByName("invalid"));
 }

 @Test
 public void testEscapeMethod() {
 // Basic test
 String input = "This is a test string";
 String expectedOutput = "This is a test string";
 String output = Entities.escape(input, Document.OutputSettings.xhtml());
 assertEquals(expectedOutput, output);

 // Edge case: empty string
 String emptyInput = "";
 String emptyExpectedOutput = "";
 String emptyOutput = Entities.escape(emptyInput, Document.OutputSettings.xhtml());
 assertEquals(emptyExpectedOutput, emptyOutput);

 // Edge case: null input
 String nullInput = null;
 String nullExpectedOutput = null;
 String nullOutput = Entities.escape(nullInput, Document.OutputSettings.xhtml());
 assertEquals(nullExpectedOutput, nullOutput);

 // Exception handling: invalid input
 String invalidInput = "This is a &test string";
 String invalidExpectedOutput = "This is a &amp;test string";
 String invalidOutput = Entities.escape(invalidInput, Document.OutputSettings.xhtml());
 assertEquals(invalidExpectedOutput, invalidOutput);
 }

 @Test
 public void testUnescapeMethod() {
 // Basic test
 String input = "&lt;This is a test&gt; string";
 String expectedOutput = "<This is a test>";
 String output = Entities.unescape(input);
 assertEquals(expectedOutput, output);

 // Edge case: empty string
 String emptyInput = "";
 String emptyOutput = Entities.unescape("");
 assertEquals(emptyOutput, "");

 // Edge case: null input
 String nullInput = null;
 String nullOutput = Entities.unescape(null);
 assertEquals(nullOutput, null);

 // Edge case: no entities in input
 String noEntitiesInput = "This is a test string";
 String noEntitiesOutput = "This is a test string";
 String noEntitiesExpectedOutput = "This is a test string";
 assertEquals(noEntitiesExpectedOutput, noEntitiesOutput);

 // Edge case: strict unescape
 String strictInput = "&lt;This is a test&amp; string";
 String strictOutput = "<This is a test>";
 String strictExpectedOutput = "<This is a test>";
 assertEquals(strictExpectedOutput, strictOutput);

 // Edge case: invalid input
 String invalidInput = "&invalid";
 String invalidOutput = Entities.unescape(invalidInput);
 assertNull(invalidOutput);
 }

 @Test
 public void testLoadEntitiesMethod() {
 // Basic test
 Properties expectedProperties = new Properties();
 expectedProperties.setProperty("quot", "0x00022");
 expectedProperties.setProperty("amp", "0x00026");
 expectedProperties.setProperty("apos", "0x00027");
 expectedProperties.setProperty("lt", "0x0003C");
 expectedProperties.setProperty("gt", "0x0003E");
 Map<String, Character> expectedEntities = new HashMap<String, Character>();
 for (Map.Entry<Object, Object> entry : expectedProperties.entrySet()) {
 Character character = Character.valueOf((char) Integer.parseInt((String) entry.getValue(), 16));
 String name = (String) entry.getKey();
 expectedEntities.put(name, character);
 }
 Map<String, Character> actualEntities = Entities.loadEntities("entities-base.properties");
 assertEquals(expectedEntities, actualEntities);

 // Edge case: null input
 assertNull(Entities.loadEntities(null));

 // Exception handling: invalid file name
 Map<String, Character> invalidEntities = Entities.loadEntities("invalidfilename.properties");
 assertTrue(invalidEntities.isEmpty());
 }

 @Test(expected = MissingResourceException.class)
 public void testMissingResourceException() {
 Entities.loadEntities("missingfilename.properties");
 }

 @Test
 public void testToCharacterKeyMethod() {
 // Basic test
 Map<String, Character> inputMap = new HashMap<String, Character>();
 inputMap.put("quot", '\"');
 inputMap.put("amp", '&');
 inputMap.put("apos", '\'');
 inputMap.put("lt", '<');
 inputMap.put("gt", '>');
 Map<Character, String> expectedOutputMap = new HashMap<Character, String>();
 expectedOutputMap.put('\"', "quot");
 expectedOutputMap.put('&', "amp");
 expectedOutputMap.put('\'\'', "apos");
 expectedOutputMap.put('<', "lt");
 expectedOutputMap.put('>', "gt");
 Map<Character, String> actualOutputMap = Entities.toCharacterKey(inputMap);
 assertEquals(expectedOutputMap, actualOutputMap);

 // Edge case: null input
 assertNull(Entities.toCharacterKey(null));
 }
}