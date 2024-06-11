package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNewDocumentType() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertNull(documentType.attr("id"));
        assertNull(documentType.attr("class"));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE htmlPUBLIC \"publicId\" systemId>\n", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "", "", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html>\n", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdOnly() {
        DocumentType documentType = new DocumentType("html", "publicId", null, null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" >\n", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemIdOnly() {
        DocumentType documentType = new DocumentType("html", null, "systemId", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html SYSTEM systemId>\n", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBothPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>\n", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertEquals("", accum.toString());
    }

    @Test
    public void testAttr() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        documentType.attr("id", "testId");
        assertEquals("testId", documentType.attr("id"));

        documentType.attr("class", "testClass");
        assertEquals("testClass", documentType.attr("class"));

        documentType.attr("custom", "customValue");
        assertEquals("customValue", documentType.attr("custom"));

        documentType.removeAttr("id");
        assertNull(documentType.attr("id"));
    }

    @Test
    public void testEquals() {
        DocumentType documentType1 = new DocumentType("html", "publicId1", "systemId1", null);
        DocumentType documentType2 = new DocumentType("html", "publicId1", "systemId1", null);

        assertEquals(documentType1, documentType2);
    }

    @Test
    public void testNotEqualsDifferentName() {
        DocumentType documentType1 = new DocumentType("html", "publicId1", "systemId1", null);
        DocumentType documentType2 = new DocumentType("head", "publicId1", "systemId1", null);

        assertNotEquals(documentType1, documentType2);
    }

    @Test
    public void testNotEqualsDifferentPublicId() {
        DocumentType documentType1 = new DocumentType("html", "publicId1", "systemId1", null);
        DocumentType documentType2 = new DocumentType("html", "publicId2", "systemId1", null);

        assertNotEquals(documentType1, documentType2);
    }

    @Test
    public void testNotEqualsDifferentSystemId() {
        DocumentType documentType1 = new DocumentType("html", "publicId1", "systemId1", null);
        DocumentType documentType2 = new DocumentType("html", "publicId1", "systemId2", null);

        assertNotEquals(documentType1, documentType2);
    }
}