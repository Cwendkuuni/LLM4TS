import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseError;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testParseValidXML() {
 String xmlString = "<root><element attr=\"value\">Content</element></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 Element root = doc.getRootElement();
 assertEquals("root", root.tagName());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.attribute("attr"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseXMLWithAttributes() {
 String xmlString = "<root attr1=\"value1\" attr2='value2'><element attr3=\"value3\">Content</element></root>";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 Element root = doc.getRootElement();
 assertEquals("root", root.tagName());
 Element element = root.child(0);
 assertEquals("element", element.tagName());
 assertEquals("value1", root.attribute("attr1"));
 assertEquals("value2", root.attribute("attr2"));
 assertEquals("value3", element.attribute("attr3"));
 assertEquals("Content", element.text());
 }

 @Test
 public void testParseEmptyXML() {
 String xmlString = "<root />";
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 Element root = doc.getRootElement();
 assertEquals("root", root.tagName());
 assertEquals(0, root.childSize());
 }

 @Test(expected = ParseError.class)
 public void testParseInvalidXML() {
 String xmlString = "<root<element />"; // missing '>' for start tag
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = null;
 try {
 doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 } catch (ParseError e) {
 // expected
 }
 assertNotNull(doc);
 assertNull(doc.getRootElement());
 }

 @Test(expected = ParseError.class)
 public void testParseXMLWithUnclosedTag() {
 String xmlString = "<root><element /><element"; // unclosed tag
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 Document doc = null;
 try {
 doc = xmlTreeBuilder.parse(new StringReader(xmlString), "http://test.com");
 } catch (ParseError e) {
 // expected
 }
 assertNotNull(doc);
 assertNull(doc.getRootElement());
 }
}