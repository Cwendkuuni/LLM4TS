import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private static final Map<String, Character> full = Entities.full;
 private static final Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;
 private static final Map<Character, String> baseByVal = Entities.baseByVal;
 private static final Map<Character, String> base = Entities.base;

 @Test
 public void testIsNamedEntity() {
 Map<Character, String> map = Entities.EscapeMode.xhtml.getMap();
 for (Map.Entry<Character, String> entry : map.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();

 boolean result = Entities.isNamedEntity(value);
 Assert.assertTrue("Named entity " + value + " should be known", result);
 }
 }

 @Test
 public void testGetCharacterByName() {
 Map<Character, String> map = Entities.EscapeMode.xhtml.getMap();
 for (Map.Entry<Character, String> entry : map.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();

 Character expected = Entities.getCharacterByName(value);
 Character result = key;

 Assert.assertEquals("Named entity " + value + " should have corresponding character " + expected, expected, result);
 }
 }

 @Test
 public void testEscapeMethodWithXhtmlOutput() {
 String input = "This is a test &amp; with some &lt;entities&gt; and &quot;quotes&quot;.";
 String expectedOutput = "This is a test &amp; with some &lt;entities&gt; and &quot;quotes&quot;.";
 String output = Entities.escape(input, Entities.Document.OutputSettings.xhtml());

 Assert.assertEquals("escape method with XHTML output should not change input", expectedOutput, output);
 }

 @Test
 public void testEscapeMethodWithBaseOutput() {
 String input = "This is a test &amp; with some &lt;entities&gt; and &quot;quotes&quot;.";
 String expectedOutput = "This is a test &amp;amp; with some &lt;entities&gt; and &quot;quotes&quot;.";
 String output = Entities.escape(input, null);

 Assert.assertEquals("escape method with base output should add 'amp' entity for '&' and not change other characters", expectedOutput, output);
 }

 @Test(expected = NullPointerException.class)
 public void testEscapeMethodWithNullOutputSettings() {
 String input = "This is a test &amp; with some &lt;entities&gt; and &quot;quotes&quot;.";
 String output = Entities.escape(input, null);
 }

 @Test
 public void testUnescapeMethod() {
 String input = "This is a test &amp; with some &lt;entities&gt; and &quot;quotes&quot;.";
 String expectedOutput = "This is a test &amp; with some &lt;entities&gt; and &quot;quotes&quot;.";
 String output = Entities.unescape(input);

 Assert.assertEquals("unescape method with no entities should not change input", expectedOutput, output);
 }

 @Test
 public void testUnescapeMethodWithValidEntity() {
 String input = "This is a test &amp; with some &lt;entities&gt; and &quot;quotes&quot;.";
 String expectedOutput = "This is a test &amp; with some &lt;entities&gt; and &quot;quotes&quot;.";
 String output = Entities.unescape(input);

 String inputWithEntity = output.replace("&amp;", "&");
 String outputWithEntity = Entities.unescape(inputWithEntity);

 Assert.assertEquals("unescape method with valid entity should return original string", expectedOutput, output);
 Assert.assertEquals("unescape method with valid entity should replace '&amp;' with '&' and unescape other entities", input, outputWithEntity);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeMethodWithInvalidEntity() {
 String input = "This is a test &amp;with some &lt;entities&gt; and &quot;quotes&quot;.";
 String output = Entities.unescape(input);

 String inputWithEntity = output.replace("&amp;", "&x");
 String expectedOutput = "This is a test &amp;with some &lt;entities&gt; and &quot;quotes&quot;.";
 String outputWithEntity = Entities.unescape(inputWithEntity);

 Assert.assertEquals("unescape method with invalid entity should not change original string", expectedOutput, output);
 }

 @Test
 public void testUnescapeMethodWithUnescapedString() {
 String input = "This is a test &amp;with some &lt;entities&gt; and &quot;quotes&quot;.";
 String output = Entities.unescape(input);
 String expectedOutput = "This is a test &amp;with some &lt;entities&gt; and &quot;quotes&quot;.";

 Assert.assertEquals("unescape method with unescaped string should not change string", expectedOutput, output);
 }
}