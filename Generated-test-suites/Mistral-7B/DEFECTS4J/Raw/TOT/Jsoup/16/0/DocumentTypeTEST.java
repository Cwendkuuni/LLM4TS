import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document doc;

 @Before
 public void setUp() {
 doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 }

 @Test
 public void testDocumentTypeName() {
 DocumentType docType = doc.documentType();
 assertEquals("#doctype", docType.nodeName());
 }

 @Test
 public void testDocumentTypePublicId() {
 DocumentType docType = doc.documentType();
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", docType.attr("publicId"));
 }

 @Test
 public void testDocumentTypeSystemId() {
 DocumentType docType = doc.documentType();
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.attr("systemId"));
 }

 @Test(expected = NullPointerException.class)
 public void testEmptyDocumentType() {
 Document emptyDoc = Jsoup.parse("<html/>");
 DocumentType emptyDocType = emptyDoc.documentType();
 assertNull("DocumentType should be null for empty document", emptyDocType);
 }

 @Test
 public void testDocumentTypeWithEmptyPublicIdAndSystemId() {
 DocumentType docType = new DocumentType("#doctype", "", "", "");
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeWithNullArguments() {
 try {
 new DocumentType(null, null, null, null);
 fail("New DocumentType instance should throw NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testDocumentTypeWithNullName() {
 try {
 new DocumentType(null, "publicId", "systemId", "");
 fail("New DocumentType instance should throw NullPointerException");
 } catch (NullPointerException e) {
 // expected
 }
 }

 @Test
 public void testDocumentTypeWithNullPublicId() {
 DocumentType docType = new DocumentType("#doctype", null, "systemId", "");
 assertNull(docType.attr("publicId"));
 }

 @Test
 public void testDocumentTypeWithNullSystemId() {
 DocumentType docType = new DocumentType("#doctype", "publicId", null, "");
 assertNull(docType.attr("systemId"));
 }
}