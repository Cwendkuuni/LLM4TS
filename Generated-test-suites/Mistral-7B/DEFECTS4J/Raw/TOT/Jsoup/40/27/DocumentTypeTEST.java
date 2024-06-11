import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.attributes.Attributes;
import static org.junit.jupiter.api.Assertions.*;

public class DocumentTypeTest {

 @Test
 void testDocumentTypeCreation() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType documentType = document.docType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNotNull(documentType.attr("publicId"));
 assertNotNull(documentType.attr("systemId"));
 }

 @Test
 void testDocumentTypeCreationWithEmptyName() {
 Document document = Jsoup.parse("<!DOCTYPE []>");
 DocumentType documentType = document.docType();

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 }

 @Test
 void testDocumentTypeCreationWithEmptyPublicId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"\" \"http://www.w3.org/TR/html4/loose.dtd\">");
 DocumentType documentType = document.docType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNotNull(documentType.attr("systemId"));
 }

 @Test
 void testDocumentTypeCreationWithEmptySystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"\">");
 DocumentType documentType = document.docType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNotNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 }

 @Test
 void testDocumentTypeCreationWithQuirksMode() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
 DocumentType documentType = document.docType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNotNull(documentType.attr("publicId"));
 assertNotNull(documentType.attr("systemId"));

 Element quirksModeElement = document.getElementsByTag("html")
 .first();
 Attributes quirksModeAttr = quirksModeElement.attributes();

 assertTrue(quirksModeAttr.getBoolean("xml:lang", false));
 }

 @Test
 void testDocumentTypeCreationWithInvalidAttributes() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\" foobar=\"bar\">");
 DocumentType documentType = document.docType();

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertFalse(documentType.hasAttr("foobar"));
 }
}