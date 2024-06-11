package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", "", document);
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeCreationWithSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "SYSTEM \"about:blank\"", document);
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("SYSTEM \"about:blank\"", documentType.attr("systemId"));
        assertNull(documentType.attr("publicId"));
    }

    @Test
    public void testDocumentTypeCreationWithAllAttributes() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", "SYSTEM \"about:blank\"", document);
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", documentType.attr("publicId"));
        assertEquals("SYSTEM \"about:blank\"", documentType.attr("systemId"));
    }

    @Test
    public void testOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", "", document);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", "", document);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "SYSTEM \"about:blank\"", document);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html SYSTEM \"about:blank\"", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBothIds() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", "SYSTEM \"about:blank\"", document);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" SYSTEM \"about:blank\"", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "", document);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testOuterHtmlWithPublicId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", "", document);
        Element html = new Element("html");
        html.appendChild(documentType);
        StringBuilder accum = new StringBuilder();
        document.outputSettings().prettyPrint(false);
        document.outputSettings().scriptingEnabled(false);
        document.outputSettings().indentAmount(0);
        document.outputSettings().metaCharset("UTF-8");
        document.outputSettings().encoding("UTF-8");
        document.appendBody();
        document.body().appendChild(html);
        document.body().appendChild(new TextNode("Content"));
        document.saveTo(accum);
        assertEquals("<html<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"><head><meta charset=\"UTF-8\"></head><body><textarea class=\"jsoup-textarea\"></textarea></body></html>", accum.toString());
    }

    @Test
    public void testOuterHtmlWithSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "SYSTEM \"about:blank\"", document);
        Element html = new Element("html");
        html.appendChild(documentType);
        StringBuilder accum = new StringBuilder();
        document.outputSettings().prettyPrint(false);
        document.outputSettings().scriptingEnabled(false);
        document.outputSettings().indentAmount(0);
        document.outputSettings().metaCharset("UTF-8");
        document.outputSettings().encoding("UTF-8");
        document.appendBody();
        document.body().appendChild(html);
        document.body().appendChild(new TextNode("Content"));
        document.saveTo(accum);
        assertEquals("<html<!DOCTYPE html SYSTEM \"about:blank\"<head><meta charset=\"UTF-8\"></head><body><textarea class=\"jsoup-textarea\"></textarea></body></html>", accum.toString());
    }

    @Test
    public void testOuterHtmlWithBothIds() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"", "SYSTEM \"about:blank\"", document);
        Element html = new Element("html");
        html.appendChild(documentType);
        StringBuilder accum = new StringBuilder();
        document.outputSettings().prettyPrint(false);
        document.outputSettings().scriptingEnabled(false);
        document.outputSettings().indentAmount(0);
        document.outputSettings().metaCharset("UTF-8");
        document.outputSettings().encoding("UTF-8");
        document.appendBody();
        document.body().appendChild(html);
        document.body().appendChild(new TextNode("Content"));
        document.saveTo(accum);
        assertEquals("<html<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" SYSTEM \"about:blank\"><head><meta charset=\"UTF-8\"></head><body><textarea class=\"jsoup-textarea\"></textarea></body></html>", accum.toString());
    }
}