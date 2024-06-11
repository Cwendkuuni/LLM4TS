import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "", document.baseUri());

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadEmptyPublicId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "", "systemId", document.baseUri());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html SYSTEM \"systemId\">", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadEmptySystemId() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "", document.baseUri());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicId\">", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document document = new Document();
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", document.baseUri());

 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, null);

 assertTrue(accum.toString().isEmpty());
 }
}