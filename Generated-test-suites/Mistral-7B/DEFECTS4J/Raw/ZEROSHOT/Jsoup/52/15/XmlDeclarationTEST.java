import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class XmlDeclarationTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<xml version='1.0' encoding='UTF-8'></xml>");
 }

 @Test
 public void testCreateXmlDeclaration() {
 String name = "xml";
 String baseUri = null;
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(name, xmlDeclaration.name());
 assertEquals("xml", xmlDeclaration.getWholeDeclaration());
 }

 @Test
 public void testCreateProcessingInstruction() {
 String name = null;
 String baseUri = null;
 boolean isProcessingInstruction = true;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

 assertFalse(xmlDeclaration.isProcessingInstruction());
 assertEquals("#processing-instruction", xmlDeclaration.nodeName());
 assertNull(xmlDeclaration.name());
 String declaration = xmlDeclaration.getWholeDeclaration();
 assertEquals("<?", declaration.substring(0, 2));
 assertNull(declaration.substring(2));
 }

 @Test
 public void testGetWholeDeclaration() {
 String name = "xml";
 String baseUri = null;
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

 String declaration = xmlDeclaration.getWholeDeclaration();
 assertEquals("xml", declaration);

 xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");
 declaration = xmlDeclaration.getWholeDeclaration();
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 String expected = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 Appendable accumulator = Jsoup.output().appendDocument(document.select("head").appendElement("head"));
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.outerHtmlHead(accumulator, 0, null);
 assertEquals(expected, accumulator.toString());
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accumulator = Jsoup.output().appendDocument(document.select("head").appendElement("head"));
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.outerHtmlTail(accumulator, 0, null);
 assertTrue(accumulator.toString().isEmpty());
 }

 @Test
 public void testToString() throws IOException {
 String xmlDeclarationString = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 Element xmlDeclarationElement = Jsoup.parse(xmlDeclarationString).select("declaration").first();
 XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlDeclarationElement;
 assertEquals(xmlDeclarationString, xmlDeclaration.toString());
 }

 @Test
 public void testXmlDeclarationWithProcessingInstruction() throws IOException {
 String xml = "<!processing-instruction(xml version='1.0' encoding='UTF-8')[ <html> ]>";
 Document parsedDocument = Jsoup.parse(xml);
 XmlDeclaration xmlDeclaration = (XmlDeclaration) parsedDocument.select("processing-instruction(").first();

 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
 assertEquals("!", xmlDeclaration.nodeName().charAt(0));
 assertFalse(xmlDeclaration.isProcessingInstruction());
 }
}