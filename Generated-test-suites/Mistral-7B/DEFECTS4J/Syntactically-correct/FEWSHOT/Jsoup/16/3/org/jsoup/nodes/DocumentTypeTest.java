package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNewDocumentType() {
        DocumentType docType = new DocumentType("html", "HTML TD DTD", "http://www.w3.org/TR/html4/strict.dtd", "/path/to/baseUri");
        assertSame("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML TD DTD", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
        assertEquals("/path/to/baseUri", docType.baseUri());
    }

    @Test
    public void testEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "", "/path/to/baseUri");
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertEquals("/path/to/baseUri", docType.baseUri());
    }

    @Test
    public void testEmptySystemId() {
        DocumentType docType = new DocumentType("html", "HTML TD DTD", "", "/path/to/baseUri");
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML TD DTD", docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertEquals("/path/to/baseUri", docType.baseUri());
    }

    @Test
    public void testNullBaseUri() {
        DocumentType docType = new DocumentType("html", "HTML TD DTD", "http://www.w3.org/TR/html4/strict.dtd", null);
        assertEquals("html", docType.attr("name"));
        assertEquals("HTML TD DTD", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "HTML TD DTD", "http://www.w3.org/TR/html4/strict.dtd", "/path/to/baseUri");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML TD DTD\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "http://www.w3.org/TR/html4/strict.dtd", "/path/to/baseUri");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadEmptySystemId() {
        DocumentType docType = new DocumentType("html", "HTML TD DTD", "", "/path/to/baseUri");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTML TD DTD\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "HTML TD DTD", "http://www.w3.org/TR/html4/strict.dtd", "/path/to/baseUri");
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }
}