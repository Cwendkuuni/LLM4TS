import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultPreserveTagCaseFalse() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "DIV";
 String normalizedTagName = htmlDefault.normalizeTag(tagName);
 assertTrue(lowerCase(tagName).equals(normalizedTagName));
 }

 @Test
 public void testHtmlDefaultPreserveTagCaseTrue() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 String tagName = "DIV";
 ParseSettings preserveCase = new ParseSettings(true, false);
 String normalizedTagName = preserveCase.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);
 }

 @Test
 public void testPreserveCasePreserveTagCaseTrue() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String tagName = "DIV";
 String normalizedTagName = preserveCase.normalizeTag(tagName);
 assertEquals(tagName, normalizedTagName);
 }

 @Test
 public void testPreserveCasePreserveAttributeCaseFalse() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String attributeName = "class";
 String normalizedAttributeName = preserveCase.normalizeAttribute(attributeName);
 assertTrue(lowerCase(attributeName).equals(normalizedAttributeName));
 }

 @Test
 public void testPreserveCasePreserveAttributeCaseTrue() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 String attributeName = "class";
 String normalizedAttributeName = preserveCase.normalizeAttribute(attributeName);
 assertEquals(attributeName, normalizedAttributeName);
 }

 @Test
 public void testHtmlDefaultNormalizeAttributes() {
 ParseSettings htmlDefault = ParseSettings.htmlDefault;
 Attributes attributes = new Attributes();
 attributes.put("class", "button");
 attributes.put("id", "submit");
 Attributes normalizedAttributes = htmlDefault.normalizeAttributes(attributes);
 assertNotNull(normalizedAttributes);
 assertEquals(2, normalizedAttributes.size());
 assertTrue(normalizedAttributes.get("class").equals(lowerCase("button")));
 assertTrue(normalizedAttributes.get("id").equals(lowerCase("submit")));
 }

 @Test
 public void testPreserveCaseNormalizeAttributes() {
 ParseSettings preserveCase = ParseSettings.preserveCase;
 Attributes attributes = new Attributes();
 attributes.put("class", "button");
 attributes.put("id", "submit");
 Attributes normalizedAttributes = preserveCase.normalizeAttributes(attributes);
 assertEquals(2, normalizedAttributes.size());
 assertEquals(attributes.get("class"), normalizedAttributes.get("class"));
 assertEquals(attributes.get("id"), normalizedAttributes.get("id"));
 }

}