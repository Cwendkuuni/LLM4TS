package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

/**
 * Test class for DocumentType class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr(DocumentType.NAME));
        assertEquals(publicId, documentType.attr(DocumentType.PUBLIC_ID));
        assertEquals(systemId, documentType.attr(DocumentType.SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() throws Exception {
        Document document = new Document();
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accumulator = new StringBuffer();
        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxHtml());

        String expected = "<!doctype html>";
        assertEquals(expected, accumulator.toString());

        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxXhtml());

        expected = "<!DOCTYPE html>";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testOuterHtmlTail() throws Exception {
        Document document = new Document();

        DocumentType documentType = new DocumentType("#doctype", null, null, null);

        Appendable accumulator = new StringBuffer();
        documentType.outerHtmlTail(accumulator, 0, Document.OutputSettings.syntaxHtml());

        // No output for html and xhtml
        assertEquals("", accumulator.toString());

        documentType.outerHtmlTail(accumulator, 0, Document.OutputSettings.syntaxXml());

        // No output for xml
        assertEquals("", accumulator.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("#doctype", null, null, null);

        Attributes attributes = documentType.attributes();

        documentType.attr(DocumentType.NAME, "html");
        assertTrue(documentType.has(DocumentType.NAME));
        assertFalse(documentType.has("nonexistent"));

        documentType.attr(DocumentType.PUBLIC_ID, "publicId");
        documentType.attr(DocumentType.SYSTEM_ID, "systemId");

        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));

        assertFalse(documentType.has(DocumentType.NAME.toLowerCase()));
        assertFalse(documentType.has(DocumentType.SYSTEM_ID.toUpperCase()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        String publicId = "publicId";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        new DocumentType(null, publicId, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicId() {
        String name = "html";
        String systemId = "systemId";
        String baseUri = "http://example.com";

        new DocumentType(name, null, systemId, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemId() {
        String name = "html";
        String publicId = "publicId";
        String baseUri = "http://example.com";

        new DocumentType(name, publicId, null, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUri() {
        String name = "html";
        String publicId = "publicId";
        String systemId = "systemId";

        new DocumentType(name, publicId, systemId, null);
    }
}