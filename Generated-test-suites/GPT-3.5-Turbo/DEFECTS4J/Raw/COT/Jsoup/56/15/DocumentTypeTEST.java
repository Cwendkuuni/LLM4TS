
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        assertEquals("name", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
    }
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlHead(accum, 0, out);
        
        assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        documentType.outerHtmlTail(accum, 0, out);
        
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
    }
}
