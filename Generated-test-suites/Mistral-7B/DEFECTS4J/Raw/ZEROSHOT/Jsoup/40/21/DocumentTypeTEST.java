import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 }

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = document.doctype();

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr("publicId"));
 assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr("systemId"));
 assertNotNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 StringBuilder accum = new StringBuilder();
 DocumentType docType = document.doctype();
 docType.outerHtmlHead(accum, 0, null);

 assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType docType = document.doctype();
 StringBuilder accum = new StringBuilder();
 docType.outerHtmlTail(accum, 0, null);

 assertEquals("", accum.toString());
 }
}