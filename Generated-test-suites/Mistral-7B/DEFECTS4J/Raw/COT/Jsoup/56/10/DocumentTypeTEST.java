import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;

public class DocumentTypeTest {

 private Document createDocument() throws IOException {
 return org.jsoup.Jsoup.parse("<html><!DOCTYPE html>\n<body></body></html>");
 }

 @Test
 public void testDocumentTypeConstructor() {
 DocumentType docType = new DocumentType("html", "HTML", "HTML", null);
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeConstructorWithAttributes() {
 DocumentType docType = new DocumentType("html", "HTML", "HTML", null, "publicID", "systemID");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("publicID", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 Document doc = createDocument();
 DocumentType docType = doc.head().child(0);
 String expected = "<!DOCTYPE html>";
 String actual = "";
 docType.outerHtmlHead(new StringBuilder(actual), 0, Document.OutputSettings.syntaxHtml5());
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlHeadWithPublicIdAndSystemId() throws Exception {
 Document doc = createDocument();
 Element head = doc.head();
 head.appendElement("meta");
 DocumentType docType = head.add("!DOCTYPE", DocumentType.PUBLIC, "HTML", "DTD HTML 1.0", null, null);
 String expected = "<!DOCTYPE HTML PUBLIC \"DTD HTML 1.0\" \"DTD/HTML10.dtd\">";
 String actual = "";
 docType.outerHtmlHead(new StringBuilder(actual), 0, Document.OutputSettings.syntaxHtml4Transitional());
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOuterHtmlHeadWithNullOutSettings() {
 DocumentType docType = new DocumentType("html", "HTML", "HTML", null);
 StringWriter stringWriter = new StringWriter();
 docType.outerHtmlHead(stringWriter, 0, null);
 }

 @Test
 public void testHasAttribute() {
 DocumentType docType = new DocumentType("html", "HTML", "HTML", null);
 assertTrue(docType.has(DocumentType.NAME));
 assertFalse(docType.has("unknown"));

 Document doc = createDocument();
 docType = doc.head().child(0);
 assertTrue(docType.has(DocumentType.NAME));
 assertTrue(docType.has(DocumentType.PUBLIC_ID));
 assertTrue(docType.has(DocumentType.SYSTEM_ID));
 }
}