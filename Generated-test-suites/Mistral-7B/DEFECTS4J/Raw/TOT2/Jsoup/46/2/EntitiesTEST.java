import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

// Test case for isNamedEntity method
@Test
public void testIsNamedEntity() {
Map<String, Character> fullEntities = Entities.full;
Character ltEntity = fullEntities.get("lt");
Assert.assertTrue(Entities.isNamedEntity("lt"));
Assert.assertEquals(ltEntity, Entities.getCharacterByName("lt"));
}

// Test case for isBaseNamedEntity method
@Test
public void testIsBaseNamedEntity() {
Map<String, Character> baseEntities = Entities.base;
Character ampEntity = baseEntities.get("amp");
Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
Assert.assertEquals(ampEntity, Entities.getCharacterByName("amp"));
}

// Test case for getCharacterByName method with known named entity
@Test
public void testGetCharacterByNameKnownEntity() {
Character ltEntity = Entities.getCharacterByName("lt");
Assert.assertEquals('<', ltEntity.charValue());
}

// Test case for getCharacterByName method with unknown named entity
@Test(expected = IllegalArgumentException.class)
public void testGetCharacterByNameUnknownEntity() {
Entities.getCharacterByName("abc");
}

// Test case for escape method with normal string
@Test
public void testEscapeNormalString() {
String source = "This is & a < test > string.";
String expected = "This is &amp; a &lt; test &gt; string.";
String result = Entities.escape(source, Document.OutputSettings.createPretty());
Assert.assertEquals(expected, result);
}

// Test case for escape method with empty string
@Test
public void testEscapeEmptyString() {
String source = "";
String expected = "";
String result = Entities.escape(source, Document.OutputSettings.createPretty());
Assert.assertEquals(expected, result);
}

// Test case for escape method with attribute value containing named entities
@Test
public void testEscapeAttributeValue() {
String source = "color=\"#&amp;039;ff00\"";
String expected = "color=\"#&amp;#x26;#x039;ff00\"";
String result = Entities.escape(source, Document.OutputSettings.createPretty());
Assert.assertEquals(expected, result);
}

// Test case for escape method with string containing control characters
@Test
public void testEscapeControlCharacters() {
String source = "\u0001\u0002\u0003";
String expected = "&#x0001;&#x0002;&#x0003";
String result = Entities.escape(source, Document.OutputSettings.createPretty());
Assert.assertEquals(expected, result);
}

// Test case for escape method with string containing surrogate pairs
@Test
public void testEscapeSurrogatePairs() {
String source = "\uD800\uDC00";
String expected = "&#x10000;";
String result = Entities.escape(source, Document.OutputSettings.createPretty());
Assert.assertEquals(expected, result);
}

// Test case for unescape method with empty string
@Test
public void testUnescapeEmptyString() {
String source = "";
String expected = "";
String result = Entities.unescape(source);
Assert.assertEquals(expected, result);
}

// Test case for unescape method with simple string
@Test
public void testUnescapeSimpleString() {
String source = "This is &amp; a &lt; test &gt; string.";
String expected = "This is a < test > string.";
String result = Entities.unescape(source);
Assert.assertEquals(expected, result);
}

// Test case for unescape method with string containing named entities
@Test
public void testUnescapeStringWithNamedEntities() {
String source = "color=\"#&amp;039;ff00\"";
String expected = "color=\"#FF00\"";
String result = Entities.unescape(source);
Assert.assertEquals(expected, result);
}

// Test case for unescape method with string containing control characters
@Test
public void testUnescapeControlCharacters() {
String source = "&#x0001;&#x0002;&#x0003";
String expected = "\u0001\u0002\u0003";
String result = Entities.unescape(source);
Assert.assertEquals(expected, result);
}

// Test case for canEncode method
@Test
public void testCanEncode() {
Assert.assertTrue(Entities.canEncode(CoreCharset.utf, 'a'));
Assert.assertTrue(Entities.canEncode(CoreCharset.utf, 'z'));
Assert.assertFalse(Entities.canEncode(CoreCharset.ascii, '\u0080'));
}

}