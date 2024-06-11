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
 Character character = entry.getValue();
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
 Character character = entry.getValue();
 assertEquals("Named entity '" + name + "' should map to character " + (int) character, character, Entities.getCharacterByName(name));
 }
 }

 @Test
 public void testEscapeWithXhtmlEscapeMode() {
 String input = "&lt; &gt; &amp; &apos; &quot;";
 String expectedOutput = "&amp;lt; &amp;gt; &amp;amp; &amp;apos; &amp;quot;";
 String output = Entities.escape(input, Entities.EscapeMode.xhtml, null);
 assertEquals("Escaped string with XHTML escape mode is incorrect", expectedOutput, output);
 }

 @Test
 public void testEscapeWithBaseEscapeMode() {
 String input = "&lt; &gt; &amp; &apos; &quot;";
 String expectedOutput = "&lt;amp;&gt;amp;&apos;amp;&quot;";
 String output = Entities.escape(input, Entities.EscapeMode.base, null);
 assertEquals("Escaped string with base escape mode is incorrect", expectedOutput, output);
 }

 @Test
 public void testEscapeWithExtendedEscapeMode() {
 String input = "&lt; &gt; &amp; &apos; &quot; <script>alert('XSS');</script>";
 String expectedOutput = "&lt;amp;&gt;amp;&apos;amp;&quot; &amp;lt;script&gt;&amp;amp;alert('XSS');&amp;lt;/script&gt;";
 String output = Entities.escape(input, Entities.EscapeMode.extended, null);
 assertEquals("Escaped string with extended escape mode is incorrect", expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testEscapeWithNullInput() {
 Entities.escape(null, Entities.EscapeMode.extended, null);
 }

 @Test
 public void testUnescapeWithValidInput() {
 String input = "&amp;lt;amp;&gt;amp;&apos;amp;&#123;quot;&#x26;&#125;";
 String expectedOutput = "&lt;&gt;&apos;&#34;&amp;&#x26;&#125;";
 String output = Entities.unescape(input, false);
 assertEquals("Unescaped string is incorrect", expectedOutput, output);
 }

 @Test
 public void testUnescapeWithInvalidInput() {
 String input = "&amp;invalid";
 String expectedOutput = input;
 String output = Entities.unescape(input, false);
 assertEquals("Unescaped string is incorrect", expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testUnescapeWithNullInput() {
 Entities.unescape(null, false);
 }

 @Test
 public void testUnescapeWithStrictMode() {
 String input = "&lt;script&gt;alert('XSS');&lt;/script&gt;";
 String expectedOutput = "&lt;script&gt;&amp;lt;script&gt;alert('XSS');&amp;lt;script&gt;&lt;/script&gt;";
 String output = Entities.unescape(input, true);
 assertEquals("Unescaped string with strict mode is incorrect", expectedOutput, output);
 }

 @Test
 public void testLoadEntitiesFromPropertiesFile() {
 Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
 for (Map.Entry<String, Character> entry : entities.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();
 assertEquals("Named entity '" + name + "' should map to character " + (int) character, character, Entities.getCharacterByName(name));
 }
 }

 @Test(expected = MissingResourceException.class)
 public void testLoadEntitiesWithInvalidFileName() {
 Entities.loadEntities("entities-invalid.properties");
 }

}