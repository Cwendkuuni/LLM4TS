package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * Test class for {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01//EN";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://localhost";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr(NAME));
        assertEquals(publicId, docType.attr(PUBLIC_ID));
        assertEquals(systemId, docType.attr(SYSTEM_ID));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        String name = "html";
        String baseUri = "http://localhost";

        DocumentType docType = new DocumentType(name, "", "", baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr(NAME));
        assertNull(docType.attr(PUBLIC_ID));
        assertNull(docType.attr(SYSTEM_ID));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithOnlyPublicId() {
        String name = "html";
        String publicId = "DTD HTML 1.0";
        String systemId = "";
        String baseUri = "http://localhost";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr(NAME));
        assertEquals(publicId, docType.attr(PUBLIC_ID));
        assertNull(docType.attr(SYSTEM_ID));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithOnlySystemId() {
        String name = "html";
        String publicId = "";
        String systemId = "DTD HTML 1.0.dtd";
        String baseUri = "http://localhost";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr(NAME));
        assertNull(docType.attr(PUBLIC_ID));
        assertEquals(systemId, docType.attr(SYSTEM_ID));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithAllAttributes() {
        String name = "html";
        String publicId = "DTD HTML 1.0";
        String systemId = "DTD HTML 1.0.dtd";
        String baseUri = "http://localhost";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr(NAME));
        assertEquals(publicId, docType.attr(PUBLIC_ID));
        assertEquals(systemId, docType.attr(SYSTEM_ID));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws IOException {
        String name = "html";
        String publicId = "DTD HTML 1.0";
        String systemId = "DTD HTML 1.0.dtd";
        String baseUri = "http://localhost";

        Document doc = new Document();
        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.html5());

        String expectedHtml = "<!doctype html PUBLIC \"DTD HTML 1.0\" \"DTD HTML 1.0.dtd\">";
        String actualHtml = accumulator.toString();

        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWhenNoPublicIdAndNoSystemId() throws IOException {
        String name = "html";
        String baseUri = "http://localhost";

        Document doc = new Document();
        DocumentType docType = new DocumentType(name, "", "", baseUri);

        Appendable accumulator = new StringBuilder();
        docType.outerHtmlHead(accumulator, 0, Document.OutputSettings.html5());

        String expectedHtml = "<!doctype html>";
        String actualHtml = accumulator.toString();

        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() throws IOException {
        String name = "html";
        String publicId = "DTD HTML 1.0";
        String systemId = "DTD HTML 1.0.dtd";
        String baseUri = "http://localhost";

        Document doc = new Document();
        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accumulator = new StringBuilder();
        docType.outerHtmlTail(accumulator, 0, Document.OutputSettings.html5());

        String expectedHtml = "";
        String actualHtml = accumulator.toString();

        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        String name = "html";
        String publicId = "DTD HTML 1.0";
        String systemId = "DTD HTML 1.0.dtd";
        String baseUri = "http://localhost";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        boolean hasName = docType.has(NAME);
        boolean hasPublicId = docType.has(PUBLIC_ID);
        boolean hasSystemId = docType.has(SYSTEM_ID);

        assertTrue(hasName);
        assertTrue(hasPublicId);
        assertTrue(hasSystemId);

        String emptyAttribute = "";
        boolean hasEmptyAttribute = docType.has(emptyAttribute);

        assertFalse(hasEmptyAttribute);
    }
}