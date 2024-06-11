import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "HTML TF", "html", "http://www.w3.org/TR/html4/strict.dtd");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("HTML TF", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("html", docType.attr(DocumentType.SYSTEM_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr(DocumentType.BASE_URI));
 }

 @Test
 public void testDocumentTypeOutputHtml() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML TF", "html", "http://www.w3.org/TR/html4/strict.dtd");
 doc.appendChild(docType);

 Equipment outputSettings = doc.outputSettings().syntax(Document.OutputSettings.Syntax.html);
 String html = doc.outerHtml();

 // Test HTML5 doctype
 assertNotNull(html.substring(0, 18).contains("<!doctype"));
 assertNotNull(html.substring(18, 33).contains("html"));

 // Test non-HTML doctype
 outputSettings = doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 html = doc.outerHtml();
 assertNotNull(html.substring(0, 16).contains("<!DOCTYPE"));
 assertNotNull(html.substring(16, 29).contains("html"));
 }

 @Test
 public void testDocumentTypeOutputXml() throws Exception {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "HTML TF", "html", "http://www.w3.org/TR/html4/strict.dtd");
 doc.appendChild(docType);

 Equipment outputSettings = doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
 String xml = doc.outerHtml();

 // Test HTML5 doctype
 assertNull(xml.substring(0, 18).contains("<!doctype")); // No doctype declaration for XML

 // Test non-HTML doctype
 outputSettings = doc.outputSettings().syntax(Document.OutputSettings.Syntax.html);
 html = doc.outerHtml();
 assertNotNull(html.substring(0, 16).contains("<!DOCTYPE"));
 assertNotNull(html.substring(16, 29).contains("html"));
 }

 @Test
 public void testDocumentTypeAttributes() {
 DocumentType docType = new DocumentType("html", "HTML TF", "html", "http://www.w3.org/TR/html4/strict.dtd");

 // Test valid attributes
 docType.attr(DocumentType.NAME, "html");
 assertEquals("html", docType.attr(DocumentType.NAME));

 docType.attr(DocumentType.PUBLIC_ID, "HTML TF");
 assertEquals("HTML TF", docType.attr(DocumentType.PUBLIC_ID));

 docType.attr(DocumentType.SYSTEM_ID, "html");
 assertEquals("html", docType.attr(DocumentType.SYSTEM_ID));

 docType.attr(DocumentType.BASE_URI, "http://www.w3.org/TR/html4/strict.dtd");
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr(DocumentType.BASE_URI));

 // Test invalid attributes
 try {
 docType.attr("invalid", "attribute");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testDocumentTypeWithEmptyAttributes() {
 DocumentType docType = new DocumentType("html", null, null, null);

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr(DocumentType.NAME));
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertNull(docType.attr(DocumentType.BASE_URI));
 }
}