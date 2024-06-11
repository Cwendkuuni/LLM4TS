import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "DIV";
 String attributeName1 = "id";
 String attributeName2 = "class";
 String attributeValue = "testClass";

 String normalizedTagName = htmlDefault.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);

 Attributes attributes = new Attributes();
 attributes.put(attributeName1, attributeValue);
 attributes.put(attributeName2, attributeValue);
 Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(attributeValue, normalizedAttributes.get(attributeName1));
 assertEquals(attributeValue, normalizedAttributes.get(attributeName2));
 }

 @Test
 public void testPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagName = "DIV";
 String attributeName1 = "ID";
 String attributeName2 = "CLASS";
 String attributeValue = "testClass";

 String normalizedTagName = preserveCase.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);

 Attributes attributes = new Attributes();
 attributes.put(attributeName1, attributeValue);
 attributes.put(attributeName2, attributeValue);
 Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(attributeName1, normalizedAttributes.get(attributeName1).getName());
 assertEquals(attributeName2, normalizedAttributes.get(attributeName2).getName());
 }

 @Test
 public void testInvalidPreserveSettings() {
 ParseSettings invalidSettings = new ParseSettings(true, false);
 String tagName = "DIV";
 String attributeName = "id";
 String attributeValue = "testClass";

 String normalizedTagName = invalidSettings.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);

 Attributes attributes = new Attributes();
 attributes.put(attributeName, attributeValue);
 Attributes normalizedAttributes = invalidSettings.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(attributeValue, normalizedAttributes.get(attributeName).getValue()); // Attribute value not normalized due to preserveAttributeCase = false
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativePreserveSettings() {
 ParseSettings negativeSettings = new ParseSettings(false, false);
 String tagName = "DIV";
 String attributeName = "id";

 negativeSettings.normalizeTag(tagName); // Throws IllegalArgumentException since preserveTagCase is false
 }
}