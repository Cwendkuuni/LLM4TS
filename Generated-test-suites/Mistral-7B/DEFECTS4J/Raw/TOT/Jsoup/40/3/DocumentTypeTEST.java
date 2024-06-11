import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
 doc.appendChild(doctype);

 assertEquals("#doctype", doctype.nodeName());
 assertEquals("html", doctype.attr("name"));
 assertEquals("publicId1", doctype.attr("publicId"));
 assertEquals("systemId1", doctype.attr("systemId"));
 assertEquals("baseUri1", doctype.attributes().get("baseUri").getValue());
 }

 @Test
 public void testDocumentTypeEmptyAttributes() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", "", "", "");
 doc.appendChild(doctype);

 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr("name"));
 assertNull(doctype.attr("publicId"));
 assertNull(doctype.attr("systemId"));
 }

 @Test
 public void testDocumentTypeNullAttributes() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", null, null, null);
 doc.appendChild(doctype);

 assertEquals("#doctype", doctype.nodeName());
 assertNull(doctype.attr("name"));
 assertNull(doctype.attr("publicId"));
 assertNull(doctype.attr("systemId"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeEmptyName() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType(null, "publicId1", "systemId1", "baseUri1");

 assertNull(doctype);
 }

 @Test
 public void testDocumentTypeOuterHtml() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
 doc.appendChild(doctype);

 StringBuilder accum = new StringBuilder();
 doctype.outerHtmlHead(accum, 0, null);
 doctype.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html PUBLIC \"publicId1\" \"systemId1\">";
 assertEquals(expected, accum.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlEmptyAttributes() {
 Document doc = new Document();
 DocumentType doctype = new DocumentType("html", "", "", "");
 doc.appendChild(doctype);

 StringBuilder accum = new StringBuilder();
 doctype.outerHtmlHead(accum, 0, null);
 doctype.outerHtmlTail(accum, 0, null);

 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }
}