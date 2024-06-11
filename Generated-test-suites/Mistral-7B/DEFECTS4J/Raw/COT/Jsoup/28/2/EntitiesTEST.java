import org.junit.Test;
import org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private static final Entities entities = new Entities();

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertTrue(Entities.isNamedEntity(name));
 assertEquals(character, Entities.getCharacterByName(name));
 }
 }

 @Test(expected = AssertionError.class)
 public void testIsNamedEntityWithInvalidName() {
 assertFalse(Entities.isNamedEntity(""));
 }

 @Test
 public void testGetCharacterByName() {
 Map<String, Character> full = Entities.full;
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertEquals(character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testEscapeMethod() {
 String input = "This is a test string with some special characters: <>\"&'";
 EscapeMode escapeMode = Entities.EscapeMode.base;
 String expectedOutput = "This is a test string with some special characters: &lt;&gt;&quot;&amp;&#39;";
 String output = Entities.escape(input, null, escapeMode.getMap());
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescapeMethod() {
 String input = "This is a test string with some special characters: &lt;&gt;&quot;&amp;&#39;";
 String output = Entities.unescape(input);
 String expectedOutput = "This is a test string with some special characters: <>\"&'";
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescapeWithStrictMode() {
 String input = "This is a test string with some special characters: &lt;&gt;&quot;&amp;&#39;";
 String output = Entities.unescape(input, true);
 String expectedOutput = "This is a test string with some special characters: < > \" & '";
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testLoadEntitiesMethod() {
 Map<String, Character> loadedEntities = Entities.loadEntities("entities-full.properties");
 Map<String, Character> expectedEntities = Entities.full;
 for (Map.Entry<String, Character> entry : expectedEntities.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertEquals(character, loadedEntities.get(name));
 }
 }

 @Test(expected = AssertionError.class)
 public void testLoadEntitiesWithMissingFile() {
 Entities.loadEntities("non-existent-file.properties");
 }

 @Test
 public void testToCharacterKeyMethod() {
 Map<String, Character> inputMap = new HashMap<>();
 inputMap.put("quot", '\'');
 inputMap.put("amp", '&');
 Map<Character, String> outputMap = Entities.toCharacterKey(inputMap);
 for (Map.Entry<Character, String> entry : outputMap.entrySet()) {
 Character character = entry.getKey();
 String name = entry.getValue();
 assertEquals(name, Entities.getCharacterByName(name));
 assertEquals(character, Entities.getCharacterByName(name));
 }
 }

}