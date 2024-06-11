import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {
    
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        assertNotNull(docType);
    }
    
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        assertEquals("#doctype", docType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        StringBuilder sb = new StringBuilder();
        try {
            docType.outerHtmlHead(sb, 0, Document.OutputSettings.html());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertTrue(sb.toString().contains("<!DOCTYPE"));
    }
    
    @Test
    public void testHasMethod() {
        DocumentType docType = new DocumentType("html", "-//W3C//DTD HTML 4.01 Transitional//EN", "http://www.w3.org/TR/html4/loose.dtd", "http://example.com");
        assertTrue(docType.has(DocumentType.NAME));
        assertFalse(docType.has(DocumentType.PUBLIC_ID));
    }
}