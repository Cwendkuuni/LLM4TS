import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();
 // Test known named entities
 assertTrue(entities.isNamedEntity("lt"));
 assertTrue(entities.isNamedEntity("gt"));
 assertTrue(entities.isNamedEntity("amp"));
 assertTrue(entities.isNamedEntity("quot"));
 assertTrue(entities.isNamedEntity("apos"));
 // Test unknown named entities
 assertFalse(entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 Entities entities = new Entities();
 // Test known named entities
 assertEquals('<', entities.getCharacterByName("lt"));
 assertEquals('&', entities.getCharacterByName("amp"));
 assertEquals('\'', entities.getCharacterByName("apos"));
 assertEquals('"', entities.getCharacterByName("quot"));
 // Test unknown named entities
 assertNull(entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 Entities entities = new Entities();
 String input = "&lt;foo&gt;<bar>&amp;baz&quot;" +
 "&apos;&#123;&#125;&#127;&#160;Hello, &world!";
 Document.OutputSettings out = new Document.OutputSettings();
 String expected = "&lt;amp;lt;&#x003C;foo&gt;&#x003E;<amp;bar>&amp;amp;baz&amp;quot;" +
 "&amp;apos;&#x007B;&#x007D;&#x007F;&#x00A0;Hello, &amp;amp;world!";
 String output = entities.escape(input, out);
 assertEquals(expected, output);
 }

 @Test
 public void testUnescape() {
 Entities entities = new Entities();
 String input = "&lt;amp;lt;&#x003C;foo&gt;&#x003E;<amp;bar>&amp;amp;baz&amp;quot;" +
 "&amp;apos;&#x007B;&#x007D;&#x007F;&#x00A0;Hello, &amp;amp;world!";
 String output = entities.unescape(input);
 String expected = "&lt;foo&gt;&lt;&gt;<bar>&amp;baz&quot;'[]{}\\xDE Hello, world!";
 assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullString() {
 Entities entities = new Entities();
 entities.unescape(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithEmptyString() {
 Entities entities = new Entities();
 entities.unescape("");
 }
}