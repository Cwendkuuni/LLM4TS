package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicID", docType.attr("publicId"));
        assertEquals("systemID", docType.attr("systemId"));
        assertNull(docType.parent());
        assertNull(docType.children());
    }

    @Test
    public void testDocumentTypeWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html");

        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicID", "systemID", doc.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicID\" systemID>\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", null, "systemID", doc.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html SYSTEM systemID>\n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicID", null, doc.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"publicID\" \n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyName() {
        Document doc = new Document();
        DocumentType docType = new DocumentType(null, "publicID", "systemID", doc.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE \n";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicID", "systemID", doc.baseUri());

        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testDocumentTypeWithElementChild() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicID", "systemID", doc.baseUri());
        Element element = new Element("html");
        docType.appendChild(element);

        assertEquals(1, docType.childNodes().size());
        assertEquals(element, docType.childNodes().get(0));
    }
}