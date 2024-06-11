import org.junit.Test;
import org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private Entities entities = new Entities();

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character charValue = entry.getValue();
 assertTrue("Named entity '" + name + "' should be recognized", Entities.isNamedEntity(name));
 }
 }

 @Test
 public void testIsNamedEntityWithInvalidName() {
 assertFalse("Invalid named entity name should not be recognized", Entities.isNamedEntity(""));
 }

 @Test
 public void testGetCharacterByName() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character charValue = entry.getValue();
 assertEquals("Named entity '" + name + "' should return expected character", charValue, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testEscapeMethod() {
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 String input = "Test < Entities > Class";
 String expectedOutputXHTML = "Test &lt; Entities &gt; Class";
 String expectedOutputBase = "Test &lt;Entities&gt; Class";
 String expectedOutputExtended = "Test &amp;lt; Entities &amp;gt; Class";

 String outputXHTML = Entities.escape(input, outputSettings.xhtml());
 String outputBase = Entities.escape(input, outputSettings.base());
 String outputExtended = Entities.escape(input, Entities.EscapeMode.extended);

 assertEquals("Escape method with XHTML output should return expected value", expectedOutputXHTML, outputXHTML);
 assertEquals("Escape method with default output should return expected value", expectedOutputBase, outputBase);
 assertEquals("Escape method with extended output should return expected value", expectedOutputExtended, outputExtended);
 }

 @Test
 public void testUnescapeMethodWithValidInput() {
 String input = "Test &lt; Entities &gt; Class";
 String expectedOutput = "Test < Entities > Class";
 String output = Entities.unescape(input);
 assertEquals("Unescape method should return expected value", expectedOutput, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeMethodWithNullInput() {
 Entities.unescape(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeMethodWithInvalidInput1() {
 String input = "Test &amp;lt; Entities &amp;gt; Class";
 String output = Entities.unescape(input);
 String unexpectedOutput = "Test < Entities > Class"; // this should not be the output since "&lt;" is not a named entity
 assertEquals("Unescape method should throw IllegalArgumentException with invalid input", output, unexpectedOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeMethodWithInvalidInput2() {
 String input = "Test &amp;amp;lt; Entities &amp;gt; Class";
 String output = Entities.unescape(input);
 String unexpectedOutput = "Test &amp;lt; Entities &amp;gt; Class"; // this should not be the output since "&amp;lt;" is "&amp;" entity itself
 assertEquals("Unescape method should throw IllegalArgumentException with invalid input", output, unexpectedOutput);
 }

 @Test
 public void testUnescapeMethodWithInvalidInput3() {
 String input = "Test &amp;#123; Entities &amp;#125; Class";
 String expectedOutput = "Test { Entities } Class";
 String output = Entities.unescape(input);
 assertEquals("Unescape method should return expected value", expectedOutput, output);
 }

 @Test
 public void testUnescapeMethodWithInvalidInput4() {
 String input = "Test &amp;#x123; Entities &amp;#x125; Class";
 String expectedOutput = "Test { Entities } Class";
 String output = Entities.unescape(input);
 assertEquals("Unescape method should return expected value", expectedOutput, output);
 }

 @Test
 public void testIsNamedEntityWithInvalidEntityName() {
 assertFalse("Invalid named entity name should not be recognized", Entities.isNamedEntity(""));
 }

 @Test
 public void testGetCharacterByNameWithInvalidEntityName() {
 assertNull("Invalid named entity name should return null", Entities.getCharacterByName(""));
 }

 private Map<String, Character> loadEntitiesFromFile(String filename) {
 Properties properties = new Properties();
 try {
 InputStream inputStream = Entities.class.getResourceAsStream(filename);
 properties.load(inputStream);
 inputStream.close();
 } catch (IOException e) {
 throw new RuntimeException("Failed to read entities file: " + filename, e);
 }
 Map<String, Character> entities = new HashMap<String, Character>();
 for (Map.Entry<Object, Object> entry : properties.entrySet()) {
 String name = (String) entry.getKey();
 Character value = Character.valueOf((char) Integer.parseInt((String) entry.getValue(), 16));
 entities.put(name, value);
 }
 return entities;
 }

 @Test
 public void testLoadEntitiesFromFile() {
 Map<String, Character> fullEntities = loadEntitiesFromFile("entities-full.properties");
 Map<String, Character> baseEntities = loadEntitiesFromFile("entities-base.properties");
 Map<Character, String> fullEntitiesByName = Entities.toCharacterKey(fullEntities);
 Map<Character, String> baseEntitiesByName = Entities.toCharacterKey(baseEntities);

 for (Map.Entry<Character, String> entry : fullEntitiesByName.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();
 assertEquals("Character to String mapping for full entities should be correct", value, Entities.getCharacterByName(value));
 }

 for (Map.Entry<Character, String> entry : baseEntitiesByName.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();
 assertEquals("Character to String mapping for base entities should be correct", value, Entities.getCharacterByName(value));
 }
 }
}