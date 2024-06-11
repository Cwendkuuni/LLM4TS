import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    // Test cases from all experts
    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId1", "systemId1", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("publicId1", docType.attr("publicId"));
        assertEquals("systemId1", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        Document doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        DocumentType docType = doc.documentType();
        Attributes attributes = docType.attributes();
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", attributes.get("name"));
        assertEquals("public", attributes.get("publicId").substring(0, 6));
        assertEquals("//W3C//DTD XHTML 1.0 Strict//EN", attributes.get("systemId"));
    }

    @Test
    public void testDocumentTypeErrorScenarios() {
        try {
            new DocumentType("", "", "", "");
            fail("Should not be able to create DocumentType with empty name.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new DocumentType("name", "", "", null);
            fail("Should not be able to create DocumentType with null baseUri.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testDocumentTypeOuterHtmlEmpty() {
        Document doc = Jsoup.parse("<!DOCTYPE html>");
        DocumentType docType = doc.documentType();
        Attributes attributes = docType.attributes();
        assertEquals("#doctype", docType.nodeName());
        assertTrue(StringUtil.isBlank(attributes.get("publicId")));
        assertTrue(StringUtil.isBlank(attributes.get("systemId")));
    }
}