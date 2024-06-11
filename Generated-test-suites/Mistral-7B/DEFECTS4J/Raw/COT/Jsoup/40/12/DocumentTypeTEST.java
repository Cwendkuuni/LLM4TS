package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.Validate;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicID", docType.attr("publicId"));
        assertEquals("systemID", docType.attr("systemId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorWithEmptyName() {
        new DocumentType("", "publicID", "systemID", null);
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicID\" \"systemID\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "systemID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "publicID", "", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicID\">";
        assertEquals(expected, accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadWithNullAccumulator() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        docType.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }
}