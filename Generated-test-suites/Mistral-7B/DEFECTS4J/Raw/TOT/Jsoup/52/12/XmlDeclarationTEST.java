import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclaration() {
 // Test case for creating a new XML declaration with valid parameters
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("<?xml>\n", xmlDeclaration.getWholeDeclaration());

 // Test case for creating a new XML declaration with encoding attribute
 XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration("xml", "baseUri", false, null, "UTF-8");
 assertEquals("<?xml encoding=\"UTF-8\">\n", xmlDeclarationWithEncoding.getWholeDeclaration());

 // Test case for creating a new XML declaration with version attribute
 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration("xml", "baseUri", false, null, null, "1.0");
 assertEquals("<?xml version=\"1.0\">\n", xmlDeclarationWithVersion.getWholeDeclaration());

 // Test case for creating a new XML declaration with both version and encoding attributes
 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration("xml", "baseUri", false, null, null, "1.0", "UTF-8");
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">\n", xmlDeclarationWithVersionAndEncoding.getWholeDeclaration());

 // Test case for creating a new XML declaration as processing instruction
 XmlDeclaration xmlDeclarationProcessingInstruction = new XmlDeclaration("xml", "baseUri", true);
 assertEquals("<?xml>\n", xmlDeclarationProcessingInstruction.getWholeDeclaration());

 // Test case for getting name of XML declaration with no attributes
 XmlDeclaration xmlDeclarationNoAttributes = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("xml", xmlDeclarationNoAttributes.name());

 // Test case for getting name of XML declaration with attributes
 XmlDeclaration xmlDeclarationWithAttributes = new XmlDeclaration("xml", "baseUri", false, null, "version", "1.0", "encoding", "UTF-8");
 assertEquals("xml", xmlDeclarationWithAttributes.name());

 // Test case for getting unencoded XML declaration with no attributes
 XmlDeclaration xmlDeclarationNoAttributeDeclaration = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("<?xml>\n", xmlDeclarationNoAttributeDeclaration.getWholeDeclaration());

 // Test case for getting unencoded XML declaration with attributes
 XmlDeclaration xmlDeclarationWithAttributeDeclaration = new XmlDeclaration("xml", "baseUri", false, null, "version", "1.0", "encoding", "UTF-8");
 assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">\n", xmlDeclarationWithAttributeDeclaration.getWholeDeclaration());

 // Test case for toString method
 XmlDeclaration xmlDeclarationToString = new XmlDeclaration("xml", "baseUri", false);
 assertEquals("<?xml>\n", xmlDeclarationToString.toString());

 // Test case for outerHtml method
 Document document = Jsoup.parse("", Parser.xmlParser());
 document.appendElement(xmlDeclarationToString);
 Appendable accumulator = new StringBuffer();
 xmlDeclarationToString.outerHtmlHead(accumulator, 0, null);
 document.body().appendElement(xmlDeclarationToString);
 xmlDeclarationToString.outerHtmlTail(accumulator, 0, null);
 assertEquals("<?xml>\n", accumulator.toString());
 }

 // Test case for constructor with null name
 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationWithNullName() {
 new XmlDeclaration(null, "baseUri", false);
 }

 // Test case for constructor with null baseUri
 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationWithNullBaseUri() {
 new XmlDeclaration("xml", null, false);
 }

 // Test case for constructor with invalid isProcessingInstruction value
 @Test(expected = IllegalArgumentException.class)
 public void testXmlDeclarationWithInvalidIsProcessingInstruction() {
 new XmlDeclaration("xml", "baseUri", "invalidValue");
 }
}