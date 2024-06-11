package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }

    @Test
    public void testEmptyConstructor() {
        DocumentType docType = new DocumentType();
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">");

        StringBuilder actual = new StringBuilder();
        docType.outerHtmlHead(actual, 0, doc.outputSettings());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "", "", "baseUri");
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html>");

        StringBuilder actual = new StringBuilder();
        docType.outerHtmlHead(actual, 0, doc.outputSettings());

        assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadWithNullName() {
        Document doc = new Document();
        DocumentType docType = new DocumentType(null, "publicId", "systemId", "baseUri");

        StringBuilder actual = new StringBuilder();
        docType.outerHtmlHead(actual, 0, doc.outputSettings());

        fail("IllegalArgumentException expected but not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadWithNullBaseUri() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder actual = new StringBuilder();
        docType.outerHtmlHead(actual, 0, doc.outputSettings());

        fail("IllegalArgumentException expected but not thrown.");
    }

    @Test
    public void testOuterHtmlTail() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");

        StringBuilder actual = new StringBuilder();
        docType.outerHtmlTail(actual, 0, doc.outputSettings());

        assertTrue(StringUtil.isBlank(actual.toString()));
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
    }
}