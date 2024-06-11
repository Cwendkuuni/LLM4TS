import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

 // Test case for isNamedEntity method
 @Test
 public void testIsNamedEntity() {
 String namedEntity = "lt";
 Assert.assertTrue("Expected 'lt' to be a named entity", Entities.isNamedEntity(namedEntity));
 }

 // Test case for isBaseNamedEntity method
 @Test
 public void testIsBaseNamedEntity() {
 String baseNamedEntity = "amp";
 Assert.assertTrue("Expected 'amp' to be a base named entity", Entities.isBaseNamedEntity(baseNamedEntity));
 }

 // Test case for getCharacterByName method
 @Test
 public void testGetCharacterByName() {
 String namedEntity = "lt";
 Character expectedCharacter = '<';
 Character actualCharacter = Entities.getCharacterByName(namedEntity);
 Assert.assertEquals("Expected '<' as character for named entity 'lt'", expectedCharacter, actualCharacter);
 }

 // Test case for escape method with normal usage
 @Test
 public void testEscapeNormalUsage() {
 String inputString = "Hello & World!";
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("Hello &amp; World!");
 String actualOutput = Entities.escape(inputString, null);
 Assert.assertEquals("Expected 'Hello &amp; World!' as output for normal usage", expectedOutput.toString(), actualOutput);
 }

 // Test case for escape method with XHTML output
 @Test
 public void testEscapeXHTMLOutput() {
 String inputString = "&lt;p&gt;Hello &amp; World!&lt;/p&gt;";
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("&lt;p&gt;Hello &amp;amp; World!&lt;/p&gt;");
 String actualOutput = Entities.escape(inputString, Entities.EscapeMode.xhtml);
 Assert.assertEquals("Expected '&lt;p&gt;Hello &amp;amp; World!&lt;/p&gt;' as output for XHTML output", expectedOutput.toString(), actualOutput);
 }

 // Test case for escape method with invalid character
 @Test(expected = IllegalArgumentException.class)
 public void testEscapeInvalidCharacter() {
 String inputString = "\uD800"; // High surrogate character
 Entities.escape(inputString, null);
 }

 // Test case for unescape method with normal usage
 @Test
 public void testUnescapeNormalUsage() {
 String inputString = "Hello &amp; World!";
 String expectedOutput = "Hello & World!";
 String actualOutput = Entities.unescape(inputString);
 Assert.assertEquals("Expected 'Hello & World!' as output for normal usage", expectedOutput, actualOutput);
 }

 // Test case for unescape method with strict mode
 @Test
 public void testUnescapeStrictMode() {
 String inputString = "Hello & World!";
 String expectedOutput = "Hello &amp; World!";
 String actualOutput = Entities.unescape(inputString, true);
 Assert.assertEquals("Expected 'Hello &amp; World!' as output for strict mode", expectedOutput, actualOutput);
 }

 // Test case for canEncode method with ASCII character
 @Test
 public void testCanEncodeASCII() {
 CoreCharset charset = CoreCharset.ascii;
 char character = 'A';
 boolean expectedResult = true;
 boolean actualResult = Entities.canEncode(charset, character, null);
 Assert.assertEquals("Expected true for ASCII character 'A'", expectedResult, actualResult);
 }

 // Test case for canEncode method with non-ASCII character
 @Test
 public void testCanEncodeNonASCII() {
 CoreCharset charset = CoreCharset.utf;
 char character = '\u00E9'; // Unicode character é
 boolean expectedResult = true;
 boolean actualResult = Entities.canEncode(charset, character, null);
 Assert.assertEquals("Expected true for non-ASCII character é", expectedResult, actualResult);
 }

 // Test case for canEncode method with unsupported character set
 @Test
 public void testCanEncodeUnsupportedCharacterSet() {
 CoreCharset charset = new CoreCharset() {
 @Override public String name() { return "Unsupported"; }
 };
 char character = 'A';
 boolean expectedResult = false;
 boolean actualResult = Entities.canEncode(charset, character, null);
 Assert.assertEquals("Expected false for unsupported character set", expectedResult, actualResult);
 }
}