package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        DocumentType documentType = new DocumentType("html", "html.disco", null, null);
        assertEquals("html", documentType.name());
        assertNull(documentType.publicId());
        assertNull(documentType.systemId());
    }

    @Test
    public void testDocumentTypeCreationWithPublicId() {
        DocumentType documentType = new DocumentType("html", "html.disco", "Public HTML", null);
        assertEquals("html", documentType.name());
        assertEquals("Public HTML", documentType.publicId());
        assertNull(documentType.systemId());
    }

    @Test
    public void testDocumentTypeCreationWithSystemId() {
        DocumentType documentType = new DocumentType("html", null, null, "http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("html", documentType.name());
        assertNull(documentType.publicId());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.systemId());
    }

    @Test
    public void testDocumentTypeCreationWithPublicIdAndSystemId() {
        DocumentType documentType = new DocumentType("html", "html.disco", "Public HTML", "http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("html", documentType.name());
        assertEquals("Public HTML", documentType.publicId());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", documentType.systemId());
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType documentType = new DocumentType("html", null, null, null);
        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 1, OutputSettings.html5());
        assertEquals("<!doctype html>", accumulator.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicId() throws IOException {
        DocumentType documentType = new DocumentType("html", "html.disco", null, null);
        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 1, OutputSettings.html5());
        assertEquals("<!doctype html PUBLIC \"html.disco\">", accumulator.toString());
    }

    @Test
    public void testOuterHtmlHeadWithSystemId() throws IOException {
        DocumentType documentType = new DocumentType("html", null, null, "http://www.w3.org/TR/html4/strict.dtd");
        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 1, OutputSettings.html5());
        assertEquals("<!doctype html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accumulator.toString());
    }

    @Test
    public void testOuterHtmlHeadWithPublicIdAndSystemId() throws IOException {
        DocumentType documentType = new DocumentType("html", "html.disco", "Public HTML", "http://www.w3.org/TR/html4/strict.dtd");
        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 1, OutputSettings.html5());
        assertEquals("<!doctype html PUBLIC \"Public HTML\" SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">", accumulator.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        assertFalse(documentType.has("name"));
        documentType.attr("name", "html");
        assertTrue(documentType.has("name"));
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        DocumentType documentType = new DocumentType("html", null, null, null);
        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlTail(accumulator, 1, OutputSettings.html5());
    }
}