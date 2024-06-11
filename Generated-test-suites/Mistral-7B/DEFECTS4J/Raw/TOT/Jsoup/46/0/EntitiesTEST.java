import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test isNamedEntity method with valid named entities
 assertEquals(true, Entities.isNamedEntity("lt"));
 assertEquals(true, Entities.isNamedEntity("amp"));
 assertEquals(true, Entities.isNamedEntity("quot"));

 // Test isNamedEntity method with invalid named entities
 assertEquals(false, Entities.isNamedEntity("invalidEntity"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Test isBaseNamedEntity method with valid named entities in base set
 assertEquals(true, Entities.isBaseNamedEntity("amp"));
 assertEquals(true, Entities.isBaseNamedEntity("quot"));

 // Test isBaseNamedEntity method with invalid named entities
 assertEquals(false, Entities.isBaseNamedEntity("lt"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test getCharacterByName method with valid named entities
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals('"', Entities.getCharacterByName("quot"));

 // Test getCharacterByName method with invalid named entities
 assertNull(Entities.getCharacterByName("invalidEntity"));
 }

 @Test
 public void testEscape() {
 // Test escape method with valid characters
 StringBuilder accum = new StringBuilder();
 Entities.escape(accum, "Hello World", Entities.Entities.Document.OutputSettings.base(), false, false, false);
 assertEquals("Hello World", accum.toString());

 accum.setLength(0);
 Entities.escape(accum, "<h1>Hello World</h1>", Entities.Entities.Document.OutputSettings.base(), true, false, false);
 assertEquals("&lt;h1&gt;Hello World&lt;/h1&gt;", accum.toString());

 // Test escape method with invalid characters
 accum.setLength(0);
 Entities.escape(accum, "Hello & World", Entities.Entities.Document.OutputSettings.base(), false, false, false);
 assertEquals("Hello &amp; World", accum.toString());
 }

 @Test
 public void testUnescape() {
 // Test unescape method with valid HTML entities
 assertEquals("Hello & World", Entities.unescape("Hello &amp; World", false));

 // Test unescape method with invalid HTML entities
 assertEquals("Hello &amp; World", Entities.unescape("Hello &#x0003B; World", false));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithInvalidArguments() {
 // Test unescape method with invalid arguments
 Entities.unescape(null, "Hello World");
 }
}