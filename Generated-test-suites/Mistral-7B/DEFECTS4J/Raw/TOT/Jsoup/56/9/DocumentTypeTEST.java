import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" " +
 "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
 }

 @Test
 public void testDocumentTypeWithName() {
 DocumentType docType = document.documentType();
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(NAME));
 }

 @Test
 public void testDocumentTypeWithPublicIdAndSystemId() {
 DocumentType docType = document.documentType();
 assertEquals("#doctype", docType.nodeName());
 assertTrue(docType.has(PUBLIC_ID));
 assertTrue(docType.has(SYSTEM_ID));
 assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", docType.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", docType.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithOnlyName() {
 Document documentWithOnlyName = Jsoup.parse("<!DOCTYPE html>");
 DocumentType docType = documentWithOnlyName.documentType();
 assertEquals("#doctype", docType.nodeName());
 assertEquals("html", docType.attr(NAME));
 assertFalse(docType.has(PUBLIC_ID));
 assertFalse(docType.has(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeWithNoAttributes() {
 Document documentWithNoAttributes = Jsoup.parse("<!DOCTYPE>");
 DocumentType docType = documentWithNoAttributes.documentType();
 assertEquals("#doctype", docType.nodeName());
 assertFalse(docType.has(PUBLIC_ID));
 assertFalse(docType.has(SYSTEM_ID));
 assertFalse(docType.has(NAME));
 }

 @Test(expected = IllegalStateException.class)
 public void testSettingAttributeAfterCreation() {
 DocumentType docType = new DocumentType("html", null, null, null);
 docType.attr(NAME, "xhtml");
 }
}