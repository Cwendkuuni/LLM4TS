
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType documentType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");
        Appendable accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        documentType.outerHtmlHead(accum, depth, outputSettings);
        
        String expected = "<!DOCTYPE DOCTYPE PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithHtml5Doctype() throws IOException {
        DocumentType documentType = new DocumentType("DOCTYPE", "", "", "baseUri");
        Appendable accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        
        documentType.outerHtmlHead(accum, depth, outputSettings);
        
        String expected = "<!doctype DOCTYPE>";
        assertEquals(expected, accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");
        Appendable accum = new StringBuilder();

        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("DOCTYPE", "publicId", "systemId", "baseUri");
        
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
        assertFalse(documentType.has("randomAttribute"));
    }
}
