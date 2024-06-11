import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Entities entities = new Entities();
 // Test known named entities
 Assert.assertTrue(entities.isNamedEntity("lt"));
 Assert.assertTrue(entities.isNamedEntity("amp"));
 Assert.assertTrue(entities.isNamedEntity("apos"));
 Assert.assertTrue(entities.isNamedEntity("quot"));
 // Test unknown named entities
 Assert.assertFalse(entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 Entities entities = new Entities();
 // Test known named entities
 Assert.assertEquals('<', entities.getCharacterByName("lt"));
 Assert.assertEquals('&', entities.getCharacterByName("amp"));
 Assert.assertEquals("'", entities.getCharacterByName("apos"));
 Assert.assertEquals('"', entities.getCharacterByName("quot"));
 // Test unknown named entities
 Assert.assertNull(entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 Entities entities = new Entities();
 Document.OutputSettings out = new Document.OutputSettings();
 String input = "The < & > characters!";
 String expected = "The &lt; &amp; &gt; characters!";
 String output = entities.escape(input, out, Entities.Entities.extended);
 Assert.assertEquals(expected, output);
 }

 @Test
 public void testEscapeWithInvalidCharacter() {
 Entities entities = new Entities();
 Document.OutputSettings out = new Document.OutputSettings();
 String input = "The ¥ character!";
 String expected = "The &#165; character!";
 String output = entities.escape(input, out, Entities.Entities.extended);
 Assert.assertEquals(expected, output);
 }

 @Test
 public void testUnescape() {
 Entities entities = new Entities();
 String input = "The &lt;amp&gt; &#165; entities!";
 String expected = "The <amp>&#165; entities!";
 String output = entities.unescape(input);
 Assert.assertEquals(expected, output);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithInvalidString() {
 Entities entities = new Entities();
 String input = "The &lt;amp&gt;";
 String output = entities.unescape(input);
 }
}