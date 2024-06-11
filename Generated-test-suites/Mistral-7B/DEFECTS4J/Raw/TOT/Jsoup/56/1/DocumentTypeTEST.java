import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attribute;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setup() {
 document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 }

 @Test
 public void testDocumentTypeName() {
 DocumentType docType = document.doctype();
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(NAME));
 }

 @Test
 public void testDocumentTypeAttributes() {
 DocumentType docType = document.doctype();

 assertEquals("html", docType.attr(NAME));
 assertNotNull(docType.attr(PUBLIC_ID));
 assertNotNull(docType.attr(SYSTEM_ID));
 assertEquals("transitional", docType.attr(PUBLIC_ID).split(" ")[1]);
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr(SYSTEM_ID));
 }

 @Test
 public void testEmptyDocumentType() {
 DocumentType docType = new DocumentType("#", "", "", "");
 assertEquals("#doctype", docType.nodeName());
 assertFalse(docType.has(NAME));
 assertFalse(docType.has(PUBLIC_ID));
 assertFalse(docType.has(SYSTEM_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 DocumentType docType = new DocumentType(null, "", "", "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPublicId() {
 DocumentType docType = new DocumentType("#", null, "", "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullSystemId() {
 DocumentType docType = new DocumentType("#", "", null, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullBaseUri() {
 DocumentType docType = new DocumentType("#", "", "", null);
 }
}