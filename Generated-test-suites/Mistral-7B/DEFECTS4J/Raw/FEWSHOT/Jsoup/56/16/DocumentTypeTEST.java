package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.jsoup.helper.StringUtil.*;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document doc = new Document();
        DocumentType dt = new DocumentType("html", "HTML", "HTML", doc.baseUri());
        assertTrue(dt.nodeName().equals("#doctype"));
        assertEquals("html", dt.attr(DocumentType.NAME));
        assertFalse(dt.attr(DocumentType.PUBLIC_ID) == null);
        assertFalse(dt.attr(DocumentType.SYSTEM_ID) == null);
        assertEquals("HTML", dt.attr(DocumentType.PUBLIC_ID));
        assertEquals("HTML", dt.attr(DocumentType.SYSTEM_ID));
        assertEquals("", dt.outString());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyStringAttributes() {
        Document doc = new Document();
        DocumentType dt = new DocumentType("html", "", "", doc.baseUri());
        assertTrue(dt.nodeName().equals("#doctype"));
        assertEquals("html", dt.attr(DocumentType.NAME));
        assertNull(dt.attr(DocumentType.PUBLIC_ID));
        assertNull(dt.attr(DocumentType.SYSTEM_ID));
        assertEquals("", dt.outString());
    }

    @Test
    public void testDocumentTypeCreationWithOnlyPublicID() {
        Document doc = new Document();
        DocumentType dt = new DocumentType("html", "PUBLIC ID", "", doc.baseUri());
        assertTrue(dt.nodeName().equals("#doctype"));
        assertEquals("html", dt.attr(DocumentType.NAME));
        assertFalse(dt.attr(DocumentType.SYSTEM_ID) == null);
        assertEquals("PUBLIC ID", dt.attr(DocumentType.PUBLIC_ID));
        assertNull(dt.attr(DocumentType.SYSTEM_ID));
        assertEquals("<!DOCTYPE html PUBLIC ID \"PUBLIC ID\">", dt.outString());
    }

    @Test
    public void testDocumentTypeCreationWithOnlySystemID() {
        Document doc = new Document();
        DocumentType dt = new DocumentType("html", "", "SYSTEM ID", doc.baseUri());
        assertTrue(dt.nodeName().equals("#doctype"));
        assertEquals("html", dt.attr(DocumentType.NAME));
        assertNull(dt.attr(DocumentType.PUBLIC_ID));
        assertFalse(dt.attr(DocumentType.SYSTEM_ID) == null);
        assertEquals("SYSTEM ID", dt.attr(DocumentType.SYSTEM_ID));
        assertEquals("<!DOCTYPE html SYSTEM \"SYSTEM ID\">", dt.outString());
    }

    @Test
    public void testDocumentTypeCreationWithPublicAndSystemIDs() {
        Document doc = new Document();
        DocumentType dt = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", doc.baseUri());
        assertTrue(dt.nodeName().equals("#doctype"));
        assertEquals("html", dt.attr(DocumentType.NAME));
        assertFalse(dt.attr(DocumentType.PUBLIC_ID) == null);
        assertEquals("PUBLIC ID", dt.attr(DocumentType.PUBLIC_ID));
        assertFalse(dt.attr(DocumentType.SYSTEM_ID) == null);
        assertEquals("SYSTEM ID", dt.attr(DocumentType.SYSTEM_ID));
        assertEquals("<!DOCTYPE html PUBLIC \"PUBLIC ID\" SYSTEM \"SYSTEM ID\">", dt.outString());
    }

    @Test
    public void testDocumentTypeToString() {
        Document doc = new Document();
        DocumentType dt = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", doc.baseUri());
        assertEquals("<!DOCTYPE html PUBLIC \"PUBLIC ID\" SYSTEM \"SYSTEM ID\">", dt.toString());
    }

    @Test
    public void testOuterHtmlHead() {
        Document doc = new Document();
        DocumentType dt = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", doc.baseUri());
        Appendable accum = new StringBuilder();

        dt.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml());
        assertEquals("<!DOCTYPE html PUBLIC \"PUBLIC ID\" SYSTEM \"SYSTEM ID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithHtml5Syntax() {
        Document doc = new Document();
        DocumentType dt = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", doc.baseUri());
        Appendable accum = new StringBuilder();

        dt.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml5());
        assertEquals("<!doctype html PUBLIC \"PUBLIC ID\" SYSTEM \"SYSTEM ID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Document doc = new Document();
        DocumentType dt = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", doc.baseUri());
        Appendable accum = new StringBuilder();

        dt.outerHtmlTail(accum, 0, Document.OutputSettings.syntaxHtml());
    }

    @Test
    public void testHasAttribute() {
        Document doc = new Document();
        DocumentType dt = new DocumentType("html", "PUBLIC ID", "SYSTEM ID", doc.baseUri());

        assertTrue(dt.has(DocumentType.NAME));
        assertTrue(dt.has(DocumentType.PUBLIC_ID));
        assertTrue(dt.has(DocumentType.SYSTEM_ID));

        assertFalse(dt.has("ERROR"));
    }
}