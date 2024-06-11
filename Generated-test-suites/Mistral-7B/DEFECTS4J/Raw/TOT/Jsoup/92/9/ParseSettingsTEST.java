import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

 @Test
 public void testHtmlDefaultNormTag() {
 String input = "<html><tag></tag></html>";
 ParseSettings settings = ParseSettings.htmlDefault;
 String expected = "tag";
 String actual = settings.normalizeTag("tag");
 assertEquals(expected, actual);
 }

 @Test
 public void testHtmlDefaultNormAttr() {
 String input = "<html <attr></attr>></html>";
 ParseSettings settings = ParseSettings.htmlDefault;
 String expected = "attr";
 String actual = settings.normalizeAttribute("attr");
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveCaseNormTag() {
 ParseSettings settings = new ParseSettings(true, false);
 String input = "<HTML><TAG></TAG></HTML>";
 String expected = "TAG";
 String actual = settings.normalizeTag("TAG");
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveCaseNormAttr() {
 ParseSettings settings = new ParseSettings(true, true);
 String input = "<html <ATTR></ATTR>></html>";
 String expected = "ATTR";
 String actual = settings.normalizeAttribute("ATTR");
 assertEquals(expected, actual);
 }

 @Test
 public void testPreserveCaseNormAttributes() {
 Attributes attributes = new Attributes();
 attributes.put("attr1", "value1");
 attributes.put("Attr2", "value2");
 ParseSettings settings = new ParseSettings(true, true);
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("attr1", normalizedAttributes.get("attr1"));
 assertEquals("Attr2", normalizedAttributes.get("Attr2"));
 }

 @Test
 public void testHtmlDefaultNormAttributes() {
 ParseSettings settings = ParseSettings.htmlDefault;
 Attributes attributes = new Attributes();
 attributes.put("attr1", "value1");
 attributes.put("Attr2", "value2");
 Attributes normalizedAttributes = settings.normalizeAttributes(attributes);
 assertEquals("attr1", normalizedAttributes.get("attr1"));
 assertEquals("attr2", normalizedAttributes.get("Attr2"));
 }

}