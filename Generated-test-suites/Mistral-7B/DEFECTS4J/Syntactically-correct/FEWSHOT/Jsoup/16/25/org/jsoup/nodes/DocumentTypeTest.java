package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML_DOC_TYPE", "html.dtd", document.baseUri());
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("HTML_DOC_TYPE", documentType.attr("publicId"));
        assertEquals("html.dtd", documentType.attr("systemId"));
        assertEquals(document.baseUri(), documentType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML_DOCTYPE", "html.dtd", document.baseUri());
        StringBuilder accumulator = new StringBuilder();

        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.defaultSettings());

        String expected = "<!DOCTYPE html PUBLIC \"HTML_DOCTYPE\" \"html.dtd\">";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicIdOnly() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML_DOCTYPE", null, document.baseUri());
        StringBuilder accumulator = new StringBuilder();

        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.defaultSettings());

        String expected = "<!DOCTYPE html PUBLIC \"HTML_DOCTYPE\">";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithSystemIdOnly() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", null, "html.dtd", document.baseUri());
        StringBuilder accumulator = new StringBuilder();

        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.defaultSettings());

        String expected = "<!DOCTYPE html SYSTEM \"html.dtd\">";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithBothPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "HTML_DOCTYPE", "html.dtd", document.baseUri());
        StringBuilder accumulator = new StringBuilder();

        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.defaultSettings());

        String expected = "<!DOCTYPE html PUBLIC \"HTML_DOCTYPE\" \"html.dtd\">";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", null, null, document.baseUri());

        // Empty outerHtmlTail method
        StringBuilder accumulator = new StringBuilder();
        documentType.outerHtmlTail(accumulator, 0, Document.OutputSettings.defaultSettings());

        assertEquals("", accumulator.toString());
    }

    @Test
    public void testDocumentTypeSetPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", null, null, document.baseUri());

        Attributes attrs = documentType.attributes();
        attrs.put("publicId", "HTML_PUBLIC_ID");
        attrs.put("systemId", "HTML_SYSTEM_ID");

        StringBuilder accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 0, Document.OutputSettings.defaultSettings());

        String expected = "<!DOCTYPE html PUBLIC \"HTML_PUBLIC_ID\" \"HTML_SYSTEM_ID\">";
        assertEquals(expected, accumulator.toString());
    }

    @Test
    public void testDocumentTypeClone() {
        Document document = new Document();
        DocumentType originalDocumentType = new DocumentType("html", "HTML_DOCTYPE", "html.dtd", document.baseUri());
        DocumentType clonedDocumentType = originalDocumentType.clone();

        assertEquals("#doctype", clonedDocumentType.nodeName());
        assertEquals("html", clonedDocumentType.attr("name"));
        assertEquals("HTML_DOCTYPE", clonedDocumentType.attr("publicId"));
        assertEquals("html.dtd", clonedDocumentType.attr("systemId"));
        assertEquals(document.baseUri(), clonedDocumentType.baseUri());
    }
}