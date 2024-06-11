import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefault() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagNameLower = htmlDefault.normalizeTag("DIV");
 String tagNamePreserved = htmlDefault.normalizeTag("DIV");
 String attributeNameLower = htmlDefault.normalizeAttribute("class");
 String attributeNamePreserved = htmlDefault.normalizeAttribute("class");
 assertEquals(tagNamePreserved, tagNamePreserved); // Preserved tag name is equal to the original name
 assertEquals(tagNameLower, "div"); // Lower-cased tag name is "div"
 assertEquals(attributeNameLower, "class"); // Lower-cased attribute name is "class"
 assertEquals(attributeNamePreserved, "class"); // Preserved attribute name is equal to the original name
 }

 @Test
 public void testPreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagNameLower = preserveCase.normalizeTag("DIV");
 String tagNamePreserved = preserveCase.normalizeTag("DIV");
 String attributeNameLower = preserveCase.normalizeAttribute("CLASS");
 String attributeNamePreserved = preserveCase.normalizeAttribute("CLASS");
 assertEquals(tagNamePreserved, "DIV"); // Preserved tag name is equal to the original name
 assertEquals(tagNameLower, "div"); // Lower-cased tag name is "div"
 assertEquals(attributeNamePreserved, "CLASS"); // Preserved attribute name is "CLASS"
 assertEquals(attributeNameLower, "class"); // Lower-cased attribute name is "class"
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes attributes = new Attributes();
 attributes.put("Class", "test");
 attributes.put("class", "Test");
 Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
 String classAttributeName = normalizedAttributes.get("class");
 assertEquals(classAttributeName, "Test"); // Preserved attribute name is "Test"
 }

 @Test(expected = NullPointerException.class)
 public void testNullNormalizeTag() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String nullName = null;
 String name = preserveCase.normalizeTag(nullName);
 }

 @Test(expected = NullPointerException.class)
 public void testNullNormalizeAttribute() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String nullName = null;
 String name = preserveCase.normalizeAttribute(nullName);
 }

 @Test(expected = NullPointerException.class)
 public void testNullAttributes() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes nullAttributes = null;
 ParseSettings.ParseSettings normalizeAttributes = preserveCase.normalizeAttributes(nullAttributes);
 }
}