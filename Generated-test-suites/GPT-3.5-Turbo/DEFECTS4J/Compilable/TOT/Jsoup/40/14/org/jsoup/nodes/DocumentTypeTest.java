package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor_ValidInput_CreatesDocumentTypeObject() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";

        // Act
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        // Assert
        assertNotNull(documentType);
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyName_ThrowsIllegalArgumentException() {
        // Arrange
        String name = "";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";

        // Act
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        // Assert
        // Expecting IllegalArgumentException to be thrown
    }

    @Test
    public void testNodeName_ReturnsDoctype() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        // Act
        String nodeName = documentType.nodeName();

        // Assert
        assertEquals("#doctype", nodeName);
    }

    @Test
    public void testOuterHtmlHead_ValidAttributeValues_ReturnsExpectedString() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        StringBuilder accum = new StringBuilder();

        // Act
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());

        // Assert
        String expectedOutput = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlHead_EmptyName_ReturnsExpectedString() {
        // Arrange
        String name = "";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        StringBuilder accum = new StringBuilder();

        // Act
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());

        // Assert
        String expectedOutput = "<!DOCTYPE PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        assertEquals(expectedOutput, accum.toString());
    }

    @Test
    public void testOuterHtmlTail_ValidInput_DoesNothing() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        String baseUri = "http://www.example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        StringBuilder accum = new StringBuilder();

        // Act
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());

        // Assert
        assertEquals("", accum.toString());
    }

}