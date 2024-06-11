import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor_nameEmpty() {
        String name = "";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";
        
        try {
            DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Name must not be empty", e.getMessage());
        }
    }

    @Test
    public void testDocumentTypeConstructor_publicIdEmpty() {
        String name = "html";
        String publicId = "";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";
        
        try {
            DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
        
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeConstructor_systemIdEmpty() {
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "";
        String baseUri = "http://www.example.com";
        
        try {
            DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
        
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeConstructor_allFieldsEmpty() {
        String name = "";
        String publicId = "";
        String systemId = "";
        String baseUri = "http://www.example.com";
        
        try {
            DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Name must not be empty", e.getMessage());
        }
    }

    @Test
    public void testNodeName() {
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";
        
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        
        assertEquals("#doctype", documentType.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead_blankAttrValues() {
        String name = "html";
        String publicId = "";
        String systemId = "";
        String baseUri = "http://www.example.com";
        
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlHead(accum, depth, out);
        
        assertEquals("<!DOCTYPE html>", accum.toString());
    }
    
    @Test
    public void testOuterHtmlHead_nonBlankAttrValues() {
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";
        
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        documentType.outerHtmlHead(accum, depth, out);
        
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">", accum.toString());
    }
}