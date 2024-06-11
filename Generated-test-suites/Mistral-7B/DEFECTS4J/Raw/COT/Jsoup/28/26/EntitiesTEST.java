import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;
import static org.jsoup.nodes.Entities.getCharacterByName;
import static org.jsoup.nodes.Entities.isNamedEntity;

public class EntitiesTest {

 private static final Map<String, Character> full = Entities.full;
 private static final Map<Character, String> xhtmlByVal = Entities.xhtmlByVal;
 private static final Map<Character, String> base = Entities.base;
 private static final Map<Character, String> baseByVal = Entities.baseByVal;
 private static final Map<Character, String> fullByVal = Entities.fullByVal;

 @Test
 public void testIsNamedEntity() {
 for (String namedEntity : full.keySet()) {
 assertTrue("Expected named entity '" + namedEntity + "' to be recognized", isNamedEntity(namedEntity));
 }
 }

 @Test(expected = AssertionError.class)
 public void testIsNamedEntityUnknownEntity() {
 assertFalse("Expected unknown entity to not be recognized", isNamedEntity("UnknownEntity"));
 }

 @Test
 public void testGetCharacterByName() {
 for (Map.Entry<String, Character> entry : full.entrySet()) {
 Character expected = entry.getValue();
 String name = entry.getKey();
 Character actual = getCharacterByName(name);
 assertEquals("Expected named entity '" + name + "' to have the character value of '" + expected + "'", expected, actual);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetCharacterByNameInvalidName() {
 try {
 getCharacterByName(null);
 fail("Expected IllegalArgumentException for null name");
 } catch (IllegalArgumentException e) {
 // expected
 }

 try {
 getCharacterByName("");
 fail("Expected IllegalArgumentException for empty name");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testEscape() {
 String string = "This <entity>&amp; that &lt;lt&gt; is &amp;amp; escaped.";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 String expected = "This &amp;amp;lt;entity&amp;gt;&amp;amp;lt;lt&amp;gt;&amp;amp;amp;amp;amp; escaped.";
 String actual = Entities.escape(string, outputSettings.encoder(), Entities.EscapeMode.extended);
 assertEquals("Expected escaped string to match '" + expected + "'", expected, actual);
 }

 @Test
 public void testUnescape() {
 String string = "This &amp;lt;entity&gt;&amp;lt;lt&amp;gt;&amp;amp;amp;amp;amp; escaped.";
 String expected = "This <entity>&lt;lt> is &amp;amp; escaped.";
 String actual = Entities.unescape(string);
 assertEquals("Expected unescaped string to match '" + expected + "'", expected, actual);
 }

 @Test
 public void testUnescapeWithTrailingSemicolon() {
 String string = "This &amp;#xA9;lt;entity&gt;&amp;#xA9;lt;lt&amp;gt;&amp;#xA9;amp;amp;amp;amp;amp; escaped;";
 String expected = "This ©<entity>&lt;lt> is ©&amp;amp; escaped.";
 String actual = Entities.unescape(string);
 assertEquals("Expected unescaped string to match '" + expected + "'", expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithInvalidCharacter() {
 try {
 Entities.unescape("This &amp;#x0000;");
 fail("Expected IllegalArgumentException for invalid character");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithUnrecognizedEntity() {
 try {
 Entities.unescape("This &foo;");
 fail("Expected IllegalArgumentException for unrecognized entity");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}