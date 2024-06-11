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

 private static final Map<String, Character> full = new HashMap<String, Character>() {{
 put("lt", '<');
 put("gt", '>');
 put("amp", '&');
 put("apos", '\'');
 put("quot", '"');
 // Add other entities here if needed
 }};

 private static final Map<Character, String> xhtmlByVal = new HashMap<Character, String>() {{
 put('<', "lt");
 put('>', "gt");
 put('&', "amp");
 put('\'', "apos");
 put('"', "quot");
 // Add other entities here if needed
 }};

 private static final Map<Character, String> baseByVal = new HashMap<Character, String>() {{
 put('<', "lt");
 put('>', "gt");
 put('&', "amp");
 put('\'', "apos");
 put('"', "quot");
 // Add other entities here if needed
 }};

 private static final Map<Character, String> fullByVal = new HashMap<Character, String>() {{
 // Add entities and their values here
 }};

 @Test
 public void testIsNamedEntity() {
 // Test for known named entities
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("amp"));
 // Test for unknown entities
 assertFalse(Entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test for known named entities
 assertEquals(Entities.getCharacterByName("lt"), '<');
 assertEquals(Entities.getCharacterByName("amp"), '&');
 // Test for unknown entities
 assertNull(Entities.getCharacterByName("unknown"));
 }

 @Test
 public void testMapComparisons() {
 // Test comparisons between maps
 assertEquals(Entities.baseByVal, fullByVal); // assumes maps are equal
 assertEquals(Entities.xhtmlByVal, new HashMap<Character, String>(Entities.baseByVal) { { putAll(fullByVal); } });
 }

 @Test
 public void testEscape() {
 String string = "this is a test < & > ' \" string";
 String escaped = Entities.escape(string, null, Entities.EscapeMode.base);
 assertEquals("this is a test &lt; &amp; &gt; &apos; &quot; string", escaped);
 }

 @Test
 public void testUnescape() {
 String escaped = "this is a test &lt; &amp; &gt; &apos; &quot; string";
 String unescaped = Entities.unescape(escaped);
 assertEquals("this is a test < & > ' \" string", unescaped);
 }

 @Test
 public void testEscapeAndUnescapeRoundtrip() {
 String string = "this is a test < & > ' \" string";
 String escaped = Entities.escape(string, null, Entities.EscapeMode.base);
 String unescaped = Entities.unescape(escaped);
 assertEquals(string, unescaped);
 }
}