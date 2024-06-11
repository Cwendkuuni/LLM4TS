import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.helper.ElementFilter;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationCreation() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", "baseUri", true);

 Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertEquals("#declaration", xmlProcessingInstruction.nodeName());
 }

 @Test
 public void testGetNodeName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", "baseUri", true);

 Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertEquals("#declaration", xmlProcessingInstruction.nodeName());
 }

 @Test
 public void testGetWholeDeclaration() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", "baseUri", false, "1.0");
 XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration("xml", "baseUri", false, null, "UTF-8");
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", "baseUri", true);

 Assert.assertEquals("xml", xmlDeclaration.name());
 Assert.assertEquals("xml version=\"1.0\"", xmlDeclarationWithVersion.getWholeDeclaration());
 Assert.assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclarationWithEncoding.getWholeDeclaration());
 Assert.assertEquals("xml", xmlProcessingInstruction.getWholeDeclaration());
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", "baseUri", false, "1.0");
 XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration("xml", "baseUri", false, null, "UTF-8");
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", "baseUri", true);

 Document document = new Document();

 String xmlDeclarationHtml = xmlDeclaration.outerHtmlHead(document.bodyAppendable(), 0, null);
 String xmlDeclarationWithVersionHtml = xmlDeclarationWithVersion.outerHtmlHead(document.bodyAppendable(), 0, null);
 String xmlDeclarationWithEncodingHtml = xmlDeclarationWithEncoding.outerHtmlHead(document.bodyAppendable(), 0, null);
 String xmlProcessingInstructionHtml = xmlProcessingInstruction.outerHtmlHead(document.bodyAppendable(), 0, null);

 Assert.assertEquals("<declaration xmlns:xml=\"http://www.w3.org/XML/1998/namespace\">", xmlDeclarationHtml);
 Assert.assertEquals("<declaration xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" version=\"1.0\">", xmlDeclarationWithVersionHtml);
 Assert.assertEquals("<declaration xmlns:xml=\"http://www.w3.org/XML/1998/namespace\" encoding=\"UTF-8\">", xmlDeclarationWithEncodingHtml);
 Assert.assertTrue(xmlProcessingInstructionHtml.startsWith("<?xml"));
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", "baseUri", true);

 Document document = new Document();

 String xmlDeclarationHtml = xmlDeclaration.outerHtml(document);
 String xmlProcessingInstructionHtml = xmlProcessingInstruction.outerHtml(document);

 Assert.assertEquals("", xmlDeclaration.outerHtmlTail(document.bodyAppendable(), 0, null).toString());
 Assert.assertEquals("", xmlProcessingInstruction.outerHtmlTail(document.bodyAppendable(), 0, null).toString());
 Assert.assertEquals(xmlDeclarationHtml, xmlDeclaration.toString());
 Assert.assertTrue(xmlProcessingInstructionHtml.startsWith("<?xml"));
 }

 @Test
 public void testAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", "baseUri", false, "1.0");
 XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration("xml", "baseUri", false, null, "UTF-8");
 XmlDeclaration xmlProcessingInstruction = new XmlDeclaration("xml", "baseUri", true);

 Attributes attributes = xmlDeclarationWithVersion.attributes();
 Attributes attributesWithEncoding = xmlDeclarationWithEncoding.attributes();

 Assert.assertEquals(0, xmlDeclaration.attributes().size());
 Assert.assertEquals(2, xmlDeclarationWithVersion.attributes().size());
 Assert.assertEquals("version", xmlDeclarationWithVersion.attributes().keySet().iterator().next());
 Assert.assertEquals("1.0", xmlDeclarationWithVersion.attributes().get("version").toString());
 Assert.assertEquals("encoding", attributesWithEncoding.keySet().iterator().next());
 Assert.assertEquals("UTF-8", attributesWithEncoding.get("encoding").toString());
 Assert.assertEquals(0, xmlProcessingInstruction.attributes().size());
 }
}