package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstruction() {
        DocumentType docType = new DocumentType("html", "HTML", null, null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertNull(docType.attr(DocumentType.PUBLIC_ID));
        assertNull(docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeConstructionWithPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "HTML", "publicId", "systemId");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertEquals("publicId", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("systemId", docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeConstructionWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html", "HTML", "", "");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertNull(docType.attr(DocumentType.PUBLIC_ID));
        assertNull(docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeConstructionWithNullBaseUri() {
        DocumentType docType = new DocumentType("html", "HTML", "", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertNull(docType.attr(DocumentType.PUBLIC_ID));
        assertNull(docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeConstructionWithBaseUri() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML", "", doc.baseUri());
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertNotNull(docType.attr(DocumentType.PUBLIC_ID));
        assertNotNull(docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML", "", doc.baseUri());

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

        assertEquals("<!DOCTYPE html>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithoutPublicIdAndSystemId() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML", "", doc.baseUri());

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html5));

        assertEquals("<!doctype html>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicId() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML", "publicId", doc.baseUri());

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\"\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithSystemId() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML", "", doc.baseUri());

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html, "http://example.com/"));

        assertEquals("<!DOCTYPE html SYSTEM \"http://example.com/HTML.dtd\">\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicIdAndSystemId() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML", "publicId", "systemId");

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" SYSTEM \"systemId\">\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "HTML", "", null);

        Appendable accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));

        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType docType = new DocumentType("html", "HTML", "publicId", "systemId");

        assertTrue(docType.has(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeHasNoAttribute() {
        DocumentType docType = new DocumentType("html", "HTML", "", "");

        assertFalse(docType.has("invalidAttribute"));
    }
}