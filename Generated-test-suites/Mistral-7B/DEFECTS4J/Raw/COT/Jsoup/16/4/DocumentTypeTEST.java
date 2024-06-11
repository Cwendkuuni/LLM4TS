package org.jsoup.nodes.test;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        assertEquals("#doctype", documentType.nodeName());
        assertNotNull(documentType.attr("name"));
        assertNotNull(documentType.attr("publicId"));
        assertNotNull(documentType.attr("systemId"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "", "", null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "", "", null);
        StringBuilder accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 0, null);
        String expected = "<!DOCTYPE html>\n";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        documentType.outerHtmlHead(null, 0, null);
        // No assertions here as the method does not throw an exception when the accumulator is null
    }

    @Test
    public void testOuterHtmlHeadWithNullDocument() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        Document document = null;
        documentType.outerHtmlHead(new StringBuilder(), 0, document);
        // No assertions here as the method does not throw an exception when the document is null
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accumulator = new StringBuilder();
        documentType.outerHtmlTail(accumulator, 0, null);
        assertEquals("", accumulator.toString());
    }
}