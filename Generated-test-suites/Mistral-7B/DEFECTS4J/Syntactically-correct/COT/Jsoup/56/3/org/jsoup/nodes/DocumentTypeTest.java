package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

/**
 * Test class for DocumentType class.
 */
public class DocumentTypeTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://www.w3.org/TR/html5/");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertEquals("HTML", docType.attr(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));
        assertEquals("HTML5", docType.attr(DocumentType.SYSTEM_ID));
        assertEquals("http://www.w3.org/TR/html5/", docType.baseUri());
    }

    /**
     * Test constructor with empty publicID and systemID.
     */
    @Test
    public void testDocumentTypeConstructorEmptyIDs() {
        DocumentType docType = new DocumentType("html", "", "", "http://www.w3.org/TR/html5/");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertFalse(docType.has(DocumentType.PUBLIC_ID));
        assertFalse(docType.has(DocumentType.SYSTEM_ID));
        assertEquals("http://www.w3.org/TR/html5/", docType.baseUri());
    }

    /**
     * Test nodeName method.
     */
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://www.w3.org/TR/html5/");
        assertEquals("#doctype", docType.nodeName());
    }

    /**
     * Test outerHtmlHead method with HTML syntax and no publicID or systemID.
     */
    @Test
    public void testOuterHtmlHeadHTMLNoIDs() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "", "", "http://www.w3.org/TR/html5/");
        Appendable accum = doc.appendBody().appendElement(docType);
        doc.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!doctype html>", accum.toString());
    }

    /**
     * Test outerHtmlHead method with HTML syntax and publicID and systemID.
     */
    @Test
    public void testOuterHtmlHeadHTMLWithIDs() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"", "XHTML1-strict.dtd", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        Appendable accum = doc.appendBody().appendElement(docType);
        doc.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }

    /**
     * Test outerHtmlHead method with non-HTML syntax and no publicID or systemID.
     */
    @Test
    public void testOuterHtmlHeadNonHTMLNoIDs() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("xml", "", "", "http://www.w3.org/XML/1998/XMLSchema.xsd");
        Appendable accum = doc.appendBody().appendElement(docType);
        doc.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE xml>", accum.toString());
    }

    /**
     * Test outerHtmlHead method with non-HTML syntax and publicID and systemID.
     */
    @Test
    public void testOuterHtmlHeadNonHTMLWithIDs() throws IOException {
        Document doc = new Document();
        DocumentType docType = new DocumentType("xml", "XML PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"", "XHTML1-strict.dtd", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        Appendable accum = doc.appendBody().appendElement(docType);
        doc.outputSettings(Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
        docType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE xml PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", accum.toString());
    }

    /**
     * Test outerHtmlTail method.
     */
    @Test
    public void testOuterHtmlTail() throws IOException {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://www.w3.org/TR/html5/");
        Appendable accum = null; // test without output
        docType.outerHtmlTail(accum, 0, null);
        // no output expected
    }

    /**
     * Test has method.
     */
    @Test
    public void testHas() {
        DocumentType docType = new DocumentType("html", "HTML", "HTML5", "http://www.w3.org/TR/html5/");
        assertTrue(docType.has(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));
        assertFalse(docType.has("nonExistentAttribute"));
    }
}