package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals("baseUri", documentType.baseUri());
    }

    @Test
    public void testEmptyConstructor() {
        DocumentType documentType = new DocumentType();
        assertNull(documentType.nodeName());
        assertNull(documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNameConstructor() {
        new DocumentType("", "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameConstructor() {
        new DocumentType(null, "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicIdConstructor() {
        new DocumentType("name", null, "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemIdConstructor() {
        new DocumentType("name", "publicId", null, "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriConstructor() {
        new DocumentType("name", "publicId", "systemId", null);
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accum.toString());
    }

    @Test
    public void testEmptyOuterHtmlHead() {
        DocumentType documentType = new DocumentType();
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().isEmpty());
    }
}