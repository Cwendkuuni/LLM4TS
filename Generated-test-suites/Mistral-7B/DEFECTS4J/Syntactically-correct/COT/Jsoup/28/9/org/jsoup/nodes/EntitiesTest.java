import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.*;

public class EntitiesTest {

 private static final Map<Character, String> base = Entities.base;
 private static final Map<Character, String> full = Entities.full;
 private static final Map<Character, String> xhtmlByVal = Entities.EscapeMode.xhtml.getMap();

 @Test
 public void testIsNamedEntity() {
 for (Map.Entry<Character, String> entry : full.entrySet()) {
 assertTrue(Entities.isNamedEntity(entry.getValue()));
 }
 }

 @Test
 public void testGetCharacterByName() {
 for (Map.Entry<Character, String> entry : full.entrySet()) {
 Character expected = entry.getKey();
 String name = entry.getValue();
 Character actual = Entities.getCharacterByName(name);
 assertEquals(expected, actual);
 }
 }

 @Test
 public void testEscapeMethod() {
 String input = "This is a test for Entities.escape() method.";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.setEncoder(StandardCharsets.UTF_8.newEncoder());
 outputSettings.setEscapeMode(Entities.EscapeMode.extended);

 String expected = "This is a test for Entities.escape() method.";
 String actual = Entities.escape(input, outputSettings);
 assertEquals(expected, actual);
 }

 @Test
 public void testUnescapeMethod() {
 String input = "&lt;Hello&amp; World&gt;";
 String expected = "<Hello World>";
 String actual = Entities.unescape(input);
 assertEquals(expected, actual);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testUnescapeMethodWithInvalidEntity() {
 String input = "&abcd;";
 String actual = Entities.unescape(input);
 fail("Expected an IllegalArgumentException, but got: " + actual);
 }

 @Test
 public void testUnescapeMethodWithNoEntities() {
 String input = "Hello World";
 String expected = "Hello World";
 String actual = Entities.unescape(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsNamedEntityWithInvalidEntity() {
 assertFalse(Entities.isNamedEntity("abcd"));
 }

 @Test
 public void testGetCharacterByNameWithInvalidEntity() {
 try {
 Entities.getCharacterByName("abcd");
 fail("Expected a NullPointerException, but got null.");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testEscapeMethodWithNullString() {
 String input = null;
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.setEncoder(StandardCharsets.UTF_8.newEncoder());
 outputSettings.setEscapeMode(Entities.EscapeMode.extended);

 String expected = null;
 String actual = Entities.escape(input, outputSettings);
 assertEquals(expected, actual);
 }

 @Test
 public void testEscapeMethodWithEmptyString() {
 String input = "";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.setEncoder(StandardCharsets.UTF_8.newEncoder());
 outputSettings.setEscapeMode(Entities.EscapeMode.extended);

 String expected = "";
 String actual = Entities.escape(input, outputSettings);
 assertEquals(expected, actual);
 }

 @Test
 public void testEscapeMethodWithSpecialChars() {
 String input = "&<>&\"'";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.setEncoder(StandardCharsets.UTF_8.newEncoder());
 outputSettings.setEscapeMode(Entities.EscapeMode.extended);

 String expected = "&amp;<amp;&amp;gt;&amp;#34;&amp;#39;";
 String actual = Entities.escape(input, outputSettings);
 assertEquals(expected, actual);
 }

 @Test
 public void testXhtmlByValMap() {
 Map<Character, String> expected = Entities.xhtmlByVal;
 Map<Character, String> actual = Entities.EscapeMode.xhtml.getMap();
 assertEquals(expected, actual);
 }

 @Test
 public void testBaseMap() {
 Map<Character, String> expected = Entities.baseByVal;
 Map<Character, String> actual = Entities.getMapForEscapeMode(Entities.EscapeMode.base);
 assertEquals(expected, actual);
 }

 @Test
 public void testFullMap() {
 Map<Character, String> expected = Entities.fullByVal;
 Map<Character, String> actual = Entities.getMapForEscapeMode(Entities.EscapeMode.extended);
 assertEquals(expected, actual);
 }

 @Test
 public void testLoadEntitiesMethod() {
 String filename = "entities-base.properties";
 Map<String, Character> expected = Entities.base;
 Map<String, Character> actual = Entities.loadEntities(filename);
 assertEquals(expected, actual);
 }

 @Test
 public void testToCharacterKeyMethod() {
 Map<String, Character> input = Entities.base;
 Map<Character, String> expected = Entities.baseByVal;
 Map<Character, String> actual = Entities.toCharacterKey(input);
 assertEquals(expected, actual);
 }

}