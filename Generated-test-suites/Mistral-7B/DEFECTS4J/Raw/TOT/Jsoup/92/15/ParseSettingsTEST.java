import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultParseSettings() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagNameLowerCased = htmlDefault.normalizeTag("HTML");
 String attributeNameLowerCased = htmlDefault.normalizeAttribute("CLASS");
 assertTrue(htmlDefault.preserveTagCase() == false);
 assertTrue(htmlDefault.preserveAttributeCase() == false);
 assertEquals("html", tagNameLowerCased);
 assertEquals("class", attributeNameLowerCased);
 }

 @Test
 public void testPreserveCaseParseSettings() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagNamePreserved = preserveCase.normalizeTag("DIV");
 String attributeNamePreserved = preserveCase.normalizeAttribute("ID");
 assertTrue(preserveCase.preserveTagCase() == true);
 assertTrue(preserveCase.preserveAttributeCase() == true);
 assertEquals("DIV", tagNamePreserved);
 assertEquals("ID", attributeNamePreserved);
 }

 @Test
 public void testNormalizeAttributes() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes attributes = new Attributes();
 attributes.put("class", "class1 class2 class3");
 attributes = preserveCase.normalizeAttributes(attributes);
 String[] expectedAttributeNames = {"class1", "class2", "class3"};
 String[] actualAttributeNames = new String[attributes.size()];
 int i = 0;
 for (String name : attributes.keySet()) {
 actualAttributeNames[i++] = name;
 }
 assertArrayEquals(expectedAttributeNames, actualAttributeNames);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalizeTagNull() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String nullTagName = null;
 try {
 preserveCase.normalizeTag(nullTagName);
 } catch (IllegalArgumentException e) {
 assertEquals("tag cannot be null", e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNormalizeAttributeNull() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String nullAttributeName = null;
 try {
 preserveCase.normalizeAttribute(nullAttributeName);
 } catch (IllegalArgumentException e) {
 assertEquals("name cannot be null", e.getMessage());
 }
 }
}