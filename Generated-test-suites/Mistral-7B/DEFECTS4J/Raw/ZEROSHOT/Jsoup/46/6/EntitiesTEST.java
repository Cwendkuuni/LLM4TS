import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();
 // Test for known named entities
 assertTrue(entities.isNamedEntity("lt"));
 assertTrue(entities.isNamedEntity("amp"));
 assertTrue(entities.isNamedEntity("quot"));
 // Test for unknown named entities
 assertFalse(entities.isNamedEntity("unknown"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 Entities entities = new Entities();
 // Test for known base named entities
 assertTrue(entities.isBaseNamedEntity("lt"));
 assertTrue(entities.isBaseNamedEntity("amp"));
 assertTrue(entities.isBaseNamedEntity("quot"));
 // Test for unknown named entities
 assertFalse(entities.isBaseNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 Entities entities = new Entities();
 // Test for known named entities
 assertEquals('<', entities.getCharacterByName("lt"));
 assertEquals('&', entities.getCharacterByName("amp"));
 assertEquals('"', entities.getCharacterByName("quot"));
 // Test for unknown named entities
 assertNull(entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 Entities entities = new Entities();
 String input = "&lt;hello&gt;world&quot;";
 StringBuilder expectedOutput = new StringBuilder();
 entities.escape(expectedOutput, input, null, false, false, false);
 // Test for default behavior
 assertEquals("<hello>", expectedOutput.toString());
 // Test for XHTML behavior
 expectedOutput.setLength(0);
 entities.escape(expectedOutput, input, null, true, false, false);
 assertEquals("<hello>", expectedOutput.toString());
 expectedOutput.setLength(0);
 entities.escape(expectedOutput, input, null, true, true, false);
 assertEquals("<hello>", expectedOutput.toString());
 expectedOutput.setLength(0);
 entities.escape(expectedOutput, input, null, true, false, true);
 assertEquals("<hello>", expectedOutput.toString());
 expectedOutput.setLength(0);
 entities.escape(expectedOutput, input, null, true, true, true);
 assertEquals("<hello>", expectedOutput.toString());
 // Test for extended behavior
 expectedOutput.setLength(0);
 entities.escape(expectedOutput, input, null, false, false, false);
 assertEquals("&lt;hello&gt;world&quot;", expectedOutput.toString());
 expectedOutput.setLength(0);
 entities.escape(expectedOutput, input, null, false, true, false);
 assertEquals("&lt;hello&gt;world&quot;", expectedOutput.toString());
 expectedOutput.setLength(0);
 entities.escape(expectedOutput, input, null, false, false, true);
 assertEquals("&amp;lt;hello&gt;world&amp;quot;", expectedOutput.toString());
 expectedOutput.setLength(0);
 entities.escape(expectedOutput, input, null, false, true, true);
 assertEquals("&amp;lt;hello&gt;world&amp;quot;", expectedOutput.toString());
 }

 @Test
 public void testUnescape() {
 Entities entities = new Entities();
 String input = "&lt;hello&gt;world&quot;";
 // Test for strict parsing
 assertEquals("<hello>world\"", entities.unescape(input, true));
 // Test for non-strict parsing
 assertEquals("<hello>world\"", entities.unescape(input, false));
 }
}