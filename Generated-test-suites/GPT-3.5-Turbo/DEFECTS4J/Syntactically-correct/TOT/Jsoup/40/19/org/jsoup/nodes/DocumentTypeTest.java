
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor_ValidParameters_AllAttributesSetCorrectly() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01//EN";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";
        
        // Act
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        
        // Assert
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyName_IllegalArgumentExceptionThrown() {
        // Arrange
        String name = "";
        String publicId = "-//W3C//DTD HTML 4.01//EN";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";
        
        // Act
        new DocumentType(name, publicId, systemId, baseUri);
    }
    
    @Test
    public void testNodeName_ReturnsDoctype() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01//EN";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        
        // Act
        String nodeName = documentType.nodeName();
        
        // Assert
        assertEquals("#doctype", nodeName);
    }
    
    @Test
    public void testOuterHtmlHead_ValidAttributes_CorrectOutput() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01//EN";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        StringBuilder stringBuilder = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        // Act
        documentType.outerHtmlHead(stringBuilder, depth, outputSettings);
        
        // Assert
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", stringBuilder.toString());
    }
    
    @Test
    public void testOuterHtmlHead_EmptyAttributes_CorrectOutput() {
        // Arrange
        String name = "html";
        String baseUri = "http://example.com";
        DocumentType documentType = new DocumentType(name, "", "", baseUri);
        StringBuilder stringBuilder = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        // Act
        documentType.outerHtmlHead(stringBuilder, depth, outputSettings);
        
        // Assert
        assertEquals("<!DOCTYPE html>", stringBuilder.toString());
    }
    
    @Test
    public void testOuterHtmlTail_ValidAttributes_CorrectOutput() {
        // Arrange
        String name = "html";
        String baseUri = "http://example.com";
        DocumentType documentType = new DocumentType(name, "", "", baseUri);
        StringBuilder stringBuilder = new StringBuilder();
        int depth = 0;
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        // Act
        documentType.outerHtmlTail(stringBuilder, depth, outputSettings);
        
        // Assert
        assertEquals("", stringBuilder.toString());
    }
}

