import org.junit.Test;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {

    // Test for the constructor
    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");

        // Check if the attributes are set correctly
        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.0//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/REC-html40/strict.dtd", docType.attr("systemId"));
        assertEquals("http://www.example.com", docType.baseUri());
    }

    // Test for the nodeName() method
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
        assertEquals("#doctype", docType.nodeName());
    }

    // Test for the outerHtmlHead() method
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");

        // Test when syntax is HTML and it doesn't have publicId and systemId
        assertEquals("<!doctype html>", docType.outerHtmlHead());

        // Test when syntax is not HTML and it has name, publicId, and systemId
        docType.attr("name", "html");
        docType.attr("publicId", "-//W3C//DTD HTML 4.0//EN");
        docType.attr("systemId", "http://www.w3.org/TR/REC-html40/strict.dtd");
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", docType.outerHtmlHead());
    }

    // Test for the outerHtmlTail() method
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");
        assertEquals("", docType.outerHtmlTail());
    }

    // Test for the has() method
    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "http://www.w3.org/TR/REC-html40/strict.dtd", "http://www.example.com");

        // Test when attribute exists
        assertTrue(docType.has("name"));
        assertTrue(docType.has("publicId"));
        assertTrue(docType.has("systemId"));

        // Test when attribute is empty or doesn't exist
        assertFalse(docType.has("emptyAttribute"));
    }
}