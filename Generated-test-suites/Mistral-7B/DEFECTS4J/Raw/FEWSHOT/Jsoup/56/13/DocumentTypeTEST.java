package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testNewDocumentType() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertEquals("publicID", docType.attr(PUBLIC_ID));
        assertEquals("systemID", docType.attr(SYSTEM_ID));
        assertNull(docType.attributes().get("quirkMode"));
    }

    @Test
    public void testEmptyDocumentType() {
        DocumentType docType = new DocumentType("html", null, null, null);

        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertNull(docType.attr(PUBLIC_ID));
        assertNull(docType.attr(SYSTEM_ID));
        assertNull(docType.attributes().get("quirkMode"));
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);

        Appendable accum = new StringBuffer();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!DOCTYPE html PUBLIC \"publicID\" SYSTEM \"systemID\">", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadHtml5() throws IOException {
        DocumentType docType = new DocumentType("html", null, null, null);

        Appendable accum = new StringBuffer();
        docType.outerHtmlHead(accum, 0, null);

        assertEquals("<!doctype html>", accum.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);

        assertTrue(docType.has(NAME));
        assertTrue(docType.has(PUBLIC_ID));
        assertTrue(docType.has(SYSTEM_ID));
        assertFalse(docType.has("quirkMode"));
    }

    @Test
    public void testAttributes() {
        DocumentType docType = new DocumentType("html", "publicID", "systemID", null);

        assertSame(docType.attributes(), docType.attributes());
    }
}