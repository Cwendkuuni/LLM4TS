import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "HTML_Public_ID";
 String systemId = "HTML_System_ID";
 String baseUri = "http://example.com";

 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri().originalText());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 DocumentType docType = new DocumentType("html", "HTML_Public_ID", "HTML_System_ID", "http://example.com");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE htmlPUBLIC \"HTML_Public_ID\" HTML_System_ID>", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "", "", "http://example.com");
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);

 assertEquals("", accum.toString());
 }

 @Test
 public void testDocumentTypeWithPublicIdAndSystemId() {
 DocumentType docType = new DocumentType("html", "HTML_Public_ID", "HTML_System_ID", "http://example.com");
 Element element = document.createElement("html");
 element.appendElement(docType);

 Attributes attributes = element.attributes();
 assertEquals("PUBLIC \"HTML_Public_ID\" HTML_System_ID", attributes.get("doctype").toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeWithNullName() {
 DocumentType docType = new DocumentType(null, "HTML_Public_ID", "HTML_System_ID", "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeWithEmptyName() {
 DocumentType docType = new DocumentType("", "HTML_Public_ID", "HTML_System_ID", "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeWithNullPublicId() {
 DocumentType docType = new DocumentType("html", null, "HTML_System_ID", "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeWithNullSystemId() {
 DocumentType docType = new DocumentType("html", "HTML_Public_ID", null, "http://example.com");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeWithNullBaseUri() {
 DocumentType docType = new DocumentType("html", "HTML_Public_ID", "HTML_System_ID", null);
 }
}