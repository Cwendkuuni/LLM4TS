package org.jsoup.nodes;

import static org.junit.Assert.*;
import static org.jsoup.helper.StringUtil.*;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(NAME));
        assertEquals("publicId", docType.attr(PUBLIC_ID));
        assertEquals("systemId", docType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithEmptyIds() {
        DocumentType docType = new DocumentType("html", "", "", null);
        assertEquals("#doctype", docType.nodeName());
        assertNull(docType.attr(PUBLIC_ID));
        assertNull(docType.attr(SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeOutputHtml() throws IOException {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.html());

        assertEquals("<!doctype html", accumulator.toString());
    }

    @Test
    public void testDocumentTypeOutputHtmlWithPublicId() throws IOException {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.html());

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\"", accumulator.toString());
    }

    @Test
    public void testDocumentTypeOutputHtmlWithSystemId() throws IOException {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.html());

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>", accumulator.toString());
    }

    @Test
    public void testDocumentTypeOutputHtmlWithNameAndIds() throws IOException {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.html());

        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" systemId>", accumulator.toString());
    }

    @Test
    public void testDocumentTypeOutputHtmlWithQuirksMode() throws IOException {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.quirksMode());

        assertEquals("<!DOCTYPE html\n<!DOCTYPE html SYSTEM \"publicId\">\n<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accumulator.toString());
    }

    @Test
    public void testDocumentTypeOutputHtmlWithNoIds() throws IOException {
        DocumentType docType = new DocumentType("html", "", "", null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.html());

        assertEquals("<!doctype html", accumulator.toString());
    }

    @Test
    public void testDocumentTypeOutputHtmlWithNoIdsAndQuirksMode() throws IOException {
        DocumentType docType = new DocumentType("html", "", "", null);

        StringBuilder accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.quirksMode());

        assertEquals("<!DOCTYPE html\n<!DOCTYPE html SYSTEM>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">", accumulator.toString());
    }
}