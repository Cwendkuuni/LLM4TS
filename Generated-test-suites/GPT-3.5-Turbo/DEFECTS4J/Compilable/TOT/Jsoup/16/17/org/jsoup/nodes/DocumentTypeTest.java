package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DocumentTypeTest {

    @Test
    public void testConstructor_ValidInput_Success() {
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
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testNodeName_ReturnsDoctype() {
        // Arrange
        DocumentType documentType = new DocumentType("", "", "", "");

        // Act
        String result = documentType.nodeName();

        // Assert
        assertEquals("#doctype", result);
    }

    @Test
    public void testOuterHtmlHead_ValidInput_Success() {
        // Arrange
        DocumentType documentType = new DocumentType("", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "");

        // Act
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String result = accum.toString();

        // Assert
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">", result);
    }

    @Test
    public void testOuterHtmlTail_ValidInput_Success() {
        // Arrange
        DocumentType documentType = new DocumentType("", "", "", "");

        // Act
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        String result = accum.toString();

        // Assert
        assertEquals("", result);
    }

}