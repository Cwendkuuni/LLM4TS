import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {
    
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr(DocumentType.SYSTEM_ID));
    }
    
    @Test
    public void testHasMethod() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        assertTrue(docType.has(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));
        assertFalse(docType.has("unknown"));
    }
    
    @Test
    public void testNodeNameMethod() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        assertEquals("#doctype", docType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadMethod() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        StringBuilder sb = new StringBuilder();
        try {
            docType.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTailMethod() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlTail(sb, 0, new Document.OutputSettings());
        
        assertEquals("", sb.toString());
    }
}