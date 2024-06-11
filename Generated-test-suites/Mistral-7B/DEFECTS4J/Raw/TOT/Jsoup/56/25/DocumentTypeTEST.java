import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "HTML", "HTML", "http://www.w3.org/TR/html4/strict.dtd");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("HTML", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("HTML", docType.attr(DocumentType.SYSTEM_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("baseUri"));
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 DocumentType docType = new DocumentType("html", "DTD HTML", "html", "http://www.w3.org/TR/REC-html40/strict.dtd", "public");
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("DTD HTML", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("html", docType.attr(DocumentType.SYSTEM_ID));
 assertEquals("public", docType.attr(DocumentType.SYSTEMS_DECLARATION));
 assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", docType.attr("baseUri"));
 }

 @Test
 public void testDocumentTypeCreationWithoutPublicIdOrSystemId() {
 DocumentType docType = new DocumentType("html5", null, null, null);
 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr(DocumentType.PUBLIC_ID));
 assertNull(docType.attr(DocumentType.SYSTEM_ID));
 assertNull(docType.attr("baseUri"));
 }

 @Test
 public void testOuterHtmlHead() throws Exception {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType docType = doc.docType();
 Attributes attrs = docType.attributes();
 String expected = "<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";
 String actual = docType.outerHtml();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlHeadWithNoPublicIdOrSystemId() throws Exception {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType docType = doc.docType();
 String expected = "<!doctype html>";
 String actual = docType.outerHtml();
 assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testOuterHtmlHeadWithNullDocType() throws Exception {
 Document doc = null;
 DocumentType docType = doc.docType();
 docType.outerHtml();
 fail("IOException expected");
 }
}