package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

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
    public void testEmptyDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html");
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertNull(docType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder expected = new StringBuilder();
        expected.append("<!DOCTYPE html");
        if (!StringUtil.isBlank(docType.attr("publicId")))
            expected.append(" PUBLIC \"").append(docType.attr("publicId")).append('"');
        if (!StringUtil.isBlank(docType.attr("systemId")))
            expected.append(" \"").append(docType.attr("systemId")).append('"');
        expected.append('>');

        StringBuilder actual = new StringBuilder();
        docType.outerHtmlHead(actual, 0, null);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder actual = new StringBuilder();
        docType.outerHtmlTail(actual, 0, null);

        // outerHtmlTail should not add anything to the output
        assertEquals("", actual.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyNameInDocumentTypeConstructor() {
        new DocumentType("", "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullNameInDocumentTypeConstructor() {
        new DocumentType(null, "publicId", "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullPublicIdInDocumentTypeConstructor() {
        new DocumentType("html", null, "systemId", "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSystemIdInDocumentTypeConstructor() {
        new DocumentType("html", "publicId", null, "baseUri");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullBaseUriInDocumentTypeConstructor() {
        new DocumentType("html", "publicId", "systemId", null);
    }
}