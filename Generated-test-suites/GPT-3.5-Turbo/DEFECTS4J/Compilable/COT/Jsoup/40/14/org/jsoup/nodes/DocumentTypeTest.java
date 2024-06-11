package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {
    @Test
    public void testConstructor() {
        // Given
        String name = "doctype";
        String publicId = "-//W3C//DTD HTML 4.0//EN";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "https://www.example.com";

        // When
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        // Then
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        // Given
        String name = "doctype";
        String publicId = "-//W3C//DTD HTML 4.0//EN";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "https://www.example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        // When
        String nodeName = documentType.nodeName();

        // Then
        assertEquals("#doctype", nodeName);
    }

    @Test
    public void testOuterHtmlHead() {
        // Given
        String name = "doctype";
        String publicId = "-//W3C//DTD HTML 4.0//EN";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "https://www.example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = null;

        // When
        documentType.outerHtmlHead(accum, depth, out);

        // Then
        assertEquals("<!DOCTYPE doctype PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        // Given
        String name = "doctype";
        String publicId = "-//W3C//DTD HTML 4.0//EN";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "https://www.example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = null;

        // When
        documentType.outerHtmlTail(accum, depth, out);

        // Then
        assertEquals("", accum.toString());
    }
}
