import org.junit.Test;
import org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private static final Map<String, Character> full = Entities.full;
 private static final Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;
 private static final Map<Character, String> baseByVal = Entities.baseByVal;
 private static final Map<Character, String> base = Entities.base;

 @Test
 public void testIsNamedEntity() {
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertTrue("Named entity '" + name + "' should be known", Entities.isNamedEntity(name));
 }
 }

 @Test(expected = AssertionError.class)
 public void testIsNamedEntityUnknownEntity() {
 assertFalse("Unknown entity should not be known", Entities.isNamedEntity("unkownEntity"));
 }

 @Test
 public void testGetCharacterByName() {
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertEquals("Named entity '" + name + "' should map to character '" + character + "'", character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testEscape() {
 String input = "This is a test string with &amp; < > characters.";
 String expectedOutput = "This is a test string with &amp; &lt; &gt; characters.";
 Document.OutputSettings out = new Document.OutputSettings();
 String output = Entities.escape(input, out);
 assertEquals("escape method should produce expected output", expectedOutput, output);
 }

 @Test
 public void testUnescape() {
 String input = "This is a test string with &lt;lt&gt; &amp; &amp;amp; characters.";
 String expectedOutput = "This is a test string with <lt> & &amp;amp;";
 String output = Entities.unescape(input);
 assertEquals("unescape method should produce expected output", expectedOutput, output);
 }

 @Test(expected = AssertionError.class)
 public void testUnescapeUnexpectedCharacter() {
 String input = "This is a test string with &%#!@ characters.";
 String output = Entities.unescape(input);
 fail("unescape method should throw an exception for unexpected character sequence");
 }

 @Test
 public void testXhtmlEntities() {
 Map<Character, String> xhtmlMap = Entities.EscapeMode.xhtml.getMap();
 for (Map.Entry<Character, String> entry : xhtmlMap.entrySet()) {
 Character character = entry.getKey();
 String expectedName = entry.getValue();
 assertEquals("XHTML entity '" + character + "' should map to named entity '" + expectedName + "'", expectedName, Entities.getCharacterByName(expectedName));
 }
 }

 @Test(expected = AssertionError.class)
 public void testGetCharacterByNameUnknownEntity() {
 Character character = Entities.getCharacterByName("unkownEntity");
 fail("getCharacterByName method should throw an exception for unknown entity");
 }

 @Test
 public void testLoadEntities() {
 Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
 for (Map.Entry<String, Character> entry : entities.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertEquals("Loaded entity '" + name + "' should map to character '" + character + "'", character, Entities.getCharacterByName(name));
 }
 }
}