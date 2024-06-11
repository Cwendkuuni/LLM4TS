import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        
        assertEquals("#doctype", docType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Document.Syntax.html);
        
        try {
            docType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!doctype doctype PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadHTML5() {
        DocumentType docType = new DocumentType("doctype", null, null, "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Document.Syntax.html);
        
        try {
            docType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!doctype doctype>", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadXML() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Document.Syntax.xml);
        
        try {
            docType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!DOCTYPE doctype PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadNoPublicId() {
        DocumentType docType = new DocumentType("doctype", null, "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Document.Syntax.html);
        
        try {
            docType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!doctype doctype \"systemId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadNoSystemId() {
        DocumentType docType = new DocumentType("doctype", "publicId", null, "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Document.Syntax.html);
        
        try {
            docType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!doctype doctype PUBLIC \"publicId\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadEmptyAttributes() {
        DocumentType docType = new DocumentType("doctype", null, null, "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Document.Syntax.html);
        
        try {
            docType.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!doctype doctype>", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings(Document.Syntax.html);
        
        docType.outerHtmlTail(accum, 0, out);
        
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("doctype", "publicId", "systemId", "baseUri");
        
        boolean hasName = docType.has("name");
        boolean hasPublicId = docType.has("publicId");
        boolean hasSystemId = docType.has("systemId");
        
        assertTrue(hasName);
        assertTrue(hasPublicId);
        assertTrue(hasSystemId);
    }
}