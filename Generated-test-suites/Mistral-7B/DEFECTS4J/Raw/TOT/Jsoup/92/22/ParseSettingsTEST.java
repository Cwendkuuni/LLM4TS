import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "DIV";
 String attributeName1 = "class";
 String attributeName2 = "data-test";

 String normalizedTagHtmlDefault = htmlDefault.normalizeTag(tagName);
 String normalizedAttributeHtmlDefault1 = htmlDefault.normalizeAttribute(attributeName1);
 String normalizedAttributeHtmlDefault2 = htmlDefault.normalizeAttribute(attributeName2);

 assertEquals(tagName, normalizedTagHtmlDefault);
 assertEquals("class", normalizedAttributeHtmlDefault1);
 assertEquals("data-test", normalizedAttributeHtmlDefault2);

 Attributes attributesHtmlDefault = new Attributes();
 attributesHtmlDefault.put("class", "testClass");
 attributesHtmlDefault.put("data-test", "testValue");

 Attributes normalizedAttributesHtmlDefault = htmlDefault.normalizeAttributes(attributesHtmlDefault);
 assertEquals("class", normalizedAttributesHtmlDefault.getClassAttr());
 assertEquals("testClass", normalizedAttributesHtmlDefault.getClassAttr());
 assertEquals("data-test", normalizedAttributesHtmlDefault.dataTestAttr());
 assertEquals("testValue", normalizedAttributesHtmlDefault.dataTestAttr("data-test"));
 }

 @Test
 public void testPreserveCaseTag() {
 ParseSettings preserveCaseTag = new ParseSettings(true, false);
 String tagName = "DIV";

 String normalizedTagPreserveCaseTag = preserveCaseTag.normalizeTag(tagName);

 assertEquals(tagName, normalizedTagPreserveCaseTag);
 }

 @Test
 public void testPreserveCaseAttribute() {
 ParseSettings preserveCaseAttribute = new ParseSettings(false, true);
 String attributeName = "Data-test";

 String normalizedAttributePreserveCaseAttribute = preserveCaseAttribute.normalizeAttribute(attributeName);

 assertEquals(attributeName, normalizedAttributePreserveCaseAttribute);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPreserveCaseInvalid() {
 ParseSettings preserveCaseInvalid = new ParseSettings(true, false);
 ParseSettings preserveCaseInvalid2 = new ParseSettings(false, true);

 String tagName = "";
 String attributeName = "";

 String normalizedTagPreserveCaseInvalid = preserveCaseInvalid.normalizeTag(tagName);
 String normalizedAttributePreserveCaseInvalid = preserveCaseInvalid2.normalizeAttribute(attributeName);
 }

 @Test
 public void testNormalizeAttributesPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes attributes = new Attributes();
 attributes.put("class", "testClass");
 attributes.put("data-test", "testValue");

 Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
 assertEquals("class", normalizedAttributes.getClassAttr());
 assertEquals("testClass", normalizedAttributes.getClassAttr());
 assertEquals("data-test", normalizedAttributes.dataTestAttr());
 assertEquals("testValue", normalizedAttributes.dataTestAttr("data-test"));
 }

}