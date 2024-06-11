package org.jsoup.nodes;

import org.hamcrest.CoreMatchers;import org.hamcrest.MatcherAssert.*;import org.junit.BeforeClass;import org.junit.Test;import static org.junit.Assert.*;

import java.io.IOException;import java.nio.charset.StandardCharsets;import java.util.HashMap;import java.util.Map;

/**
 * JUnit 4 test file for the Entities class.
 */
public class EntitiesTest {

 private static Map<Character, String> base;
 private static Map<Character, String> full;
 private static Map<Character, String> xhtmlByVal;
 private static Map<Character, String> baseByVal;
 private static Map<Character, String> fullByVal;

 @BeforeClass
 public static void init() {
 base = Entities.base;
 full = Entities.full;
 xhtmlByVal = Entities.xhtmlByVal;
 baseByVal = Entities.baseByVal;
 fullByVal = Entities.fullByVal;
 }

 /**
 * Test method for {@link Entities#isNamedEntity(String)} with a known named entity.
 */
 @Test
 public void testIsNamedEntity_knownEntity() {
 String namedEntity = "lt";
 assertTrue(Entities.isNamedEntity(namedEntity));
 }

 /**
 * Test method for {@link Entities#isNamedEntity(String)} with an unknown entity.
 */
 @Test
 public void testIsNamedEntity_unknownEntity() {
 String unknownEntity = "unknown";
 assertFalse(Entities.isNamedEntity(unknownEntity));
 }

 /**
 * Test method for {@link Entities#isBaseNamedEntity(String)} with a known base named entity.
 */
 @Test
 public void testIsBaseNamedEntity_knownEntity() {
 String baseNamedEntity = "amp";
 assertTrue(Entities.isBaseNamedEntity(baseNamedEntity));
 }

 /**
 * Test method for {@link Entities#isBaseNamedEntity(String)} with an unknown entity.
 */
 @Test
 public void testIsBaseNamedEntity_unknownEntity() {
 String unknownEntity = "unknown";
 assertFalse(Entities.isBaseNamedEntity(unknownEntity));
 }

 /**
 * Test method for {@link Entities#getCharacterByName(String)} with a known named entity.
 */
 @Test
 public void testGetCharacterByName_knownEntity() {
 String namedEntity = "lt";
 Character expected = '<';
 Character actual = Entities.getCharacterByName(namedEntity);
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link Entities#getCharacterByName(String)} with an unknown entity.
 */
 @Test (expected = IllegalArgumentException.class)
 public void testGetCharacterByName_unknownEntity() {
 String unknownEntity = "unknown";
 Entities.getCharacterByName(unknownEntity);
 }

 /**
 * Test method for {@link Entities#escape(String, Document.OutputSettings)} with a simple string.
 */
 @Test
 public void testEscape_simpleString() {
 String input = "Hello & World!";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 String expected = "Hello &amp; World!";
 String actual = Entities.escape(input, outputSettings);
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link Entities#escape(String, Document.OutputSettings)} with a string containing named entities.
 */
 @Test
 public void testEscape_stringWithNamedEntities() {
 String input = "Hello &lt;World&gt;!";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 String expected = "Hello &lt;amp;&gt;lt;&gt;World&lt;/amp;&gt;!";
 String actual = Entities.escape(input, outputSettings);
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link Entities#escape(String, Document.OutputSettings)} with a string containing special characters and named entities.
 */
 @Test
 public void testEscape_stringWithSpecialCharsAndNamedEntities() {
 String input = "Hello &lt;World&gt;! &nbsp; &quot; &amp; &apos;";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 String expected = "Hello &lt;amp;&gt;lt;&gt;World&lt;/amp;&gt;! &nbsp; &quot; &amp;amp;&quot; &amp;amp;&apos;";
 String actual = Entities.escape(input, outputSettings);
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link Entities#escape(String, Document.OutputSettings)} with a string containing a surrogate pair.
 */
 @Test
 public void testEscape_stringWithSurrogatePair() {
 String input = "\uD800\uDC00";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 String expected = "&#x10000;";
 String actual = Entities.escape(input, outputSettings);
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link Entities#unescape(String)} with a simple string.
 */
 @Test
 public void testUnescape_simpleString() {
 String input = "Hello &amp; World!";
 String expected = "Hello & World!";
 String actual = Entities.unescape(input);
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link Entities#unescape(String)} with a string containing named entities.
 */
 @Test
 public void testUnescape_stringWithNamedEntities() {
 String input = "Hello &lt;World&gt;!";
 String expected = "Hello <World>";
 String actual = Entities.unescape(input);
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link Entities#canEncode(CoreCharset, char, CharsetEncoder)} with a common ASCII character.
 */
 @Test
 public void testCanEncode_asciiChar() {
 char c = 'A';
 CoreCharset charset = CoreCharset.ascii;
 boolean expected = true;
 boolean actual = Entities.canEncode(charset, c, null);
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link Entities#canEncode(CoreCharset, char, CharsetEncoder)} with a non-ASCII character.
 */
 @Test
 public void testCanEncode_nonAsciiChar() {
 char c = '\u00E9';
 CoreCharset charset = CoreCharset.utf;
 boolean expected = true;
 boolean actual = Entities.canEncode(charset, c, null);
 assertEquals(expected, actual);
 }

 /**
 * Test method for {@link Entities#canEncode(CoreCharset, char, CharsetEncoder)} with a character that cannot be encoded by the given CharsetEncoder.
 */
 @Test
 public void testCanEncode_unencodableChar() {
 char c = '\uD800';
 CoreCharset charset = CoreCharset.ascii;
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 boolean expected = false;
 boolean actual = Entities.canEncode(charset, c, encoder);
 assertEquals(expected, actual);
 }

}