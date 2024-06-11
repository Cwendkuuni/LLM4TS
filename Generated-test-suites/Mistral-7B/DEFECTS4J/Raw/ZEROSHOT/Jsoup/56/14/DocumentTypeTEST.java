package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Before;
import org.junit.Test;

/**
 * Test for DocumentType class.
 */
public class DocumentTypeTest {

    private Document document;

    @Before
    public void setup() {
        document = new Document();
    }

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "HTML", "html", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertNull(docType.attr(DocumentType.PUBLIC_ID));
        assertNull(docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithPublicIdAndSystemId() {
        DocumentType docType = new DocumentType("html", "HTML PUBLIC", "URL1", "URL2");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertEquals("PUBLIC", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("URL1", docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr(DocumentType.NAME));
        assertNull(docType.attr(DocumentType.PUBLIC_ID));
        assertNull(docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws Exception {
        DocumentType docType = new DocumentType("html", "HTML", "URL1", "URL2");

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml());

        String expected = "<!DOCTYPE html PUBLIC \"URL1\" \"URL2\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithHtml5() throws Exception {
        DocumentType docType = new DocumentType("html", null, null, null);

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, Document.OutputSettings.syntaxHtml5());

        String expected = "<!doctype html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() throws Exception {
        DocumentType docType = new DocumentType("html", null, null, null);

        Appendable accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, Document.OutputSettings.syntaxHtml());

        String expected = "";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType docType1 = new DocumentType("html", null, null, null);
        DocumentType docType2 = new DocumentType("html", "HTML PUBLIC", "URL1", "URL2");

        assertTrue(docType1.has(DocumentType.NAME));
        assertTrue(docType2.has(DocumentType.NAME));
        assertTrue(docType2.has(DocumentType.PUBLIC_ID));
        assertTrue(docType2.has(DocumentType.SYSTEM_ID));
        assertFalse(docType1.has(DocumentType.PUBLIC_ID));
        assertFalse(docType1.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testParseDocumentType() throws Exception {
        String htmlCode = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";

        Document parsedDocument = Parser.parseBodyFragment(htmlCode);
        Node node = parsedDocument.body();

        Element docTypeElement = (Element) node.child(0);
        DocumentType docType = (DocumentType) docTypeElement.child(0);

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertEquals("PUBLIC", docType.attr(DocumentType.PUBLIC_ID));
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", docType.attr(DocumentType.SYSTEM_ID));
    }
}