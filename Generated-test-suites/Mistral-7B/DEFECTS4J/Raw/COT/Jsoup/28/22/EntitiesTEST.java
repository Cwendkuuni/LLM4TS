import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

 private static final Character[] NAMED_ENTITIES = {
 'lt', 'gt', 'amp', 'apos', 'quot'
 };

 @Test
 public void testIsNamedEntity() {
 for (Character namedEntity : NAMED_ENTITIES) {
 Assert.assertTrue("'isNamedEntity' should return true for known named entities: " + namedEntity, Entities.isNamedEntity(Entities.getCharacterByName(namedEntity.toString()).toString()));
 }
 }

 @Test(expected = AssertionError.class)
 public void testIsNamedEntityUnknownEntity() {
 Assert.assertFalse("'isNamedEntity' should return false for unknown entities", Entities.isNamedEntity("unknownEntity"));
 }

 @Test
 public void testGetCharacterByName() {
 for (Character namedEntity : NAMED_ENTITIES) {
 Character character = Entities.getCharacterByName(namedEntity.toString());
 Assert.assertNotNull("'getCharacterByName' should return a character for known named entities: " + namedEntity, character);
 Assert.assertEquals("'getCharacterByName' should return the correct character for known named entities: " + namedEntity, namedEntity, character);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetCharacterByNameNull() {
 Assert.assertNull("'getCharacterByName' should return null for null input", Entities.getCharacterByName(null));
 }

 @Test
 public void testEscapeNullString() {
 String escapedString = Entities.escape(null, null, Entities.EscapeMode.base);
 Assert.assertNull("'escape' should return null for null input", escapedString);
 }

 @Test
 public void testEscapeEmptyString() {
 String escapedString = Entities.escape("", null, Entities.EscapeMode.base);
 Assert.assertEquals("'escape' should return an empty string for an empty input", "", escapedString);
 }

 @Test
 public void testEscapeStringWithMappedCharacters() {
 String input = "&lt;&gt;&amp;&apos;&quot;";
 String expectedOutput = "&amp;lt;>&amp;gt;>&amp;amp;>&amp;apos;>&amp;quot;";
 String output = Entities.escape(input, null, Entities.EscapeMode.base);
 Assert.assertEquals("'escape' should return the correct output for a string with mapped characters: " + input, expectedOutput, output);
 }

 @Test
 public void testEscapeStringWithNonMappedCharacters() {
 String input = "abcdefghijk";
 String expectedOutput = "abcdefghijk";
 String output = Entities.escape(input, null, Entities.EscapeMode.base);
 Assert.assertEquals("'escape' should return the original string for a string without mapped characters: " + input, expectedOutput, output);
 }

 @Test(expected = IOException.class)
 public void testLoadEntitiesThrowsIOException() {
 Entities.loadEntities(null);
 }

 @Test
 public void testLoadEntities() {
 Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
 Assert.assertNotNull("'loadEntities' should return a non-null map", entities);
 Assert.assertEquals("'loadEntities' should return the correct map size", 32, entities.size());
 }

 @Test(expected = MissingResourceException.class)
 public void testLoadEntitiesMissingResource() {
 Entities.loadEntities("missingResource.properties");
 }
}