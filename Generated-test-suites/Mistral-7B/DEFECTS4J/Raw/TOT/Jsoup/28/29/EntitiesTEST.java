package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Testing {@link Entities} class.
 */
public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();
 // true cases
 assertTrue(entities.isNamedEntity("lt"));
 assertTrue(entities.isNamedEntity("amp"));
 // false cases
 assertFalse(entities.isNamedEntity("gt"));
 assertFalse(entities.isNamedEntity("apples"));
 }

 @Test
 public void testGetCharacterByName() {
 Entities entities = new Entities();
 // known named entities
 assertEquals('<', entities.getCharacterByName("lt"));
 assertEquals('&', entities.getCharacterByName("amp"));
 // unknown named entity
 assertNull(entities.getCharacterByName("gt"));
 }

 @Test
 public void testEscape() {
 Entities entities = new Entities();
 String input = "Hello <world>! & this is a test.";
 String expectedOutput = "Hello &lt;world&gt;! &amp; this is a test.";
 String output = entities.escape(input, Document.OutputSettings.xhtml());
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testEscapeInvalidCharacter() {
 Entities entities = new Entities();
 String input = "Hello Çöld! & this is a test.";
 String expectedOutput = "Hello &#0154;ld! &amp; this is a test.";
 String output = entities.escape(input, Document.OutputSettings.xhtml());
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescape() {
 Entities entities = new Entities();
 String input = "Hello &lt;world&gt;! &amp; this is a test.";
 String expectedOutput = "Hello <world>! & this is a test.";
 String output = entities.unescape(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescapeInvalidCharacter() {
 Entities entities = new Entities();
 String input = "Hello &#154;ld;! &amp; this is a test.";
 String expectedOutput = "Hello &#154;ld;! & this is a test.";
 String output = entities.unescape(input);
 assertSame(input, output);
 }
}