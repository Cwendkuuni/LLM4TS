import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private static final Map<String, Character> full = Entities.full;
 private static final Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;
 private static final Map<Character, String> baseByVal = Entities.baseByVal;

 /**
 * Test method for {@link Entities#isNamedEntity(String)}
 */
 @Test
 public void testIsNamedEntity() {
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();

 Assert.assertTrue("'"+ name + "' should be a named entity", Entities.isNamedEntity(name));
 Assert.assertEquals("Named entity '"+ name + "' should return the corresponding character '"+ character + "'", character, Entities.getCharacterByName(name));
 }
 }

 /**
 * Test method for {@link Entities#getCharacterByName(String)}
 */
 @Test
 public void testGetCharacterByName() {
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 String name = entry.getKey();
 Character character = entry.getValue();

 Assert.assertEquals("Named entity '"+ name + "' should return the corresponding character '"+ character + "'", character, Entities.getCharacterByName(name));
 }
 }

 /**
 * Test method for {@link Entities#escape(String, Document.OutputSettings)}
 */
 @Test
 public void testEscape() {
 String input = "This is a test string with &lt;entities&gt; and &amp;amp; in it.";
 Document.OutputSettings outputSettings = new Document.OutputSettings();

 String expected = "This is a test string with &amp;lt;entities&gt; and &amp;amp;amp; in it.";
 String result = Entities.escape(input, outputSettings);

 Assert.assertEquals("escape method should return the expected escaped string", expected, result);
 }

 /**
 * Test method for {@link Entities#escape(String, CharsetEncoder, Entities.EscapeMode)}
 */
 @Test
 public void testEscapeWithEscapeMode() {
 String input = "This is a test string with &lt;entities&gt; and &amp;amp; in it.";
 CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
 Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;

 String expected = "This is a test string with &amp;lt;entities&gt; and &amp;amp; in it.";
 String result = Entities.escape(input, encoder, escapeMode);

 Assert.assertEquals("escape method with EscapeMode should return the expected escaped string", expected, result);
 }

 /**
 * Test method for {@link Entities#unescape(String)}
 */
 @Test
 public void testUnescape() {
 String input = "This is a test string with &amp;lt;entities&gt; and &amp;amp; in it.";

 String expected = "This is a test string with <entities> and & in it.";
 String result = Entities.unescape(input);

 Assert.assertEquals("unescape method should return the expected unescaped string", expected, result);
 }

 /**
 * Test method for {@link Entities#isNamedEntity(String)} with invalid named entities
 */
 @Test(expected = IllegalArgumentException.class)
 public void testIsNamedEntityInvalid() {
 Entities.isNamedEntity("invalidEntity");
 }

 /**
 * Test method for {@link Entities#getCharacterByName(String)} with invalid named entities
 */
 @Test(expected = IllegalArgumentException.class)
 public void testGetCharacterByNameInvalid() {
 Entities.getCharacterByName("invalidEntity");
 }

 /**
 * Test method for {@link Entities#escape(String, Document.OutputSettings)} with null input
 */
 @Test(expected = NullPointerException.class)
 public void testEscapeNullInput() {
 Entities.escape(null, new Document.OutputSettings());
 }

 /**
 * Test method for {@link Entities#escape(String, CharsetEncoder, Entities.EscapeMode)} with null input
 */
 @Test(expected = NullPointerException.class)
 public void testEscapeNullInputWithEscapeMode() {
 Entities.escape(null, StandardCharsets.UTF_8.newEncoder(), Entities.EscapeMode.extended);
 }

 /**
 * Test method for {@link Entities#unescape(String)} with null input
 */
 @Test(expected = NullPointerException.class)
 public void testUnescapeNullInput() {
 Entities.unescape(null);
 }
}