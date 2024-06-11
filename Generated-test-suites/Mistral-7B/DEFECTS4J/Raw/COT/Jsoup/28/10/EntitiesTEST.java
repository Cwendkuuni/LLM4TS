import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private static final Map<String, Character> full = Entities.full;
 private static final Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;
 private static final Map<Character, String> baseByVal = Entities.baseByVal;

 @Test
 public void testIsNamedEntity() {
 // Testing isNamedEntity method with valid named entities
 Assert.assertTrue("lt is a named entity", Entities.isNamedEntity("lt"));
 Assert.assertTrue("apos is a named entity", Entities.isNamedEntity("apos"));
 Assert.assertTrue("quot is a named entity", Entities.isNamedEntity("quot"));
 Assert.assertTrue("amp is a named entity", Entities.isNamedEntity("amp"));

 // Testing isNamedEntity method with invalid named entities
 Assert.assertFalse("invalidChar is not a named entity", Entities.isNamedEntity("invalidChar"));
 }

 @Test
 public void testGetCharacterByName() {
 // Testing getCharacterByName method with valid named entities
 Character lt = Entities.getCharacterByName("lt");
 Character quot = Entities.getCharacterByName("quot");
 Character apos = Entities.getCharacterByName("apos");
 Character amp = Entities.getCharacterByName("amp");
 Assert.assertEquals("lt character should be <", lt, '<');
 Assert.assertEquals("quot character should be \"", quot, '\"');
 Assert.assertEquals("apos character should be '", apos, '\'');
 Assert.assertEquals("amp character should be &", amp, '&');

 // Testing getCharacterByName method with invalid named entities
 Assert.assertNull("invalidChar should return null", Entities.getCharacterByName("invalidChar"));
 }

 @Test
 public void testEscapeMethod() {
 // Testing escape method with valid strings
 String validString = "This is a test string with &lt;lt&gt; entity and &amp;amp; ampersand.";
 String expectedEscapedString = "This is a test string with &amp;lt;&#034;lt;&#034;gt; entity and &amp;amp; ampersand.";
 String escapedString = Entities.escape(validString, null, Entities.EscapeMode.extended);
 Assert.assertEquals("escapedString should match expectedEscapedString", expectedEscapedString, escapedString);

 // Testing escape method with empty string
 String emptyString = "";
 String escapedEmptyString = Entities.escape(emptyString, null, Entities.EscapeMode.extended);
 Assert.assertEquals("escapedEmptyString should be an empty string", "", escapedEmptyString);

 // Testing escape method with invalid chars
 String invalidString = "This is a test string with invalidChars.";
 String expectedInvalidEscapedString = "This is a test string with invalidChars.";
 String invalidEscapedString = Entities.escape(invalidString, null, Entities.EscapeMode.extended);
 Assert.assertEquals("escapedString should match expectedInvalidEscapedString", expectedInvalidEscapedString, invalidEscapedString);
 }

 @Test
 public void testUnescapeMethod() {
 // Testing unescape method with valid strings
 String escapedString = "This is a test string with &amp;lt;&#034;lt;&#034;gt; entity and &amp;amp; ampersand.";
 String expectedUnescapedString = "This is a test string with <lt>lt></gt> entity and &amp; ampersand.";
 String unescapedString = Entities.unescape(escapedString);
 Assert.assertEquals("unescapedString should match expectedUnescapedString", expectedUnescapedString, unescapedString);

 // Testing unescape method with empty string
 String escapedEmptyString = "";
 String unescapedEmptyString = Entities.unescape(escapedEmptyString);
 Assert.assertEquals("unescapedEmptyString should be an empty string", "", unescapedEmptyString);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeWithNullString() {
 Entities.escape(null, null, Entities.EscapeMode.extended);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullString() {
 Entities.unescape(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullPattern() {
 Entities.unescape(null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeWithInvalidEscapeMode() {
 Entities.escape("test", null, "invalidEscapeMode");
 }

 @Test
 public void testLoadEntities() {
 Map<String, Character> loadedEntities = Entities.loadEntities("entities-base.properties");
 Character loadedEntity = loadedEntities.get("quot");
 Assert.assertEquals("loadedEntity should be \"", loadedEntity, '\'');
 }

 @Test(expected = MissingResourceException.class)
 public void testLoadEntitiesWithMissingFile() {
 Entities.loadEntities("missingFile.properties");
 }
}