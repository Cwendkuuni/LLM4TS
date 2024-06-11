import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
 assertEquals("#doctype", docType.nodeName());
 assertNotNull(docType.attr("name"));
 assertNotNull(docType.attr("publicId"));
 assertNotNull(docType.attr("systemId"));
 assertNotNull(docType.attr("baseUri"));
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html");
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.attr("baseUri"));
 }

 @Test
 public void testDocumentTypeOuterHtml() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
 doc.appendChild(docType);

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html");
 if (!StringUtil.isBlank(docType.attr("publicId"))) {
 expected.append(" PUBLIC ");
 expected.append("\"").append(docType.attr("publicId")).append('"');
 }
 if (!StringUtil.isBlank(docType.attr("systemId"))) {
 expected.append(" ");
 expected.append("\"").append(docType.attr("systemId")).append('"');
 }
 expected.append('>');

 StringBuilder actual = new StringBuilder();
 doc.outerHtml(actual, 0, null);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 try {
 new DocumentType(null, "publicId", "systemId", "baseUri");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyPublicId() {
 try {
 new DocumentType("html", "", "systemId", "baseUri");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptySystemId() {
 try {
 new DocumentType("html", "publicId", "", "baseUri");
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testDocumentTypeOuterHtmlWithNoAttributes() {
 Document doc = new Document();
 DocumentType docType = new DocumentType("html");
 doc.appendChild(docType);

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html>");
 StringBuilder actual = new StringBuilder();
 doc.outerHtml(actual, 0, null);
 assertEquals(expected.toString(), actual.toString());
 }
}