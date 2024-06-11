import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 /**
 * Test constructor with valid arguments.
 */
 @Test
 public void testXmlDeclarationConstructor() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(name, xmlDeclaration.name());
 assertEquals(baseUri, xmlDeclaration.baseUri());
 assertFalse(xmlDeclaration.isProcessingInstruction());
 }

 /**
 * Test constructor with null name.
 */
 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationConstructorNullName() {
 String name = null;
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 new XmlDeclaration(name, baseUri, isProcessingInstruction);
 }

 /**
 * Test Getter methods.
 */
 @Test
 public void testGetters() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = true;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals(name, xmlDeclaration.name());
 assertEquals(baseUri, xmlDeclaration.baseUri());
 assertTrue(xmlDeclaration.isProcessingInstruction());
 }

 /**
 * Test getWholeDeclaration method.
 */
 @Test
 public void testGetWholeDeclaration() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 assertEquals("<?xml?>", xmlDeclaration.getWholeDeclaration());

 String version = "1.0";
 XmlDeclaration xmlDeclarationWithVersion = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 xmlDeclarationWithVersion.attributes().put("version", version);
 assertEquals("<?xml version=\"" + version + "\"?>", xmlDeclarationWithVersion.getWholeDeclaration());

 String encoding = "UTF-8";
 XmlDeclaration xmlDeclarationWithEncoding = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 xmlDeclarationWithEncoding.attributes().put("encoding", encoding);
 assertEquals("<?xml encoding=\"" + encoding + "\"?>", xmlDeclarationWithEncoding.getWholeDeclaration());

 String versionAndEncoding = "1.0 UTF-8";
 XmlDeclaration xmlDeclarationWithVersionAndEncoding = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 xmlDeclarationWithVersionAndEncoding.attributes().put("version", version);
 xmlDeclarationWithVersionAndEncoding.attributes().put("encoding", encoding);
 assertEquals("<?xml version=\"" + version + "\" encoding=\"" + encoding + "\"?>", xmlDeclarationWithVersionAndEncoding.getWholeDeclaration());
 }

 /**
 * Test outerHtmlHead method.
 */
 @Test
 public void testOuterHtmlHead() throws IOException {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 StringBuilder expected = new StringBuilder();
 expected.append("<?").append(xmlDeclaration.getWholeDeclaration()).append(">");

 Appendable accum = document.bodyAppender();
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 assertEquals(expected.toString(), accum.toString());
 }

 /**
 * Test outerHtmlTail method.
 */
 @Test
 public void testOuterHtmlTail() throws IOException {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 Appendable accum = document.bodyAppender();
 xmlDeclaration.outerHtml(accum, 0, null);
 assertEquals("", xmlDeclaration.outerHtmlTail(accum, 0, null).toString());
 }

 /**
 * Test toString method.
 */
 @Test
 public void testToString() {
 String name = "xml";
 String baseUri = "http://example.com";
 boolean isProcessingInstruction = false;
 XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
 assertEquals(xmlDeclaration.getWholeDeclaration(), xmlDeclaration.toString());
 }
}