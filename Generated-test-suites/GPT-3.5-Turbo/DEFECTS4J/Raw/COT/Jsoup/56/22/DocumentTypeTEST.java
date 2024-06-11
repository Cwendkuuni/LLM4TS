import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {
    
    @Test
    public void testConstructor() {
        // Test basic functionality of the constructor
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN",
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        
        assertEquals("html", docType.attr("name"));
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", docType.attr("systemId"));
        assertEquals("http://www.example.com", docType.baseUri());
    }
    
    @Test
    public void testNodeName() {
        // Test the node name
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN",
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        
        assertEquals("#doctype", docType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() throws IOException {
        // Test the outerHtmlHead method
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN",
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        
        // Set output settings to Syntax.html
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, out);
        
        String expected = "<!doctype html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(expected, sb.toString());
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testOuterHtmlHeadNoPublicIdNoSystemId() throws IOException {
        // Test the outerHtmlHead method with no publicId and systemId
        DocumentType docType = new DocumentType("html", null, null, "http://www.example.com");
        
        // Set output settings to Syntax.html
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.html);
        
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, out);
        
        String expected = "<!doctype html>";
        assertEquals(expected, sb.toString());
    }
    
    @Test
    public void testOuterHtmlHeadOtherSyntax() throws IOException {
        // Test the outerHtmlHead method with syntax other than Syntax.html
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN",
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        
        // Set output settings to Syntax.xml
        Document.OutputSettings out = new Document.OutputSettings();
        out.syntax(Document.OutputSettings.Syntax.xml);
        
        StringBuilder sb = new StringBuilder();
        docType.outerHtmlHead(sb, 0, out);
        
        String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(expected, sb.toString());
    }
    
    @Test
    public void testHas() {
        // Test the has method
        DocumentType docType = new DocumentType("html", "-//W3C//DTD XHTML 1.0 Transitional//EN",
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", "http://www.example.com");
        
        assertTrue(docType.has("name"));
        assertTrue(docType.has("publicId"));
        assertTrue(docType.has("systemId"));
        assertFalse(docType.has("someOtherAttribute"));
    }
}