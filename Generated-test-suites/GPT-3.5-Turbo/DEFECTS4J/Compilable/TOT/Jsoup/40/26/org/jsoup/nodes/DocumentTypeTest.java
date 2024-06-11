package org.jsoup.nodes;
import org.jsoup.nodes.DocumentType;
import org.junit.Test;

import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testConstructor_ValidInput_SuccessfulCreation() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.0//EN";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "http://example.com";

        // Act
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        // Assert
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_EmptyName_ExceptionThrown() {
        // Arrange
        String name = "";
        String publicId = "-//W3C//DTD HTML 4.0//EN";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "http://example.com";

        // Act
        new DocumentType(name, publicId, systemId, baseUri);

        // Assert
        // Exception is expected to be thrown
    }

    @Test
    public void testNodeName_ReturnsDoctype() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.0//EN";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "http://example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        // Act
        String nodeName = documentType.nodeName();

        // Assert
        assertEquals("#doctype", nodeName);
    }

    @Test
    public void testOuterHtmlHead_ValidAttributes_ReturnsExpectedOutput() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.0//EN";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "http://example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        // Act
        documentType.outerHtmlHead(accum, depth, out);

        // Assert
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHead_EmptyAttributes_ReturnsExpectedOutput() {
        // Arrange
        String name = "html";
        DocumentType documentType = new DocumentType(name, "", "", "");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        // Act
        documentType.outerHtmlHead(accum, depth, out);

        // Assert
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail_ValidAttributes_ReturnsEmptyString() {
        // Arrange
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.0//EN";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "http://example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        // Act
        documentType.outerHtmlTail(accum, depth, out);

        // Assert
        assertEquals("", accum.toString());
    }
}