import org.junit.Test;
import org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Map<String, Character> full = Entities.full;

 // Test known named entities
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("gt"));
 assertTrue(Entities.isNamedEntity("amp"));
 assertTrue(Entities.isNamedEntity("apos"));
 assertTrue(Entities.isNamedEntity("quot"));

 // Test unknown named entities
 assertFalse(Entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 Map<String, Character> full = Entities.full;

 // Test known named entities
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('>', Entities.getCharacterByName("gt"));
 assertEquals("'", Entities.getCharacterByName("apos"));
 assertEquals('"', Entities.getCharacterByName("quot"));

 // Test unknown named entities
 assertNull(Entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 Map<Character, String> xhtmlByVal = Entities.EscapeMode.xhtml.getMap();
 CharsetEncoder encoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();

 String input = "This is a test <> with & entities.";
 String expectedOutput = "This is a test &lt;&gt; with &amp; entities.";

 String output = Entities.escape(input, encoder, Entities.EscapeMode.xhtml);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testUnescape() {
 String input = "This is a test &lt;&gt; with &amp; entities.";
 String expectedOutput = "This is a test <> with & entities.";

 String output = Entities.unescape(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testXhtmlByVal() {
 Map<Character, String> xhtmlByVal = Entities.EscapeMode.xhtml.getMap();

 // Test known entities
 assertEquals("quot", xhtmlByVal.get('\u00022'));
 assertEquals("amp", xhtmlByVal.get('\u00026'));
 assertEquals("apos", xhtmlByVal.get('\u00027'));
 assertEquals("lt", xhtmlByVal.get('\u0003C'));
 assertEquals("gt", xhtmlByVal.get('\u0003E'));

 // Test unknown characters
 assertNull(xhtmlByVal.get('?'));
 }

 @Test
 public void testLoadEntities() {
 Map<String, Character> entities = Entities.loadEntities("entities-full.properties");

 // Test known entities
 Character lt = entities.get("lt");
 Character gt = entities.get("gt");
 Character amp = entities.get("amp");
 Character apos = entities.get("apos");
 Character quot = entities.get("quot");

 assertEquals('<', lt.charValue());
 assertEquals('>', gt.charValue());
 assertEquals('&', amp.charValue());
 assertEquals("'", apos.charValue());
 assertEquals('"', quot.charValue());
 }
}