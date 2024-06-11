package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.jsoup.helper.StringUtil.isBlank;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTMLTF", "http://www.w3.org/TR/html4/strict.dtd", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTMLTF", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
        assertNotNull(docType.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        DocumentType docType = new DocumentType("", "", "", null);
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtml() {
        DocumentType docType = new DocumentType("html", "HTMLTF", "http://www.w3.org/TR/html4/strict.dtd", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTMLTF\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());

        accum.setLength(0);
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testEmptyDocumentTypeOuterHtml() {
        DocumentType docType = new DocumentType("", "", "", null);

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE>", accum.toString());

        accum.setLength(0);
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeWithOnlyName() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());

        accum.setLength(0);
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeWithOnlyPublicId() {
        DocumentType docType = new DocumentType("html", "HTMLTF", null, null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTMLTF", docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTMLTF\">", accum.toString());

        accum.setLength(0);
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeWithOnlySystemId() {
        DocumentType docType = new DocumentType("html", null, "http://www.w3.org/TR/html4/strict.dtd", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
        assertNull(docType.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());

        accum.setLength(0);
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeWithAllAttributes() {
        DocumentType docType = new DocumentType("html", "HTMLTF", "http://www.w3.org/TR/html4/strict.dtd", "http://test.com");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("HTMLTF", docType.attr("publicId"));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr("systemId"));
        assertEquals("http://test.com", docType.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"HTMLTF\" \"http://www.w3.org/TR/html4/strict.dtd\">", accum.toString());

        accum.setLength(0);
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());

        accum.setLength(0);
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeWithNullAttributes() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());

        accum.setLength(0);
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeWithBlankAttributes() {
        DocumentType docType = new DocumentType("html", null, "", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertEquals("", docType.attr("systemId"));
        assertNull(docType.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>", accum.toString());

        accum.setLength(0);
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }
}