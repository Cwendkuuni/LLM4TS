package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();
 // Test known named entities
 assertTrue(entities.isNamedEntity("lt"));
 assertTrue(entities.isNamedEntity("amp"));
 assertTrue(entities.isNamedEntity("apos"));
 assertTrue(entities.isNamedEntity("quot"));
 // Test unknown named entities
 assertFalse(entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 Entities entities = new Entities();
 // Test known named entities
 assertEquals('&', entities.getCharacterByName("amp"));
 assertEquals('<', entities.getCharacterByName("lt"));
 assertEquals('\'', entities.getCharacterByName("apos"));
 assertEquals('\"', entities.getCharacterByName("quot"));
 // Test unknown named entities
 assertNull(entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 Entities entities = new Entities();
 String input = "This is a test & with special < characters & some numbers: 123";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 String expectedOutput = "This is a test &amp; with special &lt; characters &amp; some numbers: 123";
 assertEquals(expectedOutput, entities.escape(input, outputSettings));
 }

 @Test
 public void testUnescape() {
 Entities entities = new Entities();
 String input = "This is a test &amp; with special &lt; characters &amp; some numbers: 123";
 String expectedOutput = "This is a test & with special < characters & some numbers: 123";
 assertEquals(expectedOutput, entities.unescape(input));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnsupportedChar() {
 Entities entities = new Entities();
 String input = "This is a test ☬ with special characters & some numbers: 123";
 String output = entities.unescape(input);
 fail("Expected IllegalArgumentException but no exception was thrown");
 }
}