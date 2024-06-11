import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<xml>", xmlDeclaration.getWholeDeclaration());
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

 @Test (expected = NullPointerException.class)
 public void testInvalidXmlDeclarationName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 }

 @Test (expected = NullPointerException.class)
 public void testInvalidBaseUri() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 }

 @Test
 public void testXmlProcessingInstruction() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-pi", null, true);
 assertEquals("#processing-instruction", xmlDeclaration.nodeName());
 assertEquals("xml-pi", xmlDeclaration.name());
 assertEquals("<?xml-pi>", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 assertEquals("<xml>", xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 Document doc = Jsoup.parse("<html><head><![CDATA[");
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Element xml = doc.createElement("xml");
 xmlDeclaration.outerHtmlHead(xml.appendText(""), 0, null);
 assertEquals("<xml>", xml.html());
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 Document doc = Jsoup.parse("<html><head><![CDATA[");
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Element xml = doc.createElement("xml");
 xmlDeclaration.outerHtmlTail(xml.appendText(""), 0, null);
 assertEquals("</xml>", xml.html());
 }
}