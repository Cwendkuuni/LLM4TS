import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor_ValidInputs_CreatesDocumentTypeObject() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.attr("systemId"));
        assertEquals("https://www.example.com", doctype.baseUri());
    }

    @Test
    public void testConstructor_EmptyName_ThrowsIllegalArgumentException() {
        try {
            DocumentType doctype = new DocumentType("", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("name must not be empty", e.getMessage());
        }
    }

    @Test
    public void testConstructor_NullPublicId_CreatesDocumentTypeObjectWithNullPublicId() {
        DocumentType doctype = new DocumentType("html", null, "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        
        assertEquals("html", doctype.attr("name"));
        assertNull(doctype.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.attr("systemId"));
        assertEquals("https://www.example.com", doctype.baseUri());
    }

    @Test
    public void testConstructor_NullSystemId_CreatesDocumentTypeObjectWithNullSystemId() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", null, "https://www.example.com");
        
        assertEquals("html", doctype.attr("name"));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.attr("publicId"));
        assertNull(doctype.attr("systemId"));
        assertEquals("https://www.example.com", doctype.baseUri());
    }

    @Test
    public void testNodeName_ReturnsDoctype() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testOuterHtmlHead_ReturnsExpectedOutput() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        doctype.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail_ReturnsEmptyString() {
        DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "https://www.example.com");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        doctype.outerHtmlTail(accum, 0, out);
        
        assertEquals("", accum.toString());
    }
}