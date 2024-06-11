package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Attributes;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType docType = new DocumentType("html", "html", null, null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertNull(docType.attr(DocumentType.PUBLIC_ID));
        assertNull(docType.attr(DocumentType.SYSTEM_ID));

        docType = new DocumentType("html", "html", "http://www.w3.org/TR/html4/strict.dtd", null);
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr(DocumentType.SYSTEM_ID));
        assertNull(docType.attr(DocumentType.PUBLIC_ID));
    }

    @Test
    public void testDocumentTypeCreationWithPublicId() {
        DocumentType docType = new DocumentType("html", "html", null, null, "html_public");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertEquals("html_public", docType.attr(DocumentType.PUBLIC_ID));
        assertNull(docType.attr(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testDocumentTypeCreationWithBothPublicAndSystemId() {
        DocumentType docType = new DocumentType("html", "html", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com/my_custom_dtd.dtd", "html_public");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attr(DocumentType.NAME));
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", docType.attr(DocumentType.SYSTEM_ID));
        assertEquals("html_public", docType.attr(DocumentType.PUBLIC_ID));
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "html", null, null);
        Attributes attributes = docType.attributes();
        assertFalse(docType.has("nonExistentAttribute"));
        assertTrue(docType.has(DocumentType.NAME));
        assertTrue(docType.has(DocumentType.PUBLIC_ID));
        assertTrue(docType.has(DocumentType.SYSTEM_ID));
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType docType = new DocumentType("html", "html", null, null);
        Appendable accumulator = new StringBuilder();
        Document document = new Document();
        document.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        assertEquals("<!doctype html>", accumulator.toString());

        docType = new DocumentType("html", "html", "http://www.w3.org/TR/html4/strict.dtd", null);
        accumulator = new StringBuilder();
        document.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accumulator.toString());

        docType = new DocumentType("html", "html", null, null, "html_public");
        accumulator = new StringBuilder();
        document.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        assertEquals("<!doctype html PUBLIC \"html_public\">", accumulator.toString());

        docType = new DocumentType("html", "html", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com/my_custom_dtd.dtd", "html_public");
        accumulator = new StringBuilder();
        document.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        assertEquals("<!doctype html PUBLIC \"html_public\" SYSTEM \"http://www.example.com/my_custom_dtd.dtd\">", accumulator.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        DocumentType docType = new DocumentType("html", "html", null, null);
        Appendable accumulator = new StringBuilder();
        Document document = new Document();
        document.outerHtml(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html), null);
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        docType.outerHtmlTail(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        assertEquals("<!doctype html>", accumulator.toString());

        docType = new DocumentType("html", "html", "http://www.w3.org/TR/html4/strict.dtd", null);
        accumulator = new StringBuilder();
        document.outerHtml(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html), null);
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        docType.outerHtmlTail(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accumulator.toString());

        docType = new DocumentType("html", "html", null, null, "html_public");
        accumulator = new StringBuilder();
        document.outerHtml(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html), null);
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        docType.outerHtmlTail(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        assertEquals("<!doctype html PUBLIC \"html_public\">", accumulator.toString());

        docType = new DocumentType("html", "html", "http://www.w3.org/TR/html4/strict.dtd", "http://www.example.com/my_custom_dtd.dtd", "html_public");
        accumulator = new StringBuilder();
        document.outerHtml(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html), null);
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        docType.outerHtmlTail(accumulator, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
        assertEquals("<!doctype html PUBLIC \"html_public\" SYSTEM \"http://www.example.com/my_custom_dtd.dtd\">", accumulator.toString());
    }
}