import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import static org.jsoup.helper.StringUtil.isBlank;

public class DocumentTypeTest {

 private DocumentType createDocumentType(String name, String publicId, String systemId, String baseUri) {
 return new DocumentType(name, publicId, systemId, baseUri);
 }

 @Test
 public void testDocumentTypeConstructor() {
 DocumentType documentType = createDocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertEquals("DTD HTML 5", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html5/DTD/html5.dtd", documentType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 DocumentType documentType = createDocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);
 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, null);
 String expected = "<!DOCTYPE html PUBLIC \"DTD HTML 5\" \"http://www.w3.org/TR/html5/DTD/html5.dtd\">";
 assertEquals(expected, accumulator.toString());

 documentType = createDocumentType("html5", null, null, null);
 accumulator = new StringBuilder();
 documentType.outerHtmlHead(accumulator, 0, null);
 String html5Expected = "<!doctype html><!DOCTYPE html5>";
 assertEquals(html5Expected, accumulator.toString());
 }

 @Test
 public void testOuterHtmlTail() {
 DocumentType documentType = createDocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);
 Appendable accumulator = new StringBuilder();
 documentType.outerHtmlTail(accumulator, 0, null);
 assertEquals("", accumulator.toString());
 }

 @Test
 public void testHasAttribute() {
 DocumentType documentType = createDocumentType("html", "DTD HTML 5", "http://www.w3.org/TR/html5/DTD/html5.dtd", null);
 assertTrue(documentType.has(DocumentType.NAME));
 assertFalse(documentType.has("invalidAttribute"));

 documentType = createDocumentType("", "", "", null);
 assertTrue(documentType.has(DocumentType.NAME));
 assertFalse(documentType.has(DocumentType.PUBLIC_ID));
 assertFalse(documentType.has(DocumentType.SYSTEM_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 DocumentType documentType = new DocumentType(null, "", "", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPublicId() {
 DocumentType documentType = new DocumentType("html", null, "", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSystemId() {
 DocumentType documentType = new DocumentType("html", "", null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 DocumentType documentType = new DocumentType("html", "", "", null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullAppendable() {
 DocumentType documentType = createDocumentType("html", "", "", null);
 documentType.outerHtmlHead(null, 0, null);
 }
}