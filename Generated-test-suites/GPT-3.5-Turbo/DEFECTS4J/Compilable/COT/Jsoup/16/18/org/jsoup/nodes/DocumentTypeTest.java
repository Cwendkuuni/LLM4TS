package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.1//EN";
        String systemId = "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd";
        String baseUri = "http://www.example.com";

        // Act
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        // Assert
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        // Arrange
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.1//EN", "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd", "http://www.example.com");

        // Act
        String nodeName = documentType.nodeName();

        // Assert
        assertEquals("#doctype", nodeName);
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        // Arrange
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.1//EN", "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd", "http://www.example.com");

        // Act
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());

        // Assert
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        // Arrange
        DocumentType documentType = new DocumentType("html", null, "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd", "http://www.example.com");

        // Act
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());

        // Assert
        assertEquals("<!DOCTYPE html http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        // Arrange
        DocumentType documentType = new DocumentType("html", "-//W3C//DTD XHTML 1.1//EN", "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd", "http://www.example.com");

        // Act
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());

        // Assert
        assertEquals("", accum.toString());
    }
}
