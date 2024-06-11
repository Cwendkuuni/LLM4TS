import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 assertEquals("#doctype", documentType.nodeName());
 assertNotNull(documentType.attr("name"));
 assertNotNull(documentType.attr("publicId"));
 assertNotNull(documentType.attr("systemId"));
 assertNotNull(documentType.attr("baseUri"));
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html");
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertNull(documentType.attr("baseUri"));
 }

 @Test
 public void testDocumentTypeOutputHtml() {
 Document document = Jsoup.parse("<!DOCTYPE html>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>");
 DocumentType documentType = document.docType();
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"" + documentType.attr("publicId") + '\"' + " \"" + documentType.attr("systemId") + '\"', accum.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlWithName() {
 Document document = Jsoup.parse("<!DOCTYPE html [<!ENTITY myEntity SYSTEM \"file:myfile.txt\">]<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>");
 DocumentType documentType = document.docType();
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html" + (documentType.attr("name") != null ? " " + documentType.attr("name") : "") + " [<!ENTITY myEntity SYSTEM \"file:myfile.txt\">]", accum.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlWithPublicIdAndSystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"My Public Id\" \"My System Id\">\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>");
 DocumentType documentType = document.docType();
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"My Public Id\" \"My System Id\"", accum.toString());
 }

 @Test
 public void testDocumentTypeOutputHtmlWithAllAttributes() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"My Public Id\" \"My System Id\" [<!ENTITY myEntity SYSTEM \"file:myfile.txt\">]>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n</body>\n</html>");
 DocumentType documentType = document.docType();
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html PUBLIC \"My Public Id\" \"My System Id\" " + (documentType.attr("name") != null ? "NAME \"" + documentType.attr("name") + "\"" : "") + " [" + (documentType.attr("publicId") != null ? "PUBLIC \"" + documentType.attr("publicId") + '\"' : "") + " " + (documentType.attr("systemId") != null ? "SYSTEM \"" + documentType.attr("systemId") + '\"' : "") + "]", accum.toString());
 }

 @Test
 public void testDocumentTypeWithNullAttributes() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = document.docType();
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 assertEquals("<!DOCTYPE html", accum.toString());
 }
}