package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

 private static final Map<Character, String> expectedXhtmlMap = new HashMap<Character, String>() {{
 put((char) 0x00022, "quot");
 put((char) 0x00026, "amp");
 put((char) 0x00027, "apos");
 put((char) 0x0003C, "lt");
 put((char) 0x0003E, "gt");
 }};

 private static final Map<String, Character> expectedFullMap = new HashMap<String, Character>() {{
 put("quot", (char) 0x00022);
 put("amp", (char) 0x00026);
 put("apos", (char) 0x00027);
 put("lt", (char) 0x0003C);
 put("gt", (char) 0x0003E);
 put("nbsp", (char) 0x000A0);
 put("iexcl", (char) 0x0000A1);
 put("cent", (char) 0x0000A2);
 put("pound", (char) 0x0000A3);
 put("curren", (char) 0x0000A4);
 put("yen", (char) 0x0000A5);
 // Add more entities as needed
 }};

 private static final Map<Character, String> expectedBaseMap = new HashMap<Character, String>() {{
 put((char) 0x00022, "quot");
 put((char) 0x00026, "amp");
 put((char) 0x00027, "apos");
 put((char) 0x0003C, "lt");
 put((char) 0x0003E, "gt");
 }};

 @Test
 public void testIsNamedEntity() {
 // Test known named entities
 assertTrue(Entities.isNamedEntity("quot"));
 assertTrue(Entities.isNamedEntity("amp"));
 assertTrue(Entities.isNamedEntity("apos"));
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("gt"));

 // Test unknown named entities
 assertFalse(Entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test known named entities
 assertEquals((char) 0x00022, Entities.getCharacterByName("quot"));
 assertEquals((char) 0x00026, Entities.getCharacterByName("amp"));
 assertEquals((char) 0x00027, Entities.getCharacterByName("apos"));
 assertEquals((char) 0x0003C, Entities.getCharacterByName("lt"));
 assertEquals((char) 0x0003E, Entities.getCharacterByName("gt"));

 // Test unknown named entities
 assertNull(Entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 String input = "Hello & World! <greeting status='success'>";
 String expectedXhtmlOutput = "Hello &amp; World! &lt;greeting status='success'&gt;";
 String expectedBaseOutput = "Hello & World! &amp;lt;greeting status='success'&gt;";

 // Test XHTML entities
 assertEquals(Entities.escape(input, Entities.EscapeMode.xhtml, null), expectedXhtmlOutput);

 // Test base entities
 assertEquals(Entities.escape(input, null, Entities.EscapeMode.base), expectedBaseOutput);
 }

 @Test
 public void testUnescape() {
 String input = "Hello &amp; World! &lt;greeting status='success'&gt;";
 String expectedXhtmlOutput = "Hello & World! <greeting status='success'>";
 String expectedBaseOutput = "Hello & World! &lt;greeting status='success'&gt;";

 // Test XHTML entities
 assertEquals(Entities.unescape(input, false), expectedXhtmlOutput);

 // Test base entities
 assertEquals(Entities.unescape(input, true), expectedBaseOutput);
 }

 @Test
 public void testXhtmlMap() {
 Map<Character, String> actualXhtmlMap = Entities.xhtmlByVal.getMap();
 assertEquals(expectedXhtmlMap, actualXhtmlMap);
 }

 @Test
 public void testFullMap() {
 Map<Character, String> actualFullMap = Entities.fullByVal.getMap();
 assertEquals(expectedFullMap, actualFullMap);
 }

 @Test
 public void testBaseMap() {
 Map<Character, String> actualBaseMap = Entities.baseByVal.getMap();
 assertEquals(expectedBaseMap, actualBaseMap);
 }
}