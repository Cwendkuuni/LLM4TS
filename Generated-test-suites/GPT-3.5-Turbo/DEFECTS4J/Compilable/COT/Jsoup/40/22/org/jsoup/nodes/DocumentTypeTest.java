package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        // Arrange
        String name = "doctype";
        String publicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        String baseUri = "http://www.example.com";

        // Act
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        // Assert
        Assert.assertEquals(name, documentType.attr("name"));
        Assert.assertEquals(publicId, documentType.attr("publicId"));
        Assert.assertEquals(systemId, documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        // Arrange
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");

        // Act
        String nodeName = documentType.nodeName();

        // Assert
        Assert.assertEquals("#doctype", nodeName);
    }

    @Test
    public void testOuterHtmlHead() {
        // Arrange
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        // Act
        documentType.outerHtmlHead(accum, depth, out);

        // Assert
        Assert.assertEquals("<!DOCTYPE doctype PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        // Arrange
        DocumentType documentType = new DocumentType("doctype", "-//W3C//DTD XHTML 1.0 Strict//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", "http://www.example.com");
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        // Act
        documentType.outerHtmlTail(accum, depth, out);

        // Assert
        Assert.assertEquals("", accum.toString());
    }
}
