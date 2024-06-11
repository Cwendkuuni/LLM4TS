import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private static final Map<String, Character> full = Entities.full;
 private static final Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;
 private static final Map<String, Character> base = Entities.base;
 private static final Map<Character, String> baseByVal = Entities.baseByVal;
 private static final Map<Character, String> fullByVal = Entities.fullByVal;

 @Test
 public void testIsNamedEntity() {
 // Test case for isNamedEntity method
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 Assert.assertTrue("Named entity " + entry.getKey() + " should be known", Entities.isNamedEntity(entry.getKey()));
 }
 }

 @Test
 public void testIsNamedEntityWithInvalidName() {
 // Test case for isNamedEntity method with invalid name
 Assert.assertFalse("Invalid named entity should not be known", Entities.isNamedEntity(""));
 }

 @Test
 public void testGetCharacterByName() {
 // Test case for getCharacterByName method
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 Character character = Entities.getCharacterByName(entry.getKey());
 Assert.assertEquals("Named entity " + entry.getKey() + " should have character value " + character, character, entry.getValue());
 }
 }

 @Test
 public void testGetCharacterByNameWithInvalidName() {
 // Test case for getCharacterByName method with invalid name
 Assert.assertNull("Invalid named entity should not have character value", Entities.getCharacterByName(""));
 }

 @Test
 public void testEscapeMethodWithDefaultEncodingAndXhtmlMode() {
 // Test case for escape method with default encoding and XHTML mode
 String input = "&lt;&amp;&apos;&quot;test&nbsp;";
 String output = Entities.escape(input, null, Entities.EscapeMode.xhtml);
 Assert.assertEquals("Escaped XHTML output should be &lt; &amp; &apos; &quot; test ", output, "&lt; &amp; &apos; &quot; test ");
 }

 @Test
 public void testEscapeMethodWithCustomEncodingAndBaseMode() {
 // Test case for escape method with custom encoding and Base mode
 String input = "ẠÀẢẠẤẦẪẨẬẪ";
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 String output = Entities.escape(input, encoder, Entities.EscapeMode.base);
 Assert.assertEquals("Escaped Base output should be &#8835;&#194;&#192;&#193;&#194;&#195;&#194;&#196;", output, input);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeMethodWithInvalidEscapeMode() {
 // Test case for escape method with invalid EscapeMode
 String input = "test";
 String output = Entities.escape(input, null, null);
 Assert.fail("Escape method should throw IllegalArgumentException with invalid EscapeMode");
 }

 @Test
 public void testUnescapeMethodWithValidString() {
 // Test case for unescape method with valid string
 String input = "&lt; &amp; &apos; &quot; test &nbsp;";
 String output = Entities.unescape(input);
 Assert.assertEquals("Unescaped output should be < & amp; ' & quot; test ", output, "< & amp; ' & quot; test ");
 }

 @Test
 public void testUnescapeMethodWithEmptyString() {
 // Test case for unescape method with empty string
 String input = "";
 String output = Entities.unescape(input);
 Assert.assertEquals("Unescaped empty string should be empty string", output, "");
 }

 @Test
 public void testUnescapeMethodWithInvalidString() {
 // Test case for unescape method with invalid string
 String input = "&";
 String output = Entities.unescape(input);
 Assert.assertEquals("Unescaped invalid string should be empty string", output, "");
 }

 @Test
 public void testUnescapeMethodWithStrictMode() {
 // Test case for unescape method with strict mode
 String input = "&lt; &amp; &apos; &quot; test &nbsp;";
 String output = Entities.unescape(input, true);
 Assert.assertEquals("Unescaped output with strict mode should be < &amp; ' &quot; test ", output, "< &amp; ' &quot; test ");
 }

 @Test
 public void testUnescapeMethodWithNonExistentEntity() {
 // Test case for unescape method with non-existent entity
 String input = "&non-existent;";
 String output = Entities.unescape(input);
 Assert.assertEquals("Unescaped output with non-existent entity should be &non-existent;", output, "&non-existent;");
 }

 @Test
 public void testLoadEntitiesMethod() {
 // Test case for loadEntities method
 Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
 // Verify loaded entities
 for (Map.Entry<String, Character> entry : entities.entrySet()) {
 Character character = entry.getValue();
 String name = entry.getKey();
 Assert.assertEquals("Loaded entity " + name + " should have character value " + character, character, Entities.getCharacterByName(name));
 }
 }

 @Test(expected = IOException.class)
 public void testLoadEntitiesMethodWithMissingFile() {
 // Test case for loadEntities method with missing file
 Entities.loadEntities("nonexistentfile.properties");
 }

 @Test
 public void testToCharacterKeyMethod() {
 // Test case for toCharacterKey method
 Map<String, Character> entities = new HashMap<>();
 entities.put("test", (char) 1);
 Map<Character, String> output = Entities.toCharacterKey(entities);
 Character character = output.keySet().iterator().next();
 String name = output.get(character);
 Assert.assertEquals("Converted entity " + name + " should have character value " + character, character, "test".charAt(0));
 Assert.assertEquals("Converted entity name should be 'test'", name, "test");
 }

}