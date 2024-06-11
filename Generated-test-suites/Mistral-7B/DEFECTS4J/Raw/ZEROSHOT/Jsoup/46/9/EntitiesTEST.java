import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test isNamedEntity method with valid named entities
 assertEquals(true, Entities.isNamedEntity("lt"));
 assertEquals(true, Entities.isNamedEntity("quot"));
 assertEquals(true, Entities.isNamedEntity("amp"));
 assertEquals(true, Entities.isNamedEntity("gt"));

 // Test isNamedEntity method with invalid named entities
 assertEquals(false, Entities.isNamedEntity("invalidEntity"));
 }

 @Test
 public void testIsBaseNamedEntity() {
 // Test isBaseNamedEntity method with valid named entities in base set
 assertEquals(true, Entities.isBaseNamedEntity("lt"));
 assertEquals(true, Entities.isBaseNamedEntity("quot"));
 assertEquals(true, Entities.isBaseNamedEntity("amp"));
 assertEquals(true, Entities.isBaseNamedEntity("gt"));

 // Test isBaseNamedEntity method with invalid named entities
 assertEquals(false, Entities.isBaseNamedEntity("invalidEntity"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test getCharacterByName method with valid named entities
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('>', Entities.getCharacterByName("gt"));
 assertEquals('"', Entities.getCharacterByName("quot"));

 // Test getCharacterByName method with invalid named entities
 assertEquals(null, Entities.getCharacterByName("invalidEntity"));
 }

 @Test
 public void testEscape() {
 // Test escape method with different input strings and output settings
 String inputString1 = "Hello & World! <greeting name=\"John\">, you are & special!";
 String expectedOutputString1_xhtml = "Hello &amp; World! &lt;greeting name=&quot;John&quot;&gt;, you are &amp; special!";
 String expectedOutputString1_base = "Hello &amp;&amp; World! &lt;greeting name=&quot;John&quot;&gt;, you are &amp;amp; special!";
 String expectedOutputString1_extended = "Hello &amp;&amp;amp; World! &lt;greeting name=&quot;John&quot;&gt;, you are &amp;amp;amp; special!";

 String inputString2 = "This string contains only ASCII characters.";
 String expectedOutputString2 = inputString2;

 String inputString3 = "Hello, 世界! 你好！";
 String expectedOutputString3_utf = inputString3;
 String expectedOutputString3_ascii = "Hello, ??!!! 你好!!";

 String inputString4 = "This string contains surrogate pairs: \uD840\uDC00 \uD840\uDC01";
 String expectedOutputString4 = "This string contains surrogate pairs: \uD840\uDC00 \uD840\uDC01";

 // Test escape method with different output settings
 Document.OutputSettings xhtmlOutputSettings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);
 Document.OutputSettings baseOutputSettings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.base);
 Document.OutputSettings extendedOutputSettings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended);

 StringBuilder outputString1Xhtml = new StringBuilder();
 StringBuilder outputString1Base = new StringBuilder();
 StringBuilder outputString1Extended = new StringBuilder();
 StringBuilder outputString2 = new StringBuilder();
 StringBuilder outputString3Utf = new StringBuilder();
 StringBuilder outputString3Ascii = new StringBuilder();
 StringBuilder outputString4 = new StringBuilder();

 Entities.escape(outputString1Xhtml, inputString1, xhtmlOutputSettings, false, false, false);
 Equals(expectedOutputString1_xhtml, outputString1Xhtml.toString());

 Entities.escape(outputString1Base, inputString1, baseOutputSettings, false, false, false);
 Equals(expectedOutputString1_base, outputString1Base.toString());

 Entities.escape(outputString1Extended, inputString1, extendedOutputSettings, false, false, false);
 Equals(expectedOutputString1_extended, outputString1Extended.toString());

 Entities.escape(outputString2, inputString2, baseOutputSettings, false, false, false);
 Equals(expectedOutputString2, outputString2.toString());

 Entities.escape(outputString3Utf, inputString3, extendedOutputSettings, false, false, false);
 Equals(expectedOutputString3_utf, outputString3Utf.toString());

 Entities.escape(outputString3Ascii, inputString3, baseOutputSettings, false, false, false);
 Equals(expectedOutputString3_ascii, outputString3Ascii.toString());

 Entities.escape(outputString4, inputString4, extendedOutputSettings, false, false, false);
 Equals(expectedOutputString4, outputString4.toString());
 }

 @Test
 public void testUnescape() {
 // Test unescape method with different input strings and strict flag
 String inputString1 = "Hello &amp; World! &lt;greeting name=&quot;John&quot;&gt;, you are &amp; special!";
 String expectedOutputString1 = "Hello World! <greeting name=\"John\">, you are special!";
 String expectedOutputString1_strict = "Hello World! <greeting name=\"John\">, you are special!";

 String inputString2 = "Hello &amp; World! &lt;greeting name=&quot;John&quot;&gt;, you are &amp;amp; special!";
 String expectedOutputString2 = "Hello World! <greeting name=\"John\">, you are special!";
 String expectedOutputString2_strict = "Hello World! <greeting name=\"John\">, you are special!";

 String inputString3 = "Hello &amp; World! &lt;greeting name=&quot;John&quot;&gt;, you are &amp;amp;amp; special!";
 String expectedOutputString3 = "Hello World! <greeting name=\"John\">, you are special!";
 String expectedOutputString3_strict = "Hello World! <greeting name=\"John\">, you are special!";

 // Test unescape method with different strict flags
 Document.OutputSettings xhtmlOutputSettings = new Document.OutputSettings();
 Document.OutputSettings baseOutputSettings = new Document.OutputSettings();
 Document.OutputSettings extendedOutputSettings = new Document.OutputSettings();

 StringBuilder outputString1Xhtml = new StringBuilder();
 StringBuilder outputString1Base = new StringBuilder();
 StringBuilder outputString1Extended = new StringBuilder();
 StringBuilder outputString2 = new StringBuilder();
 StringBuilder outputString3 = new StringBuilder();

 Entities.escape(outputString1Xhtml, inputString1, new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml), false, false, false);
 StringBuilder unescapedString1Xhtml = new StringBuilder();
 Entities.unescape(unescapedString1Xhtml, outputString1Xhtml.toString(), true);
 Equals(expectedOutputString1, unescapedString1Xhtml.toString());

 Entities.escape(outputString1Base, inputString1, new Document.OutputSettings().escapeMode(Entities.EscapeMode.base), false, false, false);
 StringBuilder unescapedString1Base = new StringBuilder();
 Entities.unescape(unescapedString1Base, outputString1Base.toString(), true);
 Equals(expectedOutputString1, unescapedString1Base.toString());

 Entities.escape(outputString1Extended, inputString1, new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended), false, false, false);
 StringBuilder unescapedString1Extended = new StringBuilder();
 Entities.unescape(unescapedString1Extended, outputString1Extended.toString(), true);
 Equals(expectedOutputString1, unescapedString1Extended.toString());

 Entities.escape(outputString2, inputString2, new Document.OutputSettings().escapeMode(Entities.EscapeMode.base), false, false, false);
 StringBuilder unescapedString2 = new StringBuilder();
 Entities.unescape(unescapedString2, outputString2.toString(), true);
 Equals(expectedOutputString2, unescapedString2.toString());

 Entities.escape(outputString3, inputString3, new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended), false, false, false);
 StringBuilder unescapedString3 = new StringBuilder();
 Entities.unescape(unescapedString3, outputString3.toString(), true);
 Equals(expectedOutputString3, unescapedString3.toString());

 Entities.escape(outputString3, inputString3, new Document.OutputSettings().escapeMode(Entities.EscapeMode.extended), false, false, false);
 StringBuilder unescapedString3Strict = new StringBuilder();
 Entities.unescape(unescapedString3Strict, outputString3.toString(), false);
 Equals(expectedOutputString3, unescapedString3Strict.toString());
 }
}