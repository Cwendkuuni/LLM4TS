import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testNodeName() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", doctype.nodeName());
    }
    
    @Test
    public void testOuterHtmlHeadHtmlSyntax() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Syntax.html);
        
        try {
            doctype.outerHtmlHead(accum, 0, settings);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        assertEquals("<!doctype", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadNonHtmlSyntax() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Syntax.xml);
        
        try {
            doctype.outerHtmlHead(accum, 0, settings);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        assertEquals("<!DOCTYPE", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHeadWithAttributes() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        doctype.attr("attribute1", "value1");
        doctype.attr("attribute2", "value2");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.syntax(Syntax.html);
        
        try {
            doctype.outerHtmlHead(accum, 0, settings);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        assertEquals("<!doctype name PUBLIC \"publicId\" \"systemId\" attribute1=\"value1\" attribute2=\"value2\">", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        
        doctype.outerHtmlTail(accum, 0, settings);
        
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testHasWithBlankAttribute() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        boolean result = doctype.has("invalidAttribute");
        
        assertFalse(result);
    }
    
    @Test
    public void testHasWithNonBlankAttribute() {
        DocumentType doctype = new DocumentType("name", "publicId", "systemId", "baseUri");
        doctype.attr("attribute1", "value1");
        
        boolean result = doctype.has("attribute1");
        
        assertTrue(result);
    }
}