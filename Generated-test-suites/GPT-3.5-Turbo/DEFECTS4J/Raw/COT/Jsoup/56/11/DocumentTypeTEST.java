
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        String name = "doctype";
        String publicId = "-//W3C//DTD HTML 4.0//EN";
        String systemId = "http://www.w3.org/TR/htm40/strict.dtd";
        String baseUri = "http://example.com";
        
        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
        
        // Check that the attributes are set correctly
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        
        // Check that base URI is set correctly
        assertEquals(baseUri, docType.baseUri());
    }
    
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("", "", "", "");
        
        // Check that the node name is correct
        assertEquals("#doctype", docType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        DocumentType docType1 = new DocumentType("html", "", "", "");
        DocumentType docType2 = new DocumentType("html", "-//W3C//DTD HTML 4.0//EN", "", "");
        DocumentType docType3 = new DocumentType("html", "", "http://www.w3.org/TR/htm40/strict.dtd", "");
        
        String expectedOutput1 = "<!doctype html>";
        String expectedOutput2 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\">";
        String expectedOutput3 = "<!DOCTYPE html \"http://www.w3.org/TR/htm40/strict.dtd\">";
        
        // Check that the outer HTML is correct for different scenarios
        assertEquals(expectedOutput1, docType1.outerHtml());
        assertEquals(expectedOutput2, docType2.outerHtml());
        assertEquals(expectedOutput3, docType3.outerHtml());
    }
}
