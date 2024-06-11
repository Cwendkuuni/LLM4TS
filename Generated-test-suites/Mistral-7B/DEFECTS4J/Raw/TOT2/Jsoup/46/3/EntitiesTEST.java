import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

 // Test for isNamedEntity method
 @Test
 public void testIsNamedEntity() {
 String namedEntity = "lt";
 boolean expectedResult = true;
 boolean actualResult = Entities.isNamedEntity(namedEntity);
 assertTrue("Expected named entity 'lt' to be known, but it was not.", actualResult == expectedResult);
 }

 // Test for isBaseNamedEntity method
 @Test
 public void testIsBaseNamedEntity() {
 String baseNamedEntity = "amp";
 boolean expectedResult = false;
 boolean actualResult = Entities.isBaseNamedEntity(baseNamedEntity);
 assertFalse("Expected named entity 'amp' not to be in base set, but it was.", actualResult == expectedResult);
 }

 // Test for getCharacterByName method
 @Test
 public void testGetCharacterByName() {
 String namedEntity = "apos";
 Character expectedResult = '\'';
 Character actualResult = Entities.getCharacterByName(namedEntity);
 assertEquals("Expected named entity 'apos' to return apostrophe character.", expectedResult, actualResult);
 }

 // Test for escape method with normal escape scenario
 @Test
 public void testEscapeNormal() {
 String inputString = "This is a <test> string.";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 StringBuilder expectedOutput = new StringBuilder("This is a &lt;test&gt; string.");
 StringBuilder actualOutput = new StringBuilder();
 Entities.escape(actualOutput, inputString, outputSettings, false, false, false);
 assertEquals("Expected escaped string to match.", expectedOutput.toString(), actualOutput.toString());
 }

 // Test for escape method with whitespace normalization
 @Test
 public void testEscapeWithWhiteSpace() {
 String inputString = "This is a \t string with whitespace.";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.setNormalizeWhiteSpace(true);
 StringBuilder expectedOutput = new StringBuilder("This is a \t &nbsp;string with whitespace.");
 StringBuilder actualOutput = new StringBuilder();
 Entities.escape(actualOutput, inputString, outputSettings, false, false, false);
 assertEquals("Expected escaped string with whitespace normalization to match.", expectedOutput.toString(), actualOutput.toString());
 }

 // Test for escape method with strip leading white space
 @Test
 public void testEscapeWithStripLeadingWhiteSpace() {
 String inputString = "\tThis is a string.\t";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.setNormalizeWhiteSpace(true);
 outputSettings.setStripLeadingWhiteSpace(true);
 StringBuilder expectedOutput = new StringBuilder("This is a ");
 StringBuilder actualOutput = new StringBuilder();
 Entities.escape(actualOutput, inputString, outputSettings, false, false, false);
 assertEquals("Expected escaped string with leading whitespace stripping to match.", expectedOutput.toString(), actualOutput.toString());
 }

 // Test for escape method with different character encoding
 @Test
 public void testEscapeWithDifferentEncoding() {
 String inputString = "Differently encoded: ÆØÅ";
 Document.OutputSettings outputSettings = new Document.OutputSettings();
 outputSettings.setEncoder(CharsetEncoder.forName("ISO-8859-1"));
 StringBuilder expectedOutput = new StringBuilder("Differently encoded: &#xA6;&#OXF8;&#xA5;");
 StringBuilder actualOutput = new StringBuilder();
 Entities.escape(actualOutput, inputString, outputSettings, false, false, false);
 assertEquals("Expected escaped string with different encoding to match.", expectedOutput.toString(), actualOutput.toString());
 }

 // Test for unescape method with normal scenario
 @Test
 public void testUnescapeNormal() {
 String inputString = "This is a &lt;test&gt; string.";
 StringBuilder expectedOutput = new StringBuilder("This is a <test> string.");
 StringBuilder actualOutput = new StringBuilder();
 Entities.unescape(actualOutput, inputString, false);
 assertEquals("Expected unescaped string to match.", expectedOutput.toString(), actualOutput.toString());
 }

 // Test for unescape method with strict mode
 @Test
 public void testUnescapeStrict() { ;
 String inputString = "This is a &lt;test&gt; string.";
 StringBuilder expectedOutput = new StringBuilder("This is a <test> string.");
 StringBuilder actualOutput = new StringBuilder();
 Entities.unescape(actualOutput, inputString, true);
 assertEquals("Expected unescaped string to match.", expectedOutput.toString(), actualOutput.toString());
 }

 // Test for canEncode method
 @Test
 public void testCanEncode() {
 CoreCharset coreCharset = CoreCharset.utf;
 char testCharacter = '\u00E5';
 boolean expectedResult = true;
 boolean actualResult = Entities.canEncode(coreCharset, testCharacter, null);
 assertTrue("Expected character 'Å' to be encodable.", actualResult == expectedResult);
 }

 // Test for canEncode method with non-encodable character
 @Test(expected = UnsupportedOperationException.class)
 public void testCanEncodeNonEncodable() {
 CoreCharset coreCharset = CoreCharset.ascii;
 char testCharacter = '\u03B4'; // Greek small letter Pi
 boolean expectedResult = false;
 boolean actualResult = Entities.canEncode(coreCharset, testCharacter, null);
 assertFalse("Expected character 'Π' to not be encodable.", actualResult == expectedResult);
 }

 // Test for loadEntities method
 @Test
 public void testLoadEntities() {
 Map<String, Character> expectedEntities = Entities.full;
 Map<String, Character> actualEntities = Entities.loadEntities("entities-full.properties");
 assertEquals("Expected loaded entities to match.", expectedEntities, actualEntities);
 }
}