import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test for known named entities
 assertEquals(true, Entities.isNamedEntity("lt"));
 // Test for unknown named entities
 assertEquals(false, Entities.isNamedEntity("unknowntag"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Test for known base named entities
 assertEquals(true, Entities.isBaseNamedEntity("lt"));
 // Test for unknown base named entities
 assertEquals(false, Entities.isBaseNamedEntity("unknowntag"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test for known named entities
 assertEquals(60, Entities.getCharacterByName("lt").charValue());
 // Test for unknown named entities
 assertNull(Entities.getCharacterByName("unknowntag"));
 }

 @Test
 public void testEscapeMethod() {
 String input = "&<>'\"test &lt; &gt; &amp; &quot;";
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("&amp;amp;amp;lt;amp;gt;amp;amp;quot;");
 String output = Entities.escape(input, Document.OutputSettings.base());
 assertEquals(expectedOutput.toString(), output);
 }

 @Test
 public void testEscapeMethodWithXhtmlOutput() {
 String input = "&<>'\"test &lt; &gt; &amp; &quot;";
 StringBuilder expectedOutput = new StringBuilder();
 expectedOutput.append("&amp;lt; &amp;gt; &amp;amp; &amp;quot;");
 String output = Entities.escape(input, Document.OutputSettings.xhtml());
 assertEquals(expectedOutput.toString(), output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeMethodWithNullInput() {
 Entities.escape(null, Document.OutputSettings.base());
 }

 @Test
 public void testUnescapeMethod() {
 String input = "&amp;amp;lt; &amp;gt; &amp;amp;quot;";
 String expectedOutput = "&lt; &gt; &quot;";
 String output = Entities.unescape(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testCanEncodeMethod() {
 // Test for ASCII character
 assertEquals(true, Entities.canEncode(Entities.CoreCharset.ascii, 'a'));
 // Test for non-ASCII character
 assertEquals(false, Entities.canEncode(Entities.CoreCharset.utf, '\u00E9'));
 }
}