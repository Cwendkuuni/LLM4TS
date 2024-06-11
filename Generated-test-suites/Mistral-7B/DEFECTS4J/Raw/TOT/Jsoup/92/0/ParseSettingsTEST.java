import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "Div";
 String attributeName1 = "class";
 String attributeName2 = "ID";

 String normalizedTagHtmlDefault = htmlDefault.normalizeTag(tagName);
 String normalizedAttributeHtmlDefault1 = htmlDefault.normalizeAttribute(attributeName1);
 String normalizedAttributeHtmlDefault2 = htmlDefault.normalizeAttribute(attributeName2);

 assertEquals(tagName, normalizedTagHtmlDefault); // Checks if lower case tag name matches the original
 assertEquals(attributeName1, normalizedAttributeHtmlDefault1); // Checks if lower case attribute name matches the original
 assertEquals("id", normalizedAttributeHtmlDefault2); // Checks if lower case attribute name is normalized to standard HTML id

 Attributes attributesHtmlDefault = new Attributes();
 attributesHtmlDefault.put("class", attributeName1);
 attributesHtmlDefault.put("ID", attributeName2);
 Attributes normalizedAttributesHtmlDefault = htmlDefault.normalizeAttributes(attributesHtmlDefault);
 assertEquals(attributeName1, normalizedAttributesHtmlDefault.get("class")); // Checks if attribute name is normalized in lower case
 assertEquals(attributeName2, normalizedAttributesHtmlDefault.get("ID")); // Checks if attribute name is normalized in lower case
 }

 @Test
 public void testPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagName = "DIV";
 String attributeName1 = "CLASS";
 String attributeName2 = "ID";

 String normalizedTagPreserveCase = preserveCase.normalizeTag(tagName);
 String normalizedAttributePreserveCase1 = preserveCase.normalizeAttribute(attributeName1);
 String normalizedAttributePreserveCase2 = preserveCase.normalizeAttribute(attributeName2);

 assertEquals(tagName, normalizedTagPreserveCase); // Checks if tag name is preserved as is
 assertEquals(attributeName1, normalizedAttributePreserveCase1); // Checks if attribute name is preserved as is
 assertEquals(attributeName2, normalizedAttributePreserveCase2); // Checks if attribute name is preserved as is

 Attributes attributesPreserveCase = new Attributes();
 attributesPreserveCase.put(attributeName1, "value1");
 attributesPreserveCase.put(attributeName2, "value2");
 Attributes normalizedAttributesPreserveCase = preserveCase.normalizeAttributes(attributesPreserveCase);

 assertEquals(attributeName1, normalizedAttributesPreserveCase.get(attributeName1)); // Checks if attribute name and value are preserved as is
 assertEquals(attributeName2, normalizedAttributesPreserveCase.get(attributeName2)); // Checks if attribute name and value are preserved as is
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPreserveCaseWithNull() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes attributesNull = null;

 try {
 preserveCase.normalizeAttributes(attributesNull);
 } catch (IllegalArgumentException e) {
 assertEquals("attributes can't be null", e.getMessage()); // Checks if the correct error message is thrown
 }
 }
}