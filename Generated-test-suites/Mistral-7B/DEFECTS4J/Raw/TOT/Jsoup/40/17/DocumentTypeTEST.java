import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
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
 String publicId = "−//W3C//DTD HTML 4.01//EN";
 String systemId = "http://www.w3.org/TR/html4/strict.dtd";
 String baseUri = "http://example.com/";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals(name, documentType.attr("name"));
 assertEquals(publicId, documentType.attr("publicId"));
 assertEquals(systemId, documentType.attr("systemId"));
 assertEquals(baseUri, documentType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("", "", "", "");

 assertEquals("#doctype", documentType.nodeName());
 assertNotNull(documentType.attr("name"));
 assertNotNull(documentType.attr("publicId"));
 assertNotNull(documentType.attr("systemId"));
 assertNotNull(documentType.baseUri());
 }

 @Test
 public void testOuterHtmlHead() {
 StringBuilder accum = new StringBuilder();
 DocumentType documentType = new DocumentType("html", "−//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"−//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicIdAndSystemIdEmpty() {
 StringBuilder accum = new StringBuilder();
 DocumentType documentType = new DocumentType("html", "", "", "http://example.com/");
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html>", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithPublicIdEmpty() {
 StringBuilder accum = new StringBuilder();
 DocumentType documentType = new DocumentType("html", "http://www.w3.org/TR/html4/strict.dtd", "", "http://example.com/");
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 DocumentType documentType = new DocumentType(null, "−//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPublicId() {
 DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", "http://example.com/");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSystemId() {
 DocumentType documentType = new DocumentType("html", "−//W3C//DTD HTML 4.01//EN", null, "http://example.com/");
 }
}