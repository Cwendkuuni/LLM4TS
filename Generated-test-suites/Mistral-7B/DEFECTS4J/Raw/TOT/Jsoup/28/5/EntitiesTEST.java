import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test known named entities in the base set
 Assert.assertTrue(Entities.isNamedEntity("amp"));
 Assert.assertTrue(Entities.isNamedEntity("apos"));
 Assert.assertTrue(Entities.isNamedEntity("gt"));
 Assert.assertTrue(Entities.isNamedEntity("lt"));
 Assert.assertTrue(Entities.isNamedEntity("quot"));

 // Test unknown named entities
 Assert.assertFalse(Entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test known named entities in the base set
 Assert.assertEquals('&', Entities.getCharacterByName("amp"));
 Assert.assertEquals('\'', Entities.getCharacterByName("apos"));
 Assert.assertEquals('<', Entities.getCharacterByName("lt"));
 Assert.assertEquals('>', Entities.getCharacterByName("gt"));
 Assert.assertEquals('"', Entities.getCharacterByName("quot"));

 // Test unknown named entities
 Assert.assertNull(Entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 String input = "&amp; <&lt; &gt; &apos; &quot;";
 String expected = "&amp;amp; &lt; &gt; &apos; &quot;";
 String output = Entities.escape(input, null, Entities.Entities.extended);
 Assert.assertEquals(expected, output);
 }

 @Test
 public void testUnescape() {
 String input = "&amp;amp; &lt; &gt; &apos; &quot;";
 String expected = "&amp; <&lt; &gt; &apos; &quot;";
 String output = Entities.unescape(input);
 Assert.assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnsupportedCharacterEncodingException() {
 String input = "\uD800";
 CharsetEncoder encoder = Charset.forName("UTF-16BE").newEncoder();
 String output = Entities.escape(input, encoder, Entities.Entities.extended);
 }
}