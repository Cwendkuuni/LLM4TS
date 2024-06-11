package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertEquals("html", documentType.attr("name"));
        Assert.assertEquals("publicID", documentType.attr("publicId"));
        Assert.assertEquals("systemID", documentType.attr("systemId"));
    }

    @Test
    public void testEmptyConstructor() {
        DocumentType documentType = new DocumentType("");
        Assert.assertEquals("#doctype", documentType.nodeName());
        Assert.assertNull(documentType.attr("name"));
        Assert.assertNull(documentType.attr("publicId"));
        Assert.assertNull(documentType.attr("systemId"));
    }

    @Test
    public void testNullNameConstructor() {
        try {
            new DocumentType(null, "", "", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicID\" \"systemID\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testEmptyPublicIdOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "", "systemID", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testEmptySystemIdOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicID", "", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicID\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testNullOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder accum = null;
        try {
            documentType.outerHtmlHead(accum, 0, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }
}