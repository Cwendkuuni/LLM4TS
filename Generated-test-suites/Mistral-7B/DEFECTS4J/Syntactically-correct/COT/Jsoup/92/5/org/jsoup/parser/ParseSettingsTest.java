import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 private ParseSettings htmlDefault;
 private ParseSettings preserveCase;

 @Before
 public void setUp() {
 htmlDefault = ParseSettings.htmlDefault;
 preserveCase = ParseSettings.preserveCase;
 }

 // Testing constructor
 @Test
 public void testParseSettingsConstructor() {
 ParseSettings parseSettings1 = new ParseSettings(false, false);
 ParseSettings parseSettings2 = new ParseSettings(true, false);
 ParseSettings parseSettings3 = new ParseSettings(false, true);
 ParseSettings parseSettings4 = new ParseSettings(true, true);
 assertEquals(htmlDefault.preserveTagCase(), false);
 assertEquals(htmlDefault.preserveAttributeCase(), false);
 assertEquals(preserveCase.preserveTagCase(), true);
 assertEquals(preserveCase.preserveAttributeCase(), true);
 }

 // Testing preserveTagCase() method
 @Test
 public void testPreserveTagCase() {
 ParseSettings parseSettings = new ParseSettings(true, false);
 assertTrue(parseSettings.preserveTagCase());
 parseSettings = new ParseSettings(false, false);
 assertFalse(parseSettings.preserveTagCase());
 }

 // Testing preserveAttributeCase() method
 @Test
 public void testPreserveAttributeCase() {
 ParseSettings parseSettings = new ParseSettings(false, true);
 assertTrue(parseSettings.preserveAttributeCase());
 parseSettings = new ParseSettings(false, false);
 assertFalse(parseSettings.preserveAttributeCase());
 }

 // Testing normalizeTag() method with same tag name
 @Test
 public void testNormalizeTagSameName() {
 String tagName = "TAG";
 String normalizedTagNameHtmlDefault = htmlDefault.normalizeTag(tagName);
 String normalizedTagNamePreserveCase = preserveCase.normalizeTag(tagName);
 assertEquals(normalizedTagNameHtmlDefault, "tag");
 assertEquals(normalizedTagNamePreserveCase, "TAG");
 }

 // Testing normalizeTag() method with different tag names
 @Test
 public void testNormalizeTagDifferentNames() {
 String tagName1 = "Tag1";
 String tagName2 = "Tag2";
 String normalizedTagNameHtmlDefault1 = htmlDefault.normalizeTag(tagName1);
 String normalizedTagNamePreserveCase1 = preserveCase.normalizeTag(tagName1);
 String normalizedTagNameHtmlDefault2 = htmlDefault.normalizeTag(tagName2);
 String normalizedTagNamePreserveCase2 = preserveCase.normalizeTag(tagName2);
 assertEquals(normalizedTagNameHtmlDefault1, "tag1");
 assertEquals(normalizedTagNamePreserveCase1, "Tag1");
 assertEquals(normalizedTagNameHtmlDefault2, "tag2");
 assertEquals(normalizedTagNamePreserveCase2, "Tag2");
 }

 // Testing normalizeAttribute() method with same attribute name
 @Test
 public void testNormalizeAttributeSameName() {
 String attributeName = "ATTRIBUTE";
 String normalizedAttributeNameHtmlDefault = htmlDefault.normalizeAttribute(attributeName);
 String normalizedAttributeNamePreserveCase = preserveCase.normalizeAttribute(attributeName);
 assertEquals(normalizedAttributeNameHtmlDefault, "attribute");
 assertEquals(normalizedAttributeNamePreserveCase, "ATTRIBUTE");
 }

 // Testing normalizeAttribute() method with different attribute names
 @Test
 public void testNormalizeAttributeDifferentNames() {
 String attributeName1 = "Attribute1";
 String attributeName2 = "Attribute2";
 String normalizedAttributeNameHtmlDefault1 = htmlDefault.normalizeAttribute(attributeName1);
 String normalizedAttributeNamePreserveCase1 = preserveCase.normalizeAttribute(attributeName1);
 String normalizedAttributeNameHtmlDefault2 = htmlDefault.normalizeAttribute(attributeName2);
 String normalizedAttributeNamePreserveCase2 = preserveCase.normalizeAttribute(attributeName2);
 assertEquals(normalizedAttributeNameHtmlDefault1, "attribute1");
 assertEquals(normalizedAttributeNamePreserveCase1, "Attribute1");
 assertEquals(normalizedAttributeNameHtmlDefault2, "attribute2");
 assertEquals(normalizedAttributeNamePreserveCase2, "Attribute2");
 }

 // Testing normalizeAttributes() method with lower case attribute names
 @Test
 public void testNormalizeAttributesLowerCase() {
 String attributeName1 = "attribute1";
 String attributeName2 = "attribute2";
 Attributes attributes = new Attributes();
 attributes.put(attributeName1, "value1");
 attributes.put(attributeName2, "value2");
 Attributes normalizedAttributesHtmlDefault = htmlDefault.normalizeAttributes(attributes);
 Attributes normalizedAttributesPreserveCase = preserveCase.normalizeAttributes(attributes);
 assertEquals(normalizedAttributesHtmlDefault.toString(), "attribute1=\"value1\" attribute2=\"value2\"");
 assertEquals(normalizedAttributesPreserveCase.toString(), "attribute1=\"attribute1\" attribute2=\"attribute2\"");
 }

 // Testing normalizeAttributes() method with upper case attribute names
 @Test (expected = IllegalArgumentException.class)
 public void testNormalizeAttributesUpperCaseException() {
 String attributeName1 = "ATTRIBUTE1";
 String attributeName2 = "ATTRIBUTE2";
 Attributes attributes = new Attributes();
 attributes.put(attributeName1, "value1");
 attributes.put(attributeName2, "value2");
 ParseSettings parseSettings = new ParseSettings(false, false);
 try {
 parseSettings.normalizeAttributes(attributes);
 } catch (IllegalArgumentException e) {
 assertEquals(e.getMessage(), "Invalid attribute name: ATTribuTE1");
 }
 }

 // Testing normalizeAttributes() method with empty attribute names
 @Test (expected = IllegalArgumentException.class)
 public void testNormalizeAttributesEmptyNameException() {
 String attributeName = "";
 Attributes attributes = new Attributes();
 attributes.put(attributeName, "value");
 ParseSettings parseSettings = new ParseSettings(false, false);
 try {
 parseSettings.normalizeAttributes(attributes);
 } catch (IllegalArgumentException e) {
 assertEquals(e.getMessage(), "Invalid attribute name: ");
 }
 }

}