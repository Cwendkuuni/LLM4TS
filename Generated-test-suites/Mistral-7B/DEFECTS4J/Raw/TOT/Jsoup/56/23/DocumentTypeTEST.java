import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "DTD_HTML", "html.dtd", null);

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(NAME));
 assertNotNull(docType.attr(PUBLIC_ID));
 assertNotNull(docType.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithNoPublicIdAndNoSystemId() {
 DocumentType docType = new DocumentType("html5", null, null, null);

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr(PUBLIC_ID));
 assertNull(docType.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
 DocumentType docType = doc.documentType();

 Appendable accum = new StringBuffer();
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("<!doctype html", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithNoPublicIdAndNoSystemId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType docType = doc.documentType();

 Appendable accum = new StringBuffer();
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals("<!DOCTYPE html", accum.toString());
 }

 @Test(expected = IOException.class)
 public void testDocumentTypeOuterHtmlHeadWithInvalidSyntax() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType docType = doc.documentType();

 Appendable accum = new StringBuffer();
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
 }
}