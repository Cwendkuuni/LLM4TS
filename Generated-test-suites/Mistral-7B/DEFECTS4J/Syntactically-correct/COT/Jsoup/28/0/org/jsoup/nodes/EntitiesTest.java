import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

 private Map<Character, String> getMapFromEscapeMode(Entities.EscapeMode mode) {
 return mode.getMap();
 }

 @Test
 public void testIsNamedEntity() {
 Map<Character, String> map = getMapFromEscapeMode(Entities.EscapeMode.full);
 Assert.assertTrue(map.containsKey('lt'));
 }

 @Test
 public void testIsNamedEntityFalse() {
 Map<Character, String> map = getMapFromEscapeMode(Entities.EscapeMode.full);
 Assert.assertFalse(map.containsKey('A'));
 }

 @Test
 public void testGetCharacterByName_lt() {
 Character result = Entities.getCharacterByName("lt");
 Assert.assertEquals('<', result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetCharacterByName_invalidEntity() {
 Entities.getCharacterByName("invalidEntity");
 }

 @Test
 public void testEscape_stringWithCharactersFromMap() {
 String input = "&lt;&gt;&apos;&quot;&amp;";
 String expectedOutput = "&lt;&gt;&apos;&quot;&amp;";
 String result = Entities.escape(input, null, Entities.EscapeMode.base);
 Assert.assertEquals(expectedOutput, result);
 }

 @Test
 public void testEscape_stringWithCharactersNotInMap() {
 String input = "ABC";
 String expectedOutput = "ABC";
 String result = Entities.escape(input, null, Entities.EscapeMode.base);
 Assert.assertEquals(expectedOutput, result);
 }

 @Test
 public void testEscape_stringWithCharactersAndCharsetEncoder() {
 String input = "ABC";
 CharsetEncoder encoder = org.apache.commons.lang3.CharEncoding.UTF_8.newEncoder();
 String expectedOutput = "ABC";
 String result = Entities.escape(input, encoder, Entities.EscapeMode.base);
 Assert.assertEquals(expectedOutput, result);
 }

 @Test
 public void testUnescape() {
 String input = "&lt;&gt;&apos;&quot;&amp;";
 String expectedOutput = "<>'&";
 String result = Entities.unescape(input);
 Assert.assertEquals(expectedOutput, result);
 }

 @Test
 public void testUnescape_stringWithMissingEntity() {
 String input = "&invalidEntity;";
 String expectedOutput = "&invalidEntity;";
 String result = Entities.unescape(input);
 Assert.assertEquals(expectedOutput, result);
 }

 @Test
 public void testUnescape_stringWithInvalidEntity() {
 String input = "&&#123;";
 String expectedOutput = "&#123;";
 String result = Entities.unescape(input);
 Assert.assertEquals(expectedOutput, result);
 }

 @Test
 public void testUnescape_stringWithTrailingSemicolon() {
 String input = "&lt;&gt;&apos;&quot;&amp;&#123;;&#123;";
 String expectedOutput = "<>'&";
 String result = Entities.unescape(input);
 Assert.assertEquals(expectedOutput, result);
 }

 @Test
 public void testUnescape_stringWithTrailingSemicolonMissing() {
 String input = "&lt;&gt;&apos;&quot;&amp;&#123;";
 String expectedOutput = "<>'&";
 String result = Entities.unescape(input);
 Assert.assertEquals(expectedOutput, result);
 }
}