import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 String name = "html";
 String publicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
 String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
 String baseUri = "http://example.com";
 DocumentType doctype = new DocumentType(name, publicId, systemId, baseUri);

 assertEquals("#doctype", doctype.nodeName());
 assertEquals(name, doctype.attr(NAME));
 assertEquals(publicId, doctype.attr(PUBLIC_ID));
 assertEquals(systemId, doctype.attr(SYSTEM_ID));
 assertEquals(baseUri, doctype.baseUri());
 }

 @Test
 public void testDocumentTypeCreationWithEmptyAttributes() {
 String name = "html";
 DocumentType doctype = new DocumentType(name, null, null, null);

 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr(PUBLIC_ID));
 assertNull(doctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeOutputHtmlHead() throws Exception {
 DocumentType doctype = new DocumentType("html", null, null, null);
 String expected = "<!doctype html>";

 Appendable accum = new StringBuffer();
 doctype.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlHeadWithPublicId() throws Exception {
 DocumentType doctype = new DocumentType("html", "public Id", null, null);
 String expected = "<!DOCTYPE html PUBLIC \"public Id\"";

 Appendable accum = new StringBuffer();
 doctype.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlHeadWithSystemId() throws Exception {
 DocumentType doctype = new DocumentType("html", null, "system Id", null);
 String expected = "<!DOCTYPE html SYSTEM \"system Id\"";

 Appendable accum = new StringBuffer();
 doctype.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlHeadWithNamePublicIdAndSystemId() throws Exception {
 DocumentType doctype = new DocumentType("html", "Public Id", "System Id", null);
 String expected = "<!DOCTYPE html PUBLIC \"Public Id\" System \"System Id\"";

 Appendable accum = new StringBuffer();
 doctype.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlHeadWithEmptyAttributes() throws Exception {
 DocumentType doctype = new DocumentType("html", null, null, null);
 String expected = "<!doctype html>";

 Appendable accum = new StringBuffer();
 doctype.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlTail() throws Exception {
 DocumentType doctype = new DocumentType("html", null, null, null);

 Appendable accum = new StringBuffer();
 doctype.outerHtmlTail(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

 assertNull(accum.toString());
 }

 @Test
 public void testDocumentTypeHasAttribute() {
 DocumentType doctype = new DocumentType("html", "Public Id", "System Id", null);

 assertTrue(doctype.has(DocumentType.NAME));
 assertTrue(doctype.has(DocumentType.PUBLIC_ID));
 assertTrue(doctype.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeHasNoAttribute() {
 DocumentType doctype = new DocumentType("html", null, null, null);

 assertFalse(doctype.has(DocumentType.PUBLIC_ID));
 assertFalse(doctype.has(DocumentType.SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeSetAttribute() {
 DocumentType doctype = new DocumentType("html", null, null, null);
 doctype.attr(DocumentType.PUBLIC_ID, "Public Id");

 assertEquals("Public Id", doctype.attr(DocumentType.PUBLIC_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeSetInvalidAttribute() {
 DocumentType doctype = new DocumentType("html", null, null, null);
 doctype.attr("invalidAttribute", "value");
 }
}