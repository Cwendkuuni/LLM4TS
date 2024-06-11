import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test known named entities
 assertEquals(true, Entities.isNamedEntity("lt"));
 assertEquals(true, Entities.isNamedEntity("amp"));

 // Test unknown named entities
 assertFalse(Entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test known named entities
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('&', Entities.getCharacterByName("amp"));

 // Test unknown named entities
 assertNull(Entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 // Test regular characters
 assertEquals("&lt;&amp;&gt;&quot;", Entities.escape("abc<def&ghi\"j", null, Entities.EscapeMode.extended));

 // Test characters from map
 assertEquals("&apos;", Entities.escape("apostrophe", null, Entities.EscapeMode.base));

 // Test characters that can be encoded
 assertEquals("hello\u00E9world", Entities.escape("helloworld", null, Entities.EscapeMode.xhtml));
 }

 @Test
 public void testUnescape() {
 // Test simple entities
 assertEquals("abc<def&ghi\"j", Entities.unescape("&lt;abc&amp;def&amp;&#x26;ghi&#x22;j;", false));

 // Test entities with trailing semicolon
 assertEquals("abc<def&ghi\"j", Entities.unescape("&lt;abc&amp;def&amp;&#x26;ghi\"j;", true));

 // Test invalid entities
 assertEquals("abc<def&ghi", Entities.unescape("&lt;abc&amp;def&amp;&#x26;ghi", false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEscapeMode() {
 Entities.escape("", null, new EscapeMode() {});
 }
}