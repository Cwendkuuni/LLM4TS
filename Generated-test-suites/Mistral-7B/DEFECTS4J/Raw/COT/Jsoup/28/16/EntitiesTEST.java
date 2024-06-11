import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

 private static final Entities entities = new Entities();

 @Test
 public void testIsNamedEntity() {
 // Test for known named entities in the full entity set
 Assert.assertTrue(entities.isNamedEntity("lt"));
 Assert.assertTrue(entities.isNamedEntity("amp"));
 Assert.assertTrue(entities.isNamedEntity("quot"));
 Assert.assertTrue(entities.isNamedEntity("apos"));
 Assert.assertTrue(entities.isNamedEntity("gt"));

 // Test for unknown named entities
 Assert.assertFalse(entities.isNamedEntity("unknown"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test for known named entities in the full entity set
 Assert.assertEquals('<', entities.getCharacterByName("lt"));
 Assert.assertEquals('&', entities.getCharacterByName("amp"));
 Assert.assertEquals('\"', entities.getCharacterByName("quot"));
 Assert.assertEquals("'", entities.getCharacterByName("apos"));
 Assert.assertEquals('>', entities.getCharacterByName("gt"));

 // Test for unknown named entities
 Assert.assertNull(entities.getCharacterByName("unknown"));
 }

 @Test
 public void testEscape() {
 // Test for basic functionality
 String string = "&lt;hello&amp; world&gt;";
 String expected = "&lt;hello&amp;amp; world&gt;";
 Assert.assertEquals(expected, Entities.escape(string, EntityEncoder.UTF_8, Entities.Entities.extended));

 // Test for characters that have a corresponding entity in the base set
 string = "&amp;lt;hello&amp; world&gt;";
 expected = "&amp;amp;lt;hello&amp;amp;amp; world&gt;";
 Assert.assertEquals(expected, Entities.escape(string, EntityEncoder.UTF_8, Entities.Entities.base));

 // Test for characters that have a corresponding entity in the xhtml set
 string = "&lt;lt&gt;hello&apos; world&gt;";
 expected = "&amp;lt;lt&gt;lt&amp;gt;hello&apos;amp;apos; world&gt;";
 Assert.assertEquals(expected, Entities.escape(string, EntityEncoder.UTF_8, Entities.Entities.xhtml));

 // Test for characters that are not in any entity set
 string = "&xyz;hello world&";
 expected = "&#252;hello world&";
 Assert.assertEquals(expected, Entities.escape(string, EntityEncoder.UTF_8, Entities.Entities.extended));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullString() {
 Entities.escape(null, EntityEncoder.UTF_8, Entities.Entities.extended);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEncoder() {
 Entities.escape(new String("test"), null, Entities.Entities.extended);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullEscapeMode() {
 Entities.escape(new String("test"), EntityEncoder.UTF_8, null);
 }

 @Test
 public void testUnescape() {
 // Test for basic functionality
 String string = "&amp;lt;hello&amp;amp; world&gt;";
 String expected = "&lt;hello world&gt;";
 Assert.assertEquals(expected, Entities.unescape(string));

 // Test for entities in the base set
 string = "&amp;amp;lt;hello&amp;amp;amp; world&gt;";
 expected = "&lt;hello world&gt;";
 Assert.assertEquals(expected, Entities.unescape(string));

 // Test for entities in the xhtml set
 string = "&amp;lt;lt&gt;lt&amp;amp;apos; world&gt;";
 expected = "&lt;lt&gt;lt&apos; world&gt;";
 Assert.assertEquals(expected, Entities.unescape(string));

 // Test for characters that are not entities
 string = "&#252;hello world&";
 expected = "&amp;#252;hello world&";
 Assert.assertEquals(expected, Entities.unescape(string));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullStringForUnescape() {
 Entities.unescape(null);
 }

 @Test
 public void testToString() {
 Entities entities = new Entities();
 Assert.assertNotNull(entities.toString());
 }
}