import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "HTML", "HTML 5.0", "http://www.w3.org/TR/html5/");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("HTML", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("HTML 5.0", docType.attr(DocumentType.SYSTEM_ID));
 assertEquals("http://www.w3.org/TR/html5/", docType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType docType = new DocumentType("", "", "", "");
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeHtmlOutput() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType docType = doc.head().documentType();
 String html = docType.outerHtml(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertTrue(html.contains("<!DOCTYPE html"));
 assertTrue(html.contains("PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""));
 assertTrue(html.contains("http://www.w3.org/TR/html4/loose.dtd\">"));
 }

 @Test
 public void testDocumentTypeInvalidHtmlOutput() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html SYSTEM \"invalid.dtd\">");
 DocumentType docType = doc.head().documentType();
 String html = docType.outerHtml(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertFalse(html.contains("<!DOCTYPE html"));
 assertFalse(html.contains("SYSTEM \"invalid.dtd\""));
 }

 @Test (expected = IllegalArgumentException.class)
 public void testDocumentTypeNullBaseUri() {
 DocumentType docType = new DocumentType("html", "HTML", "HTML 5.0", null);
 }
}