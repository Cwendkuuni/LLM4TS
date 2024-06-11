import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Testing for known named entities
 assertTrue(isNamedEntity("lt"));
 assertTrue(isNamedEntity("gt"));
 assertTrue(isNamedEntity("amp"));
 assertTrue(isNamedEntity("apos"));
 assertTrue(isNamedEntity("quot"));

 // Testing for unknown named entities
 assertFalse(isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 // Testing for known named entities
 assertEquals('&', getCharacterByName("amp"));
 assertEquals('<', getCharacterByName("lt"));
 assertEquals('>', getCharacterByName("gt"));
 assertEquals("'", getCharacterByName("apos"));
 assertEquals("\"", getCharacterByName("quot"));

 // Testing for unknown named entities
 assertNull(getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 String input = "This is a test & with some &amp; entities &amp; <lt>html&gt; tags &amp; an &apos;apos&apos; character &amp; &#123; &amp; &amp;&amp; and &nbsp;.";
 String expected = "This is a test &amp; &amp;amp; &amp;lt;html&amp;gt; &amp; #&#x26;&#x61;&#x6D;&#x65;&#x26;&gt; &amp; &apos;&apos; &amp; &#123; &amp; &amp;&amp; &amp; #x200B;";
 String output = escape(input, Document.OutputSettings.base());
 assertEquals(expected, output);
 }

 @Test
 public void testUnescape() {
 String input = "This is a test &amp; &amp;amp; &lt;html&gt; &apos;&apos; &amp; #&#x26;&#x61;&#x6D;&#x65;&#x26;&gt; &amp; &amp;&amp; &nbsp;";
 String expected = "This is a test & & <html> ' ' < &amp; and ";
 String output = unescape(input);
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullInput() {
 unescape(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithEmptyInput() {
 unescape("");
 }

 @Test
 public void testUnescapeWithInvalidEntity() {
 String input = "This is a test &xyz;";
 String expected = "This is a test &xyz;";
 String output = unescape(input);
 assertEquals(expected, output);
 }

 // Add more test cases as needed

}