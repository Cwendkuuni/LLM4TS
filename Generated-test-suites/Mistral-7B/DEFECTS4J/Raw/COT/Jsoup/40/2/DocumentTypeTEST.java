package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstruction() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
    }

    @Test
    public void testEmptyDocumentTypeConstruction() {
        DocumentType docType = new DocumentType("html", "", "", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        docType.outerHtmlHead(new StringBuilder(), 0, null);
        assertEquals(expected, docType.outerHtml());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        docType.outerHtmlTail(new StringBuilder(), 0, null);
        assertEquals("", docType.outerHtml());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDocumentTypeName() {
        new DocumentType(null, "publicId", "systemId", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicId() {
        new DocumentType("html", null, "systemId", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemId() {
        new DocumentType("html", "publicId", null, null);
    }

    @Test
    public void testDocumentTypeNodeName() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testDocumentTypeToString() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, docType.toString());
    }

    @Test
    public void testDocumentTypeWithBaseUri() {
        Document doc = new Document();
        Element base = new Element("html");
        doc.setBaseUri("http://example.com");
        doc.appendChild(base);
        DocumentType docType = new DocumentType("html", "publicId", "systemId", doc.baseUri().toString());
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\" xmlns:xml=" + doc.baseUri() + '>';
        docType.outerHtmlHead(new StringBuilder(), 0, null);
        assertEquals(expected, docType.outerHtml());
    }

    @Test
    public void testDocumentTypeWithEmptyBaseUri() {
        Document doc = new Document();
        Element base = new Element("html");
        doc.setBaseUri("");
        doc.appendChild(base);
        DocumentType docType = new DocumentType("html", "publicId", "systemId", doc.baseUri().toString());
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        docType.outerHtmlHead(new StringBuilder(), 0, null);
        assertEquals(expected, docType.outerHtml());
    }

}