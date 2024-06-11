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
 private static final Pattern unescapePattern = Entities.unescapePattern;
 private static final Pattern strictUnescapePattern = Entities.strictUnescapePattern;

 @Test
 public void testIsNamedEntity() {
 // Test basic functionality
 Assert.assertTrue(Entities.isNamedEntity("lt"));
 Assert.assertFalse(Entities.isNamedEntity("rt")); // edge case: non-existent entity

 // Test edge cases and exceptions
 Assert.assertEquals('&', Entities.getCharacterByName("amp")); // valid entity
 Assert.assertNull(Entities.getCharacterByName("rt")); // non-existent entity
 Assert.assertNull(Entities.getCharacterByName("")); // empty name
 }

 @Test
 public void testGetCharacterByName() {
 // Test basic functionality
 Assert.assertEquals('<', Entities.getCharacterByName("lt"));

 // Test edge cases and exceptions
 Assert.assertNull(Entities.getCharacterByName("rt")); // non-existent entity
 Assert.assertNull(Entities.getCharacterByName("")); // empty name
 }

 @Test
 public void testEscape() {
 // Test basic functionality
 String string = "This is a test";
 String expected = "This is a test";
 String escaped = Entities.escape(string, null, Entities.EscapeMode.base);
 Assert.assertEquals(expected, escaped);

 // Test edge cases and exceptions
 string = "&amp;";
 expected = "&amp;amp;";
 escaped = Entities.escape(string, null, Entities.EscapeMode.base);
 Assert.assertEquals(expected, escaped);

 string = "&x26;";
 expected = "&amp;#x26;";
 escaped = Entities.escape(string, null, Entities.EscapeMode.base);
 Assert.assertEquals(expected, escaped);

 string = "&x{123};"; // invalid hex value
 expected = string;
 escaped = Entities.escape(string, null, Entities.EscapeMode.base);
 Assert.assertEquals(expected, escaped);
 }

 @Test
 public void testUnescape() {
 // Test basic functionality
 String string = "This is &amp; a test";
 String expected = "This is a test";
 String unescaped = Entities.unescape(string);
 Assert.assertEquals(expected, unescaped);

 // Test edge cases and exceptions
 string = "This is &#x26; a test";
 expected = "This is &amp; a test";
 unescaped = Entities.unescape(string);
 Assert.assertEquals(expected, unescaped);

 string = "This is &lt; a test";
 expected = "This is < a test";
 unescaped = Entities.unescape(string);
 Assert.assertEquals(expected, unescaped);

 string = "This is &amp;amp; a test";
 expected = "This is &amp; a test"; // repeating escape sequence should be treated as a single entity
 unescaped = Entities.unescape(string);
 Assert.assertEquals(expected, unescaped);

 string = "This is &lt; &gt; a test"; // overlapping entity sequences
 expected = "This is < > a test";
 unescaped = Entities.unescape(string);
 Assert.assertEquals(expected, unescaped);

 string = "This is &amp;<script>alert('XSS');</script> a test"; // XSS attack
 expected = "This is &amp;< script>alert('XSS');</script> a test"; // should not execute script
 unescaped = Entities.unescape(string);
 Assert.assertEquals(expected, unescaped);
 }

 // Add more test cases for other methods in Entities class as required

}