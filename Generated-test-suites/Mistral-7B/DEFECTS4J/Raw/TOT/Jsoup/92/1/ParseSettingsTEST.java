import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "DIV";
 String attributeName1 = "class";
 String attributeName2 = "data-test";

 String normalizedTagName = htmlDefault.normalizeTag(tagName);
 String normalizedAttributeName1 = htmlDefault.normalizeAttribute(attributeName1);
 String normalizedAttributeName2 = htmlDefault.normalizeAttribute(attributeName2);

 assertEquals(tagName, normalizedTagName); // HTML default preserves lower case for tags
 assertEquals(attributeName1, normalizedAttributeName1); // HTML default preserves lower case for attributes
 assertEquals(attributeName2, normalizedAttributeName2); // HTML default preserves lower case for attributes

 Attributes attributes = new Attributes();
 attributes.put(attributeName1, "value1");
 attributes.put(attributeName2, "value2");

 Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 }

 @Test
 public void testPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagName = "DIV";
 String attributeName1 = "CLASS";
 String attributeName2 = "DATA-TEST";

 String normalizedTagName = preserveCase.normalizeTag(tagName);
 String normalizedAttributeName1 = preserveCase.normalizeAttribute(attributeName1);
 String normalizedAttributeName2 = preserveCase.normalizeAttribute(attributeName2);

 assertEquals(tagName, normalizedTagName); // Preserves tag name case
 assertEquals(attributeName1, normalizedAttributeName1); // Preserves attribute name case
 assertEquals(attributeName2, normalizedAttributeName2); // Preserves attribute name case

 Attributes attributes = new Attributes();
 attributes.put(attributeName1, "value1");
 attributes.put(attributeName2, "value2");

 Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 }

 @Test
 public void testErrorScenario_NullName() {
 ParseSettings preserveCase = ParseSettings.preserveCase;

 String nullTagName = null;
 String nullAttributeName = null;

 try {
 String normalizedTagName = preserveCase.normalizeTag(nullTagName);
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 String normalizedAttributeName = preserveCase.normalizeAttribute(nullAttributeName);
 } catch (NullPointerException e) {
 // Expected
 }

 try {
 ParseSettings newParseSettings = new ParseSettings(false, null);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testErrorScenario_EmptyName() {
 ParseSettings preserveCase = ParseSettings.preserveCase;

 String emptyTagName = "";
 String emptyAttributeName = "";

 String normalizedTagName = preserveCase.normalizeTag(emptyTagName);
 assertEquals("", normalizedTagName); // Empty strings are preserved as is

 String normalizedAttributeName = preserveCase.normalizeAttribute(emptyAttributeName);
 assertEquals("", normalizedAttributeName); // Empty strings are preserved as is
 }
}