import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

 private static final Entities entities = new Entities();

 @Test
 public void testIsNamedEntity() {
 // Test isNamedEntity method
 for (Entities.EscapeMode mode : Entities.EscapeMode.values()) {
 for (Map.Entry<Character, String> entry : mode.getMap().entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();

 Assert.assertTrue("Named entity " + value + " should be present in " + mode, entities.isNamedEntity(value));
 }
 }
 }

 @Test
 public void testIsNamedEntityWithInvalidEntityName() {
 // Test isNamedEntity method with invalid entity name
 Assert.assertFalse("Invalid entity name should not be present in any entity set", entities.isNamedEntity("invalidEntity"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test getCharacterByName method
 for (Entities.EscapeMode mode : Entities.EscapeMode.values()) {
 for (Map.Entry<Character, String> entry : mode.getMap().entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();

 Character result = entities.getCharacterByName(value);

 Assert.assertEquals("Named entity " + value + " should map to character " + key, key, result);
 }
 }
 }

 @Test
 public void testEscapeMethodWithDefaultEncoderAndEscapeMode() {
 // Test escape method with default encoder and EscapeMode.base
 String input = "Test < & >";
 String expectedOutput = "Test &lt; &amp; &gt;";
 String actualOutput = entities.escape(input, null, Entities.EscapeMode.base);

 Assert.assertEquals("escape method output does not match expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testEscapeMethodWithCustomEncoderAndEscapeMode() {
 // Test escape method with custom encoder and EscapeMode.base
 String input = "Test < & >";
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 String expectedOutput = "Test &#60; &#38; &#62;";
 String actualOutput = entities.escape(input, encoder, Entities.EscapeMode.base);

 Assert.assertEquals("escape method output does not match expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testEscapeMethodWithCustomEncoderAndXHtmlEscapeMode() {
 // Test escape method with custom encoder and EscapeMode.xhtml
 String input = "Test < & >";
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 String expectedOutput = "Test &lt; &amp; &gt;";
 String actualOutput = entities.escape(input, encoder, Entities.EscapeMode.xhtml);

 Assert.assertEquals("escape method output does not match expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testEscapeMethodWithCustomEncoderAndExtendedEscapeMode() {
 // Test escape method with custom encoder and EscapeMode.extended
 String input = "Test < & > © ®";
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 String expectedOutput = "Test &lt; &amp; &gt; &#169; &#174;";
 String actualOutput = entities.escape(input, encoder, Entities.EscapeMode.extended);

 Assert.assertEquals("escape method output does not match expected output", expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeMethodWithNullInput() {
 // Test escape method with null input
 String input = null;
 entities.escape(input, null, Entities.EscapeMode.base);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeMethodWithNullEscapeMode() {
 // Test escape method with null EscapeMode
 String input = "Test < & >";
 String output = entities.escape(input, null, null);
 }

 @Test
 public void testUnescapeMethodWithSimpleEntities() {
 // Test unescape method with simple entities
 String input = "Test &lt; &amp; &gt;";
 String expectedOutput = "Test < & >";
 String actualOutput = entities.unescape(input);

 Assert.assertEquals("unescape method output does not match expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testUnescapeMethodWithNamedEntities() {
 // Test unescape method with named entities
 String input = "Test &quot; &amp; &lt; &gt;";
 String expectedOutput = "Test \" & & < >";
 String actualOutput = entities.unescape(input);

 Assert.assertEquals("unescape method output does not match expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testUnescapeMethodWithNumericEntities() {
 // Test unescape method with numeric entities
 String input = "Test &#x22; &#38; &#x3C; &#x3E;";
 String expectedOutput = "Test \" & < >";
 String actualOutput = entities.unescape(input);

 Assert.assertEquals("unescape method output does not match expected output", expectedOutput, actualOutput);
 }

 @Test
 public void testUnescapeMethodWithInvalidEntities() {
 // Test unescape method with invalid entities
 String input = "Test &invalid &";
 String output = entities.unescape(input);

 Assert.assertEquals("Input should not be unescaped", input, output);
 }

 @Test
 public void testUnescapeMethodWithStrictMode() {
 // Test unescape method with strict mode
 String input = "Test &lt; &amp; &gt;";
 String expectedOutput = "Test < & >";
 String actualOutput = entities.unescape(input, true);

 Assert.assertEquals("unescape method output does not match expected output", expectedOutput, actualOutput);
 }
}