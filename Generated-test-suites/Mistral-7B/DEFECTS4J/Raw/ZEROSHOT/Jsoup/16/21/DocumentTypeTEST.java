import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "baseUri");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML_PUBLIC_ID", docType.attr("publicId"));
 assertEquals("HTML_SYSTEM_ID", docType.attr("systemId"));
 assertEquals("baseUri", docType.baseUri());
 }

 @Test
 public void testDocumentTypeOuterHtmlHead() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();

 docType.outerHtmlHead(accum, 0, out);

 assertEquals("<!DOCTYPE htmlPUBLIC \"HTML_PUBLIC_ID\" \"HTML_SYSTEM_ID\">", accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();

 docType.outerHtmlTail(accum, 0, out);

 assertTrue(accum.toString().isEmpty());
 }

 @Test
 public void testDocumentTypeWithEmptyPublicIdAndSystemId() {
 DocumentType docType = new DocumentType("html", "", "", "baseUri");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("", docType.attr("publicId"));
 assertEquals("", docType.attr("systemId"));
 assertEquals("baseUri", docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithNullParameters() {
 DocumentType docType = new DocumentType(null, null, null, null);

 assertEquals("#doctype", docType.nodeName());
 assertNull(docType.attr("name"));
 assertNull(docType.attr("publicId"));
 assertNull(docType.attr("systemId"));
 assertNull(docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithOnlyName() {
 DocumentType docType = new DocumentType("html");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("", docType.attr("publicId"));
 assertEquals("", docType.attr("systemId"));
 assertEquals(null, docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithOnlyPublicId() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("HTML_PUBLIC_ID", docType.attr("publicId"));
 assertEquals("", docType.attr("systemId"));
 assertEquals(null, docType.baseUri());
 }

 @Test
 public void testDocumentTypeWithOnlySystemId() {
 DocumentType docType = new DocumentType("html", "", "HTML_SYSTEM_ID");

 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr("name"));
 assertEquals("", docType.attr("publicId"));
 assertEquals("HTML_SYSTEM_ID", docType.attr("systemId"));
 assertEquals(null, docType.baseUri());
 }

 @Test
 public void testDocumentTypeAttributes() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID", "HTML_SYSTEM_ID", "baseUri");
 Attributes attributes = docType.attributes();

 assertEquals(3, attributes.size());
 assertEquals("name", attributes.keyAt(0));
 assertEquals("html", attributes.valueAt(0));
 assertEquals("publicId", attributes.keyAt(1));
 assertEquals("HTML_PUBLIC_ID", attributes.valueAt(1));
 assertEquals("systemId", attributes.keyAt(2));
 assertEquals("HTML_SYSTEM_ID", attributes.valueAt(2));
 }

 @Test
 public void testDocumentTypeWithQuirksMode() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID_QUIRKS", "HTML_SYSTEM_ID_QUIRKS", "baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();

 docType.outerHtmlHead(accum, 0, out);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID_QUIRKS\" \"HTML_SYSTEM_ID_QUIRKS\">", accum.toString());
 }

 @Test
 public void testDocumentTypeWithQuirksModePublicIdEmpty() {
 DocumentType docType = new DocumentType("html", "", "HTML_SYSTEM_ID_QUIRKS", "baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();

 docType.outerHtmlHead(accum, 0, out);

 assertEquals("<!DOCTYPE html SYSTEM \"HTML_SYSTEM_ID_QUIRKS\">", accum.toString());
 }

 @Test
 public void testDocumentTypeWithQuirksModeSystemIdEmpty() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID_QUIRKS", "", "baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();

 docType.outerHtmlHead(accum, 0, out);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID_QUIRKS\">", accum.toString());
 }

 @Test
 public void testDocumentTypeWithQuirksModeBothEmpty() {
 DocumentType docType = new DocumentType("html", "", "", "baseUri");
 docType.attr("publicId", "HTML_PUBLIC_ID_QUIRKS");
 docType.attr("systemId", "HTML_SYSTEM_ID_QUIRKS");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();

 docType.outerHtmlHead(accum, 0, out);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID_QUIRKS\" \"HTML_SYSTEM_ID_QUIRKS\">", accum.toString());
 }

 @Test
 public void testDocumentTypeWithQuirksModePublicIdAndSystemId() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID_QUIRKS", "HTML_SYSTEM_ID_QUIRKS", "baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();

 docType.outerHtmlHead(accum, 0, out);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID_QUIRKS\" \"HTML_SYSTEM_ID_QUIRKS\">", accum.toString());
 }

 @Test
 public void testDocumentTypeWithQuirksModePublicIdOnly() {
 DocumentType docType = new DocumentType("html", "HTML_PUBLIC_ID_QUIRKS", "", "baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();

 docType.outerHtmlHead(accum, 0, out);

 assertEquals("<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID_QUIRKS\">", accum.toString());
 }

 @Test
 public void testDocumentTypeWithQuirksModeSystemIdOnly() {
 DocumentType docType = new DocumentType("html", "", "HTML_SYSTEM_ID_QUIRKS", "baseUri");
 StringBuilder accum = new StringBuilder();
 Document.OutputSettings out = new Document.OutputSettings();

 docType.outerHtmlHead(accum, 0, out);

 assertEquals("<!DOCTYPE html SYSTEM \"HTML_SYSTEM_ID_QUIRKS\">", accum.toString());
 }
}