import static org.junit.Assert.*;
import org.junit.Test;

public class DocumentTypeTest {

    // Test the constructor
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        assertEquals("name", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals("baseUri", documentType.baseUri());
    }

    // Test the nodeName method
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        assertEquals("#doctype", documentType.nodeName());
    }

    // Test the outerHtmlHead method for HTML syntax
    @Test
    public void testOuterHtmlHeadHtmlSyntax() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        
        assertEquals("<!doctype", accum.toString());
    }

    // Test the outerHtmlHead method for other syntax
    @Test
    public void testOuterHtmlHeadOtherSyntax() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        StringBuilder accum = new StringBuilder();
        try {
            documentType.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
        
        assertEquals("<!DOCTYPE", accum.toString());
    }

    // Test the outerHtmlTail method
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        
        assertEquals("", accum.toString());
    }

    // Test the has method
    @Test
    public void testHas() {
        DocumentType documentType = new DocumentType("name", "publicId", "systemId", "baseUri");
        
        assertTrue(documentType.has("name"));
        assertTrue(documentType.has("publicId"));
        assertTrue(documentType.has("systemId"));
        assertFalse(documentType.has("invalidAttribute"));
    }
}