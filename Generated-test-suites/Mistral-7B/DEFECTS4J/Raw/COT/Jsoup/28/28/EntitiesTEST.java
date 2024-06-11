import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private final static Map<String, Character> full = new HashMap<String, Character>() {{
 put("lt", '<');
 put("gt", '>');
 // Add other entities here
 }};

 private final static Map<Character, String> xhtmlByVal = new HashMap<Character, String>() {{
 put('<', "lt");
 put('>', "gt");
 // Add other entities here
 }};

 private final static Map<String, Character> base = new HashMap<String, Character>() {{
 put("lt", '<');
 put("gt", '>');
 // Add other entities here
 }};

 private final static Map<Character, String> baseByVal = new HashMap<Character, String>() {{
 put('<', "lt");
 put('>', "gt");
 // Add other entities here
 }};

 private final static Map<Character, String> fullByVal = new HashMap<Character, String>() {{
 put('<', "lt");
 put('>', "gt");
 // Add other entities here
 }};

 @Test
 public void testIsNamedEntity() {
 // Test for known named entities
 Assert.assertTrue(Entities.isNamedEntity("lt"));
 Assert.assertTrue(Entities.isNamedEntity("gt"));
 // Test for unknown named entities
 Assert.assertFalse(Entities.isNamedEntity("unknownEntity"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test for known named entities
 Assert.assertEquals('<', Entities.getCharacterByName("lt"));
 Assert.assertEquals('>', Entities.getCharacterByName("gt"));
 // Test for unknown named entities
 Assert.assertNull(Entities.getCharacterByName("unknownEntity"));
 }

 @Test
 public void testEscape() {
 String string = "&lt;hello&amp;world&gt;";
 Character outputEscapeMode = Entities.EscapeMode.xhtml.charAtValue();
 String expected = "&lt;lt&gt;hello&amp;amp;world&gt;";
 String result = Entities.escape(string, null, outputEscapeMode);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeWithNullString() {
 Entities.escape(null, null, Entities.EscapeMode.xhtml);
 }

 @Test
 public void testEscapeWithEmptyString() {
 String result = Entities.escape("", null, Entities.EscapeMode.xhtml);
 Assert.assertEquals("", result);
 }

 @Test
 public void testUnescape() {
 String string = "&lt;lt&gt;hello&amp;amp;world&gt;";
 String expected = "<lt>hello&amp;world>";
 String result = Entities.unescape(string);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullString() {
 Entities.unescape(null);
 }

 @Test
 public void testUnescapeWithEmptyString() {
 String result = Entities.unescape("");
 Assert.assertEquals("", result);
 }

 @Test
 public void testUnescapeWithInvalidString() {
 String string = "&unkwonEntity;";
 String result = Entities.unescape(string);
 Assert.assertEquals(string, result);
 }

 @Test
 public void testIsNamedEntityWithXHTMLMode() {
 // Test for known named entities in XHTML mode
 Assert.assertTrue(Entities.isNamedEntity("lt", Entities.EscapeMode.xhtml));
 Assert.assertTrue(Entities.isNamedEntity("gt", Entities.EscapeMode.xhtml));
 // Test for unknown named entities in XHTML mode
 Assert.assertFalse(Entities.isNamedEntity("unknownEntity", Entities.EscapeMode.xhtml));
 }

 @Test
 public void testGetCharacterByNameWithXHTMLMode() {
 // Test for known named entities in XHTML mode
 Assert.assertEquals('<', Entities.getCharacterByName("lt", Entities.EscapeMode.xhtml));
 Assert.assertEquals('>', Entities.getCharacterByName("gt", Entities.EscapeMode.xhtml));
 // Test for unknown named entities in XHTML mode
 Assert.assertNull(Entities.getCharacterByName("unknownEntity", Entities.EscapeMode.xhtml));
 }

 @Test
 public void testEscapeWithXHTMLMode() {
 String string = "&lt;hello&amp;world&gt;";
 Character outputEscapeMode = Entities.EscapeMode.xhtml.charAtValue();
 String expected = "&lt;lt&gt;hello&amp;amp;world&gt;";
 String result = Entities.escape(string, null, outputEscapeMode);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeWithNullStringAndXHTMLMode() {
 Entities.escape(null, null, Entities.EscapeMode.xhtml);
 }

 @Test
 public void testEscapeWithEmptyStringAndXHTMLMode() {
 String result = Entities.escape("", null, Entities.EscapeMode.xhtml);
 Assert.assertEquals("", result);
 }

 @Test
 public void testUnescapeWithXHTMLMode() {
 String string = "&lt;lt&gt;hello&amp;amp;world&gt;";
 String expected = "<lt>hello&amp;world>";
 String result = Entities.unescape(string, true); // strict unescape
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullStringAndXHTMLMode() {
 Entities.unescape(null, true);
 }

 @Test
 public void testUnescapeWithEmptyStringAndXHTMLMode() {
 String result = Entities.unescape("", true);
 Assert.assertEquals("", result);
 }

 @Test
 public void testLoadEntities() {
 // Test for loading entities from properties file
 Map<String, Character> loadedEntities = Entities.loadEntities("entities-base.properties");
 // Add assertions for loaded entities here
 }

}