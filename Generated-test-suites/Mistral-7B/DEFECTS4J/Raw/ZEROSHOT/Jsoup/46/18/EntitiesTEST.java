package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Map<Character, String> full = Entities.full;
 for (Map.Entry<Character, String> entry : full.entrySet()) {
 CharactersAssert.assertTrue("Named entity " + entry.getKey() + " should be in the full entity set.", Entities.isNamedEntity(entry.getValue().toString()));
 }

 Map<Character, String> base = Entities.base;
 for (Map.Entry<Character, String> entry : base.entrySet()) {
 CharactersAssert.assertTrue("Named entity " + entry.getKey() + " should be in the base entity set.", Entities.isBaseNamedEntity(entry.getValue().toString()));
 }

 String invalidEntity = "\u0001";
 CharactersAssert.assertFalse("Invalid entity " + invalidEntity + " should not be a named entity.", Entities.isNamedEntity(invalidEntity));
 }

 @Test
 public void testGetCharacterByName() {
 Map<Character, String> full = Entities.full;
 for (Map.Entry<Character, String> entry : full.entrySet()) {
 Character expected = entry.getKey();
 String name = entry.getValue().toString();
 Character actual = Entities.getCharacterByName(name);
 CharactersAssert.assertEquals("Named entity " + name + " should return the correct character.", expected, actual);
 }
 }

 @Test
 public void testEscape() {
 Character characterToEscape = '&';
 String expectedEscapedString = "&amp;";
 String inputString = "This string contains the character to escape: " + characterToEscape;
 String outputString = Entities.escape(inputString, Document.OutputSettings.base());
 CharactersAssert.assertEquals("Escaping the character " + characterToEscape + " should produce the string \"" + expectedEscapedString + "\".", expectedEscapedString, outputString);
 }

 @Test
 public void testUnescape() {
 String entityString = "This string contains &amp; and &lt; and &gt; entities.";
 String expectedUnescapedString = "This string contains & and < and >.";
 String outputString = Entities.unescape(entityString);
 CharactersAssert.assertEquals("Unescaping the string should produce the expected string.", expectedUnescapedString, outputString);
 }

 private static class CharactersAssert {
 static void assertFalse(String message, boolean condition) {
 Assert.assertFalse(message, condition);
 }

 static void assertEquals(String message, Character expected, Character actual) {
 Assert.assertEquals(message, expected.intValue(), actual.intValue());
 }
 }
}