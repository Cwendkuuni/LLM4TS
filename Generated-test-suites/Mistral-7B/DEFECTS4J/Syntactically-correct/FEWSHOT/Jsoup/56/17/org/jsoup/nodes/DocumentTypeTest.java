package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "HTML 4.01 Transitional", null, null);
        doc.appendChild(doctype);
        Element html = doc.getElementById("html");
        assertEquals(doctype, html.child(0));

        // Test with baseURI
        Document doc2 = new Document();
        DocumentType doctype2 = new DocumentType("html", "HTML 4.01 Transitional", null, "baseURI");
        doc2.setBaseURI("baseURI");
        doc2.appendChild(doctype2);
        Element html2 = doc2.getElementById("html");
        assertEquals(doctype2, html2.child(0));
        assertEquals("baseURI", doctype2.baseUri());
    }

    @Test
    public void testDocumentTypeNodeName() {
        DocumentType doctype = new DocumentType("html", "HTML 4.01 Transitional", null, null);
        assertEquals("#doctype", doctype.nodeName());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws IOException {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "HTML 4.01 Transitional", null, null);
        doc.appendChild(doctype);

        Appendable accum = new StringBuilder();
        doctype.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml());
        assertEquals("<!doctype html PUBLIC \"HTML 4.01 Transitional\" >", accum.toString());

        // Test with public ID and system ID
        DocumentType doctype2 = new DocumentType("html", "HTML 4.01 Transitional", "DTD HTML 4.01", null);
        doc.appendChild(doctype2);
        accum.setLength(0);
        doctype2.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml());
        assertEquals("<!DOCTYPE html PUBLIC \"DTD HTML 4.01\" \"DTD HTML 4.01.dtd\">", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() throws IOException {
        Document doc = new Document();
        DocumentType doctype = new DocumentType("html", "HTML 4.01 Transitional", null, null);
        doc.appendChild(doctype);
        Appendable accum = new StringBuilder();
        doctype.outerHtmlTail(accum, 0, Document.OutputSettings.syntaxHtml());
        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType doctype = new DocumentType("html", "HTML 4.01 Transitional", null, null);
        assertTrue(doctype.has(DocumentType.NAME));
        assertTrue(doctype.has(DocumentType.PUBLIC_ID));
        assertFalse(doctype.has("nonexistent"));

        DocumentType doctype2 = new DocumentType("html", "HTML 4.01 Transitional", "DTD HTML 4.01", null);
        assertTrue(doctype2.has(DocumentType.NAME));
        assertTrue(doctype2.has(DocumentType.PUBLIC_ID));
        assertTrue(doctype2.has(DocumentType.SYSTEM_ID));
    }
}