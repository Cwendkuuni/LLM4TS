import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 }

 @Test
 public void testDocumentTypeName() {
 DocumentType documentType = document.documentType();
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testDocumentTypeAttributes() {
 DocumentType documentType = document.documentType();
 assertEquals("html", documentType.attr("name"));
 assertNotNull(documentType.attr("publicId"));
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", documentType.attr("publicId"));
 assertNotNull(documentType.attr("systemId"));
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", documentType.attr("systemId"));
 }

 @Test
 public void testDocumentTypeEmptyName() {
 DocumentType documentType = new DocumentType("", "", "", "");
 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeNullName() {
 DocumentType documentType = new DocumentType(null, "", "", "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeNullPublicId() {
 DocumentType documentType = new DocumentType("html", null, "", "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeNullSystemId() {
 DocumentType documentType = new DocumentType("html", "", null, "");
 }
}