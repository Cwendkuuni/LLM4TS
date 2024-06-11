
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
 
    @Test
    public void testConstructor() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", dt.nodeName());
        assertTrue(dt.has("name"));
        assertTrue(dt.has("publicId"));
        assertTrue(dt.has("systemId"));
        assertFalse(dt.has("nonExistentAttribute"));
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        try {
            dt.outerHtmlHead(accum, 0, out);
            assertEquals("<!DOCTYPE name PUBLIC \"publicId\" \"systemId\">", accum.toString());
            
            accum.setLength(0);
            out.syntax(Syntax.html);
            dt.outerHtmlHead(accum, 0, out);
            assertEquals("<!doctype name PUBLIC \"publicId\" \"systemId\">", accum.toString());
            
            accum.setLength(0);
            dt = new DocumentType("name", "publicId", "", "baseUri");
            dt.outerHtmlHead(accum, 0, out);
            assertEquals("<!doctype name PUBLIC \"publicId\">", accum.toString());
            
            accum.setLength(0);
            dt = new DocumentType("name", "", "systemId", "baseUri");
            dt.outerHtmlHead(accum, 0, out);
            assertEquals("<!doctype name \"systemId\">", accum.toString());
            
            accum.setLength(0);
            dt = new DocumentType("name", "", "", "baseUri");
            dt.outerHtmlHead(accum, 0, out);
            assertEquals("<!doctype name>", accum.toString());
        } catch (IOException e) {
            fail("Unexpected IOException occurred");
        }
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType dt = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        dt.outerHtmlTail(accum, 0, out);
        assertEquals("", accum.toString());
    }
}
