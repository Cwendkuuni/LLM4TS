import org.junit.Test;
import org.junit.Assert.*;
import static org.jsoup.nodes.Entities.*;

public class EntitiesTest {

 private static final Map<Character, Character> expectedXhtmlMap = new HashMap<Character, Character>() {{
 put('&', "amp");
 put('<', "lt");
 put('>', "gt");
 put('"', "quot");
 put("'", "apos");
 }};

 private static final Map<Character, Character> expectedBaseMap = new HashMap<Character, Character>() {{
 put('&', "amp");
 put('<', "lt");
 put('>', "gt");
 put('"', "quot");
 put("'", "apos");
 put('_', "sub");
 put("^", "sup");
 put("~", "tilde");
 put("Nbsp", '\u00A0');
 put("nbsp", '\u00A0');
 put("iexcl", '\u00A1');
 // Add more mappings as needed
 }};

 private static final Map<Character, Character> expectedFullMap = new HashMap<Character, Character>() {{
 // Add all entity mappings from the class here
 putAll(baseMap);
 put('Agrave', '\u00C0');
 put('Aacute', '\u00C1');
 put('Acirc', '\u00C2');
 // Add more mappings as needed
 }};

 @Test
 public void testIsNamedEntity() {
 for (Character entity : full.keySet()) {
 assertTrue(isNamedEntity(entity.toString()));
 }
 }

 @Test
 public void testGetCharacterByName() {
 for (Character entity : full.keySet()) {
 Character character = getCharacterByName(entity.toString());
 assertEquals(entity, character);
 }
 }

 @Test
 public void testEscapeWithXhtmlMode() {
 String original = "Hello & World! <greet name=\"John\"/>";
 Map<Character, String> map = EscapeMode.xhtml.getMap();
 String escaped = escape(original, null, EscapeMode.xhtml);
 String expected = "Hello &amp; World! &lt;greet name=&quot;John&quot;&gt;";
 assertEquals(expected, escaped);
 }

 @Test
 public void testEscapeWithBaseMode() {
 String original = "Hello & World! <greet name=\"John\"/>";
 Map<Character, String> map = EscapeMode.base.getMap();
 String escaped = escape(original, null, EscapeMode.base);
 String expected = "Hello &amp;amp; World! &lt;greet name=&apos;John&apos;&gt;";
 assertEquals(expected, escaped);
 }

 @Test
 public void testEscapeWithExtendedMode() {
 String original = "Hello & World! <greet name=\"John\"/>";
 Map<Character, String> map = EscapeMode.extended.getMap();
 String escaped = escape(original, null, EscapeMode.extended);
 String expected = "Hello &amp;amp;amp; World! &lt;greet name=&quot;John&quot;&gt;";
 assertEquals(expected, escaped);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeWithNullString() {
 String nullString = null;
 escape(nullString, null, EscapeMode.base);
 }

 @Test
 public void testUnescape() {
 String escaped = "Hello &amp;amp; World! &lt;greet name=&quot;John&quot;&gt;";
 String unescaped = unescape(escaped);
 String original = "Hello & World! <greet name=\"John\">";
 assertEquals(original, unescaped);
 }

 @Test
 public void testUnescapeWithStrict() {
 String escaped = "Hello &amp;amp; World! &lt;greet name=&quot;John&quot;&gt;";
 String unescaped = unescape(escaped, true);
 String original = "Hello &amp; World! <greet name=\"John\">";
 assertEquals(original, unescaped);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullString() {
 String nullString = null;
 unescape(nullString);
 }

 @Test
 public void testIsNamedEntityEdgeCases() {
 assertFalse(isNamedEntity(""));
 assertFalse(isNamedEntity(null));
 assertFalse(isNamedEntity("\uD800")); // High Surrogate
 assertFalse(isNamedEntity("\uDBFF")); // Low Surrogate
 assertFalse(isNamedEntity("\uD834\uDD1E")); // Supplementary Plane
 }

 @Test
 public void testGetCharacterByNameEdgeCases() {
 try {
 Character character = getCharacterByName("\uD834\uDD1E");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testEscapeEdgeCases() {
 // Test encoding of characters not in any map
 String original = "Hello \uD834\uDD1E World!";
 Map<Character, String> map = EscapeMode.base.getMap();
 String escaped = escape(original, null, EscapeMode.base);
 String expected = "Hello &#x{6}D834&#x{6}DD1E World!";
 assertEquals(expected, escaped);

 // Test encoding of Unicode characters outside the BMP
 String original = "Hello \uD800\uDC00 World!";
 Map<Character, String> map = EscapeMode.base.getMap();
 String escaped = escape(original, null, EscapeMode.base);
 String expected = "Hello &#x{1}0000&#x{1}00000 World!";
 assertEquals(expected, escaped);
 }

 @Test
 public void testUnescapeEdgeCases() {
 String escaped = "Hello &#x{1}0000&#x{1}00000 World!";
 String unescaped = unescape(escaped);
 String original = "Hello \uD800\uDC00 World!";
 assertEquals(original, unescaped);
 }

}