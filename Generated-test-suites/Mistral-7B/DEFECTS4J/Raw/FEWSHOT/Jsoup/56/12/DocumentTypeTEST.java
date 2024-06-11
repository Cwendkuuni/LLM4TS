package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.helper.StringUtil.isBlank;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertFalse(docType.has(DocumentType.PUBLIC_ID));
        assertFalse(docType.has(DocumentType.SYSTEM_ID));

        docType = new DocumentType("html", null, null, null);
        assertTrue(docType.has(DocumentType.NAME));
        assertFalse(docType.has(DocumentType.PUBLIC_ID));
        assertFalse(docType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeOutputHtml() throws IOException {
        DocumentType docType1 = new DocumentType("html", null, null, null);
        StringBuilder output = new StringBuilder();
        docType1.outerHtmlHead(output, 0, Document.OutputSettings.html());
        assertEquals("<!doctype html>", output.toString());

        DocumentType docType2 = new DocumentType("html", "publicId", "systemId", null);
        output.setLength(0);
        docType2.outerHtmlHead(output, 0, Document.OutputSettings.html());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", output.toString());
    }

    @Test
    public void testDocumentTypeOutputXml() throws IOException {
        DocumentType docType1 = new DocumentType("html", null, null, null);
        StringBuilder output = new StringBuilder();
        docType1.outerHtmlHead(output, 0, Document.OutputSettings.xml());
        assertEquals("<!DOCTYPE html>", output.toString());

        DocumentType docType2 = new DocumentType("html", "publicId", "systemId", null);
        output.setLength(0);
        docType2.outerHtmlHead(output, 0, Document.OutputSettings.xml());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", output.toString());
    }

    @Test
    public void testDocumentTypeOutputWithBlankAttributes() throws IOException {
        DocumentType docType = new DocumentType("html", "publicId", "", null);
        StringBuilder output = new StringBuilder();
        docType.outerHtmlHead(output, 0, Document.OutputSettings.html());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"\" >", output.toString());
    }

    @Test
    public void testDocumentTypeOutputWithoutNameAttribute() throws IOException {
        DocumentType docType = new DocumentType(null, "publicId", "systemId", null);
        StringBuilder output = new StringBuilder();
        docType.outerHtmlHead(output, 0, Document.OutputSettings.html());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", output.toString());
    }

    @Test
    public void testDocumentTypeOutputWithNullBaseUri() throws IOException {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder output = new StringBuilder();
        docType.outerHtmlHead(output, 0, Document.OutputSettings.html());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", output.toString());
    }

    @Test
    public void testDocumentTypeOutputWithEmptyBaseUri() throws IOException {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "");
        StringBuilder output = new StringBuilder();
        docType.outerHtmlHead(output, 0, Document.OutputSettings.html());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", output.toString());
    }

    @Test
    public void testDocumentTypeOutputWithBothIds() throws IOException {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder output = new StringBuilder();
        docType.outerHtmlHead(output, 0, Document.OutputSettings.html());
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", output.toString());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        assertTrue(docType.has(DocumentType.NAME));
        assertFalse(docType.has(DocumentType.PUBLIC_ID));
        assertFalse(docType.has(DocumentType.SYSTEM_ID));

        docType.attr(DocumentType.PUBLIC_ID, "testPublicId");
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertEquals("testPublicId", docType.attr(DocumentType.PUBLIC_ID));
    }
}