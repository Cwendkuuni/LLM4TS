package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        String name = "html";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testEmptyConstructor() {
        DocumentType documentType = new DocumentType();

        assertNull(documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
        assertNull(documentType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "", "", "");

        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);

        assertEquals("<!DOCTYPE html>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        DocumentType documentType = new DocumentType("html", "publicID", "", "");

        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        DocumentType documentType = new DocumentType("html", "", "systemID", "");

        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);

        assertEquals("<!DOCTYPE html SYSTEM \"systemID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", "");

        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();

        documentType.outerHtmlHead(accum, depth, out);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"systemID\">", accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNameInConstructor() {
        String name = "";
        String publicId = "publicID";
        String systemId = "systemID";
        String baseUri = "http://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertThrows(IllegalArgumentException.class, () -> documentType.outerHtmlHead(new StringBuilder(), 0, new Document.OutputSettings()));
    }

    @Test
    public void testSetName() {
        DocumentType documentType = new DocumentType();

        String newName = "newName";

        documentType.attr("name", newName);

        assertEquals(newName, documentType.attr("name"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetEmptyName() {
        DocumentType documentType = new DocumentType();

        documentType.attr("name", "");

        assertThrows(IllegalArgumentException.class, () -> documentType.outerHtmlHead(new StringBuilder(), 0, new Document.OutputSettings()));
    }

    @Test(expected = NullPointerException.class)
    public void testNullBaseUri() {
        DocumentType documentType = new DocumentType();

        documentType.baseUri(null);

        assertThrows(NullPointerException.class, () -> documentType.outerHtmlHead(new StringBuilder(), 0, new Document.OutputSettings()));
    }

    @Test
    public void testSetBaseUri() {
        DocumentType documentType = new DocumentType();

        String baseUri = "http://example.com";

        documentType.baseUri(baseUri);

        assertEquals(baseUri, documentType.baseUri());
    }
}