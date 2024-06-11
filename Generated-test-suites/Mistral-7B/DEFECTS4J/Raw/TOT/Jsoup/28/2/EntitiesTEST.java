import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 Map<Character, String> fullMap = Entities.full;
 for (Map.entry<Character, String> entry : fullMap.entrySet()) {
 String name = Character.toString(entry.getKey());
 Character charValue = entry.getValue();

 Assert.assertTrue("Named entity '" + name + "' should be present in full map", Entities.isNamedEntity(name));
 Assert.assertEquals("Named entity '" + name + "' should map to character '" + charValue + "'", charValue, Entities.getCharacterByName(name));
 }

 Character unknownEntity = (char) 0x00;
 Assert.assertFalse("Unknown entity '" + unknownEntity + "' should not be present in full map", Entities.isNamedEntity(Character.toString(unknownEntity)));
 }

 @Test
 public void testEscapeAndUnescape() {
 String input = "&lt;hello&amp;world&gt; &quot; &apos; &amp; &amp;amp; &amp;#123; &amp;#x7f;";

 // test escaping
 Document.OutputSettings settings = new Document.OutputSettings();
 String escaped = Entities.escape(input, settings.encoder(), Entities.Entities.extended);
 Assert.assertEquals("Escaped string should match expected output: ", "&amp;lt;hello&amp;amp;amp;amp;amp;lt; &amp;quot; &amp;apos; &amp;amp; &amp;amp;amp;amp;amp;#123; &amp;#x7f;", escaped);

 // test unescaping
 String unescaped = Entities.unescape(escaped);
 Assert.assertEquals("Unescaped string should match original input: ", input, unescaped);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidEscapeMode() {
 Entities.escape("input", null, null);
 }

 @Test
 public void testInvalidEncoder() {
 CharsetEncoder encoder = null;
 Document.OutputSettings settings = new Document.OutputSettings();
 settings.encoder = encoder;

 String input = "invalid";
 String escaped = Entities.escape(input, settings.encoder, Entities.Entities.extended);
 Assert.assertNull("Output should be null if encoder is invalid", escaped);
 }
}