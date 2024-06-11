import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "html";
 String systemId = "//www.w3.org/TR/REC-html40/strict.dtd";
 String baseUri = "https://www.example.com";
 DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", docType.nodeName());
 assertEquals(name, docType.attr("name"));
 assertEquals(publicId, docType.attr("publicId"));
 assertEquals(systemId, docType.attr("systemId"));
 assertEquals(baseUri, docType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType docType = new DocumentType("", "", "", "");
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeOutput() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "https://www.example.com");
 Element root = doc.parse("<!DOCTYPE html>");
 docType.outerHtmlHead(root.html(), 0, null);
 docType.outerHtmlTail(root.html(), 0, null);

 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 assertEquals(expected, root.html());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 String name = null;
 String publicId = "html";
 String systemId = "//www.w3.org/TR/REC-html40/strict.dtd";
 String baseUri = "https://www.example.com";

 try {
 new DocumentType(name, publicId, systemId, baseUri);
 } catch (IllegalArgumentException e) {
 assertEquals("name is marked non-null but is null", e.getMessage());
 throw e;
 }
 }
}