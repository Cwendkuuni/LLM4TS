import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(DocumentType.NAME));
 assertEquals("publicID", docType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", docType.attr(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeHtmlOutput() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html>");
 DocumentType docType = doc.documentType();

 Attributes attrs = docType.attributes();
 assertEquals("html", attrs.get(DocumentType.NAME));
 assertEquals("", attrs.get(DocumentType.PUBLIC_ID));
 assertEquals("", attrs.get(DocumentType.SYSTEM_ID));

 Appendable accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!doctype html>", accum.toString());
 }

 @Test
 public void testDocumentTypeWithPublicId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"xhtml1-strict.dtd\">");
 DocumentType docType = doc.documentType();

 Attributes attrs = docType.attributes();
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", attrs.get(DocumentType.NAME));
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", attrs.get(DocumentType.PUBLIC_ID));
 assertEquals("", attrs.get(DocumentType.SYSTEM_ID));

 Appendable accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"xhtml1-strict.dtd\">", accum.toString());
 }

 @Test
 public void testDocumentTypeWithSystemId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html SYSTEM \"xhtml1.dtd\">");
 DocumentType docType = doc.documentType();

 Attributes attrs = docType.attributes();
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", attrs.get(DocumentType.NAME));
 assertEquals("", attrs.get(DocumentType.PUBLIC_ID));
 assertEquals("xhtml1.dtd", attrs.get(DocumentType.SYSTEM_ID));

 Appendable accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!DOCTYPE html SYSTEM \"xhtml1.dtd\">", accum.toString());
 }

 @Test
 public void testDocumentTypeWithNamePublicIdAndSystemId() throws IOException {
 Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"xhtml1-strict.dtd\">");
 DocumentType docType = doc.documentType();

 Attributes attrs = docType.attributes();
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", attrs.get(DocumentType.NAME));
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", attrs.get(DocumentType.PUBLIC_ID));
 assertEquals("xhtml1-strict.dtd", attrs.get(DocumentType.SYSTEM_ID));

 Appendable accum = new StringBuilder();
 docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 assertEquals("<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"xhtml1-strict.dtd\">", accum.toString());
 }

 @Test (expected = IOException.class)
 public void testDocumentTypeWithNullBaseUri() {
 DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
 }
}