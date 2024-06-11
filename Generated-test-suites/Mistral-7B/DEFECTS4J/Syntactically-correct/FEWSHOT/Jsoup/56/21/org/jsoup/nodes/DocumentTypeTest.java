package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstruction() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(DocumentType.NAME));
        assertNotNull(documentType.attr(DocumentType.PUBLIC_ID));
        assertNotNull(documentType.attr(DocumentType.SYSTEM_ID));
        assertNull(documentType.parent());
        assertNull(documentType.nextSibling());
        assertNull(documentType.previousSibling());
    }

    @Test
    public void testDocumentTypeConstructionWithEmptyStringAttributes() {
        DocumentType documentType = new DocumentType("html", "", "", null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr(DocumentType.NAME));
        assertNull(documentType.attr(DocumentType.PUBLIC_ID));
        assertNull(documentType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeToString() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        document.appendChild(documentType);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", documentType.toString());
    }

    @Test
    public void testDocumentTypeToHtmlString() throws IOException {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        document.appendChild(documentType);

        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxHtml());
        documentType.outerHtmlTail(accumulator, 0, Document.OutputSettings.syntaxHtml());

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", accumulator.toString());
    }

    @Test
    public void testDocumentTypeWithoutPublicIdAndSystemId() throws IOException {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "", null);
        document.appendChild(documentType);

        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntaxHtml());
        documentType.outerHtmlTail(accumulator, 0, Document.OutputSettings.syntaxHtml());

        assertEquals("<!doctype html>", accumulator.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        assertTrue(documentType.has(DocumentType.NAME));
        assertTrue(documentType.has(DocumentType.PUBLIC_ID));
        assertTrue(documentType.has(DocumentType.SYSTEM_ID));

        documentType.attr(DocumentType.PUBLIC_ID, "");
        assertFalse(documentType.has(DocumentType.PUBLIC_ID));
    }

    @Test
    public void testCreateDocumentTypeNodeWithAttributes() {
        Document document = new Document();

        Element html = document.createElement("html");
        html.setAttribute("xmlns", "http://www.w3.org/1999/xhtml");
        document.appendChild(html);

        DocumentType doctype = new DocumentType("html", "publicId", "systemId", document);
        doctype.attr(DocumentType.PUBLIC_ID, "publicId");
        doctype.attr(DocumentType.SYSTEM_ID, "systemId");

        html.appendChild(doctype);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", doctype.outerHtml());
    }
}