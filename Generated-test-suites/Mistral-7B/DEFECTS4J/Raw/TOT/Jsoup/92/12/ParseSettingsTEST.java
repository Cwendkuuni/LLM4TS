import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultNormalizeTag() {
 String input = "HTMLTAG";
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String expected = "htmltag";
 String actual = htmlDefault.normalizeTag(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testHtmlDefaultNormalizeAttribute() {
 String input = "attributeHTML";
 ParseSettings htmlDefault = ParseSets.htmlDefault;
 String expected = "attributehtml";
 String actual = htmlDefault.normalizeAttribute(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveCaseNormalizeTag() {
 String input = "TagName";
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String expected = "TagName";
 String actual = preserveCase.normalizeTag(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveCaseNormalizeAttribute() {
 String input = "AttributeName";
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String expected = "AttributeName";
 String actual = preserveCase.normalizeAttribute(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveCaseNormalizeAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("tagName", "TAGNAME");
 attributes.put("attributeName", "ATTRIBUTENAME");
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes expected = attributes;
 Attributes actual = preserveCase.normalizeAttributes(attributes);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPreserveTagSetting() {
 ParseSettings parseSettings = new ParseSettings(true, false);
 String input = "TAGNAME";
 String expected = "TAGNAME";
 String actual = parseSettings.normalizeTag(input);
 fail("Expected IllegalArgumentException, but got: " + actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidPreserveAttributeSetting() {
 ParseSettings parseSettings = new ParseSettings(false, true);
 String input = "ATTRIBUTE";
 String expected = "attribute";
 String actual = parseSettings.normalizeAttribute(input);
 fail("Expected IllegalArgumentException, but got: " + actual);
 }
}