import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 // Test creating a DocumentType with valid arguments
 String name = "html";
 String publicId = "HTML_DOCTYPE";
 String systemId = "about:html";
 String baseUri = "http://example.com/";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

 // Test nodeName method
 assertEquals("#doctype", documentType.nodeName());

 // Test outerHTML method
 Document doc = Jsoup.parse("<html><head></head></html>", baseUri);
 doc.insertBefore(0, documentType);
 String expectedOuterHtml = "<!DOCTYPE html PUBLIC \"HTML_DOCTYPE\" about:html>";
 String actualOuterHtml = documentType.outerHtml();
 assertEquals(expectedOuterHtml, actualOuterHtml);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeInvalidPublicId() {
 String name = "html";
 String publicId = "";
 String systemId = "about:html";
 String baseUri = "http://example.com/";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeInvalidSystemId() {
 String name = "html";
 String publicId = "HTML_DOCTYPE";
 String systemId = "";
 String baseUri = "http://example.com/";
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 }

 @Test(expected = NullPointerException.class)
 public void testDocumentTypeNullBaseUri() {
 String name = "html";
 String publicId = "HTML_DOCTYPE";
 String systemId = "about:html";
 String baseUri = null;
 DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
 }

 @Test
 public void testDocumentTypeToString() {
 DocumentType documentType = new DocumentType("html", "HTML_DOCTYPE", "about:html", "http://example.com/");
 String expectedToString = "#doctype[name=html]";
 String actualToString = documentType.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testDocumentTypeEquals() {
 DocumentType documentType1 = new DocumentType("html", "HTML_DOCTYPE", "about:html", "http://example.com/");
 DocumentType documentType2 = new DocumentType("html", "HTML_DOCTYPE", "about:html", "http://example.com/");
 assertTrue(documentType1.equals(documentType2));
 }

 @Test
 public void testDocumentTypeHashCode() {
 DocumentType documentType1 = new DocumentType("html", "HTML_DOCTYPE", "about:html", "http://example.com/");
 DocumentType documentType2 = new DocumentType("html", "HTML_DOCTYPE", "about:html", "http://example.com/");
 assertEquals(documentType1.hashCode(), documentType2.hashCode());
 }

 @Test
 public void testDocumentTypeClone() {
 DocumentType documentType = new DocumentType("html", "HTML_DOCTYPE", "about:html", "http://example.com/");
 DocumentType clonedDocumentType = documentType.cloneNode(true);
 assertEquals(documentType, clonedDocumentType);
 }

 @Test
 public void testDocumentTypeChildNodes() {
 DocumentType documentType = new DocumentType("html", "HTML_DOCTYPE", "about:html", "http://example.com/");
 Element child = new Element("html");
 documentType.appendChild(child);
 assertNotNull(documentType.childNodes());
 assertFalse(documentType.childNodes().isEmpty());
 }

 @Test
 public void testDocumentTypeParentNode() {
 Document document = Jsoup.parse("<html><head><!-- Test --></head><body></body></html>", "http://example.com/");
 DocumentType documentType = (DocumentType) document.childNodes().get(0);
 Element parent = document.body();
 documentType.appendChild(new Element("div"));
 assertSame(parent, documentType.parentNode());
 }

 @Test
 public void testDocumentTypeNextSibling() {
 Document document = Jsoup.parse("<html><head><!-- Test --></head><body><div><!DOCTYPE html></div></body></html>", "http://example.com/");
 DocumentType documentType = (DocumentType) document.body().childNodes().get(1);
 Element nextSibling = document.body().childNodes().get(2);
 assertSame(nextSibling, documentType.nextSibling());
 }

 @Test
 public void testDocumentTypePreviousSibling() {
 Document document = Jsoup.parse("<html><head><!-- Test --></head><body><div><!DOCTYPE html></div><div></div></body></html>", "http://example.com/");
 DocumentType documentType = (DocumentType) document.body().childNodes().get(1);
 Element previousSibling = document.body().childNodes().get(0);
 assertSame(previousSibling, documentType.previousSibling());
 }

 @Test
 public void testDocumentTypeAttributes() {
 DocumentType documentType = new DocumentType("html", "HTML_DOCTYPE", "about:html", "http://example.com/");
 documentType.attr("data-test", "test-value");
 assertEquals("test-value", documentType.attr("data-test"));
 }

 @Test
 public void testDocumentTypeRemoveAttribute() {
 DocumentType documentType = new DocumentType("html", "HTML_DOCTYPE", "about:html", "http://example.com/");
 documentType.attr("data-test", "test-value");
 documentType.removeAttr("data-test");
 assertNull(documentType.attr("data-test"));
 }
}