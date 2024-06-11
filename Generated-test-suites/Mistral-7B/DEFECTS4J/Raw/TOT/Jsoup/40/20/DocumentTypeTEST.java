import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType doctype = new DocumentType("html", "publicID", "systemID", document.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertNotNull(doctype.attr("name"));
 assertNotNull(doctype.attr("publicId"));
 assertNotNull(doctype.attr("systemId"));
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 Document document = new Document();
 DocumentType doctype = new DocumentType("", "", "", document.baseUri());

 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr("name"));
 assertNull(doctype.attr("publicId"));
 assertNull(doctype.attr("systemId"));
 }

 @Test
 public void testDocumentTypeOuterHtml() {
 Document document = new Document();
 DocumentType doctype = new DocumentType("html", "publicID", "systemID", document.baseUri());
 StringBuilder accum = new StringBuilder();

 doctype.outerHtmlHead(accum, 0, null);
 doctype.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html PUBLIC \"publicID\" \"systemID\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithEmptyAttributes() {
 Document document = new Document();
 DocumentType doctype = new DocumentType("html", "", "", document.baseUri());
 StringBuilder accum = new StringBuilder();

 doctype.outerHtmlHead(accum, 0, null);
 doctype.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyNameDocumentTypeCreation() {
 Document document = new Document();
 try {
 DocumentType doctype = new DocumentType("", "publicID", "systemID", document.baseUri());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }
}