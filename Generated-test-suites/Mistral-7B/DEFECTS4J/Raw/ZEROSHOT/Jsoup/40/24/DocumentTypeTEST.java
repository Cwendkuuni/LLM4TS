import org.junit.Test;
import org.junit.runner.RunWith;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;
import static org.junit.Assert.*;

@RunWith(org.junit.runner.JUnit4.class)
public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = new Document();
 DocumentType documentType = document.createElement("html", "", "");

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.outerHtml());

 Attributes attributes = documentType.attributes();
 assertNotNull(attributes);
 assertEquals("html", attributes.get("name"));
 assertEquals("", attributes.get("publicId"));
 assertEquals("", attributes.get("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithPublicId() {
 Document document = new Document();
 DocumentType documentType = document.createElement("html", "publicID", "systemID");

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.outerHtml());

 Attributes attributes = documentType.attributes();
 assertNotNull(attributes);
 assertEquals("html", attributes.get("name"));
 assertEquals("publicID", attributes.get("publicId"));
 assertEquals("systemID", attributes.get("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithSystemId() {
 Document document = new Document();
 DocumentType documentType = document.createElement("html", "", "systemID");

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.outerHtml());

 Attributes attributes = documentType.attributes();
 assertNotNull(attributes);
 assertEquals("html", attributes.get("name"));
 assertEquals("", attributes.get("publicId"));
 assertEquals("systemID", attributes.get("systemId"));
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 Document document = new Document();
 DocumentType documentType = document.createElement("html", "publicID", "systemID");

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.outerHtml());

 Attributes attributes = documentType.attributes();
 assertNotNull(attributes);
 assertEquals("html", attributes.get("name"));
 assertEquals("publicID", attributes.get("publicId"));
 assertEquals("systemID", attributes.get("systemId"));
 }

 @Test
 public void testDocumentTypeOuterHtml() {
 Document document = new Document();
 DocumentType documentType = document.createElement("html", "publicID", "systemID");

 String expected = "<!DOCTYPE html PUBLIC \"publicID\" \"systemID\">";
 assertEquals(expected, documentType.outerHtml());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithEmptyPublicId() {
 Document document = new Document();
 DocumentType documentType = document.createElement("html", "", "systemID");

 String expected = "<!DOCTYPE html SYSTEM \"systemID\">";
 assertEquals(expected, documentType.outerHtml());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithEmptySystemId() {
 Document document = new Document();
 DocumentType documentType = document.createElement("html", "publicID", "");

 String expected = "<!DOCTYPE html PUBLIC \"publicID\">";
 assertEquals(expected, documentType.outerHtml());
 }

 @Test
 public void testDocumentTypeOuterHtmlWithEmptyPublicIdAndSystemId() {
 Document document = new Document();
 DocumentType documentType = document.createElement("html");

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, documentType.outerHtml());
 }

 @Test
 public void testDocumentTypeToString() {
 Document document = new Document();
 DocumentType documentType = document.createElement("html", "publicID", "systemID");

 String expected = "[DocumentType html]";
 assertEquals(expected, documentType.toString());
 }

 @Test
 public void testDocumentTypeEquals() {
 Document document = new Document();
 DocumentType documentType1 = document.createElement("html", "publicID1", "systemID1");
 DocumentType documentType2 = document.createElement("html", "publicID2", "systemID2");

 assertNotEquals(documentType1, documentType2);

 DocumentType documentType3 = document.createElement("html", "publicID1", "systemID1");
 assertEquals(documentType1, documentType3);
 }

 @Test
 public void testDocumentTypeHashCode() {
 Document document = new Document();
 DocumentType documentType1 = document.createElement("html", "publicID1", "systemID1");
 DocumentType documentType2 = document.createElement("html", "publicID2", "systemID2");
 DocumentType documentType3 = document.createElement("html", "publicID1", "systemID1");

 assertEquals(documentType1.hashCode(), documentType3.hashCode());
 assertNotEquals(documentType1.hashCode(), documentType2.hashCode());
 }
}