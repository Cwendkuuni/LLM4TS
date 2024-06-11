import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {
    
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertNotNull(docType);
    }
    
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertEquals("#doctype", docType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder sb = new StringBuilder();
        try {
            docType.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        StringBuilder sb = new StringBuilder();
        try {
            docType.outerHtmlTail(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("HTML", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com");
        assertTrue(docType.has("name"));
        assertTrue(docType.has("publicId"));
        assertTrue(docType.has("systemId"));
    }
}