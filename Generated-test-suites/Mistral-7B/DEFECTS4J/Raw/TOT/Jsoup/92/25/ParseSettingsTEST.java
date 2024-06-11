import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testNormalizeTag_HtmlDefault() {
 String tagName = "HTML";
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String expected = "html";
 String actual = htmlDefault.normalizeTag(tagName);
 assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeTag_PreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagName = "DIV";
 String expected = "DIV";
 String actual = preserveCase.normalizeTag(tagName);
 assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeAttribute_HtmlDefault() {
 String attrName = "class";
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String expected = "class";
 String actual = htmlDefault.normalizeAttribute(attrName);
 assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeAttribute_PreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String attrName = "CLASS";
 String expected = "CLASS";
 String actual = preserveCase.normalizeAttribute(attrName);
 assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeAttributes_HtmlDefault() {
 Attributes attributes = new Attributes();
 attributes.put("id", "test");
 attributes.put("class", "testClass");
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 Attributes expected = new Attributes().put("id", "test").put("class", "test");
 Attributes actual = htmlDefault.normalizeAttributes(attributes);
 assertEquals(expected, actual);
 }

 @Test
 public void testNormalizeAttributes_PreserveCase() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes attributes = new Attributes().put("ID", "test").put("CLASS", "testClass");
 Attributes expected = new Attributes().put("ID", "test").put("CLASS", "testClass");
 Attributes actual = preserveCase.normalizeAttributes(attributes);
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testNormalizeTag_NullTagName() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagName = null;
 String expectedErrorMessage = "Tag name cannot be null.";
 try {
 preserveCase.normalizeTag(tagName);
 } catch (AssertionError e) {
 String actualErrorMessage = e.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }
 }

 @Test(expected = AssertionError.class)
 public void testNormalizeAttribute_NullAttributeName() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String attrName = null;
 String expectedErrorMessage = "Attribute name cannot be null.";
 try {
 preserveCase.normalizeAttribute(attrName);
 } catch (AssertionError e) {
 String actualErrorMessage = e.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }
 }

 @Test(expected = AssertionError.class)
 public void testNormalizeAttributes_NullAttributes() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes attributes = null;
 String expectedErrorMessage = "Attributes cannot be null.";
 try {
 preserveCase.normalizeAttributes(attributes);
 } catch (AssertionError e) {
 String actualErrorMessage = e.getMessage();
 assertEquals(expectedErrorMessage, actualErrorMessage);
 }
 }
}