import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testParse() {
 String xmlInput = "<root><element attr=\"value\">Content</element></root>";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");
 Element root = xmlDoc.rootElement();
 Element element = root.child(0);
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseWithEmptyInput() {
 String xmlInput = "";
 Document xmlDoc = xmlTreeBuilder.parse(xmlInput, "http://test.com");
 assertNull(xmlDoc);
 }

 @Test
 public void testParseWithInvalidInput() {
 String xmlInput = "<invalid>";
 try {
 xmlTreeBuilder.parse(xmlInput, "http://test.com");
 fail("Expected ParseException not thrown");
 } catch (ParseException e) {
 // pass
 }
 }

 @Test
 public void testParseFragment() {
 String xmlFragment = "<root><element attr=\"value\">Content</element></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element root = (Element) nodes.get(0);
 Element element = (Element) nodes.get(1);
 assertEquals("root", root.nodeName());
 assertEquals("element", element.nodeName());
 assertEquals("value", element.attr("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseFragmentWithEmptyInput() {
 String xmlFragment = "";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertFalse(nodes.isEmpty());
 }

 @Test
 public void testParseFragmentWithInvalidInput() {
 String xmlFragment = "<invalid>";
 try {
 xmlTreeBuilder.parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 fail("Expected ParseException not thrown");
 } catch (ParseException e) {
 // pass
 }
 }
}