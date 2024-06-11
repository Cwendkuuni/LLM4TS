package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.helper.StringUtil.isBlank;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr(NAME));
        assertEquals(publicId, docType.attr(PUBLIC_ID));
        assertEquals(systemId, docType.attr(SYSTEM_ID));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithoutAttributes() {
        String name = "html";
        String baseUri = "http://example.com/";

        DocumentType docType = new DocumentType(name, null, null, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr(NAME));
        assertNull(docType.attr(PUBLIC_ID));
        assertNull(docType.attr(SYSTEM_ID));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testHasAttribute() {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertTrue(docType.has(NAME));
        assertTrue(docType.has(PUBLIC_ID));
        assertTrue(docType.has(SYSTEM_ID));

        String emptyName = "";
        DocumentType emptyDocType = new DocumentType(emptyName, null, null, baseUri);

        assertFalse(emptyDocType.has(NAME));
        assertFalse(emptyDocType.has(PUBLIC_ID));
        assertFalse(emptyDocType.has(SYSTEM_ID));
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        Document doc = new Document();
        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
        doc.appendChild(docType);

        Appendable accumulator = new StringBuilder();
        doc.outerHtml(accumulator, null);

        String expected = "<!DOCTYPE html>\n";

        if (isBlank(publicId) && isBlank(systemId)) {
            expected = "<!doctype html>\n";
        }

        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        Document doc = new Document();
        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);
        doc.appendChild(docType);

        Appendable accumulator = new StringBuilder();
        doc.outerHtml(accumulator, null);

        assertEquals("", accumulator.toString().substring(docType.outerHtmlHead(accumulator, 0, null).length()));
    }
}