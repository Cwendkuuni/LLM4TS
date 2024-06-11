package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.helper.Validate;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeConstructor() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr("name"));
        assertEquals("publicId", docType.attr("publicId"));
        assertEquals("systemId", docType.attr("systemId"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeConstructorWithEmptyName() {
        DocumentType docType = new DocumentType(null, "publicId", "systemId", null);
        // Since we are testing for an exception, no assertions are needed.
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        DocumentType docType = new DocumentType("html", "", "systemId", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
        DocumentType docType = new DocumentType("html", "publicId", "", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\">";
        assertEquals(expected, accum.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDocumentTypeOuterHtmlHeadWithNullAccumulator() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        docType.outerHtmlHead(null, 0, null);
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);
        String expected = "";
        assertEquals(expected, accum.toString());
    }
}