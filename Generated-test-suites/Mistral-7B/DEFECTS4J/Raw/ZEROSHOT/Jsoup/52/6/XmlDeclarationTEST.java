import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = new Document();
 }

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertNull(xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("!-- processing instruction --", null, true);
 assertEquals("#processing-instruction", xmlDeclaration.nodeName());
 assertEquals("!-- processing instruction --", xmlDeclaration.name());
 assertEquals("!-- processing instruction --", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<xml/>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtml() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Element element = new Element("root");
 element.appendElement(xmlDeclaration);
 assertEquals("<xml/>", element.outerHtml());
 }

 @Test
 public void testXmlDeclarationOuterHtmlWithVersion() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 Element element = new Element("root");
 element.appendElement(xmlDeclaration);
 assertEquals("<xml version=\"1.0\"/>", element.outerHtml());
 }

 @Test
 public void testXmlDeclarationOuterHtmlWithEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 Element element = new Element("root");
 element.appendElement(xmlDeclaration);
 assertEquals("<xml encoding=\"UTF-8\"/>", element.outerHtml());
 }

 @Test
 public void testXmlDeclarationOuterHtmlWithVersionAndEncoding() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 Element element = new Element("root");
 element.appendElement(xmlDeclaration);
 assertEquals("<xml version=\"1.0\" encoding=\"UTF-8\"/>", element.outerHtml());
 }
}