import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 }

 @Test
 public void testDocumentTypeName() {
 DocumentType docType = (DocumentType) document.head();
 assertEquals("#doctype", docType.nodeName());
 }

 @Test
 public void testDocumentTypePublicId() {
 DocumentType docType = (DocumentType) document.head();
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("publicId"));
 }

 @Test
 public void testDocumentTypeSystemId() {
 DocumentType docType = (DocumentType) document.head();
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeEmptyName() {
 DocumentType docType = new DocumentType("", "", "", "");
 assertEquals("#doctype", docType.nodeName());
 assertNotNull(docType.attr("name")); // Should be empty
 }

 @Test
 public void testDocumentTypeNullName() {
 try {
 new DocumentType(null, "", "", "");
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testDocumentTypeNullPublicId() {
 DocumentType docType = new DocumentType("Name", null, "", "");
 assertEquals("#doctype", docType.nodeName());
 assertNotNull(docType.attr("publicId")); // Should be empty
 }

 @Test
 public void testDocumentTypeNullSystemId() {
 DocumentType docType = new DocumentType("Name", "", null, "");
 assertEquals("#doctype", docType.nodeName());
 assertNotNull(docType.attr("systemId")); // Should be empty
 }
}