package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "such.public.id";
        String systemId = "such.system.id";
        String baseUri = "http://www.example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        String name = "html";
        DocumentType documentType = new DocumentType(name, null, null, null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        String name = "html";
        String publicId = "such.public.id";
        String systemId = "such.system.id";
        String baseUri = "http://www.example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"such.public.id\" such.system.id>", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        String name = "html";
        String systemId = "such.system.id";
        String baseUri = "http://www.example.com";
        DocumentType documentType = new DocumentType(name, null, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html " + systemId, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
        String name = "html";
        String publicId = "such.public.id";
        String baseUri = "http://www.example.com";
        DocumentType documentType = new DocumentType(name, publicId, null, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"such.public.id\"", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyName() {
        String publicId = "such.public.id";
        String systemId = "such.system.id";
        String baseUri = "http://www.example.com";
        DocumentType documentType = new DocumentType(null, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        String name = "html";
        String publicId = "such.public.id";
        String systemId = "such.system.id";
        String baseUri = "http://www.example.com";
        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }
}