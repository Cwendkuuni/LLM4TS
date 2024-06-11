package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

/**
 * JUnit 4 test file for DocumentType class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "HTML";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "https://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeNodeName() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "HTML", "http://www.w3.org/TR/REC-html40/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML\" http://www.w3.org/TR/REC-html40/strict.dtd>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", "", "http://www.w3.org/TR/REC-html40/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "HTML", null, null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithBothEmpty() {
        DocumentType documentType = new DocumentType("html", "", "", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testDocumentTypeOuterHtmlHeadWithNullAccumulator() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        documentType.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        String expected = "";
        assertEquals(expected, accum.toString());
    }
}