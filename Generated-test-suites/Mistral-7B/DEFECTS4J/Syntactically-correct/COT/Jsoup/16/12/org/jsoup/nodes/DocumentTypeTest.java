import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 private DocumentType createDocumentType(String name, String publicId, String systemId, String baseUri) {
 return new DocumentType(name, publicId, systemId, baseUri);
 }

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = createDocumentType("html", "publicID", "systemID", "baseURI");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicID", documentType.attr("publicId"));
 assertEquals("systemID", documentType.attr("systemId"));
 assertEquals("baseURI", documentType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType documentType = createDocumentType("html", "", "", "baseURI");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertEquals("baseURI", documentType.baseUri());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNameDocumentTypeCreation() {
 DocumentType documentType = createDocumentType(null, "publicID", "systemID", "baseURI");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPublicIdDocumentTypeCreation() {
 DocumentType documentType = createDocumentType("html", null, "systemID", "baseURI");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSystemIdDocumentTypeCreation() {
 DocumentType documentType = createDocumentType("html", "publicID", null, "baseURI");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUriDocumentTypeCreation() {
 DocumentType documentType = createDocumentType("html", "publicID", "systemID", null);
 }

 @Test
 public void testOuterHtmlHead() {
 StringBuilder accum = new StringBuilder();
 DocumentType documentType = createDocumentType("html", "publicID", "systemID", "baseURI");
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE htmlPUBLIC \"publicID\" systemID>\n", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptyPublicId() {
 StringBuilder accum = new StringBuilder();
 DocumentType documentType = createDocumentType("html", "", "systemID", "baseURI");
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html>\n", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadWithEmptySystemId() {
 StringBuilder accum = new StringBuilder();
 DocumentType documentType = createDocumentType("html", "publicID", "", "baseURI");
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE htmlPUBLIC \"publicID\" \">\n", accum.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 StringBuilder accum = new StringBuilder();
 DocumentType documentType = createDocumentType("html", "publicID", "systemID", "baseURI");
 documentType.outerHtmlTail(accum, 0, null);
 assertEmpty(accum.toString());
 }

}