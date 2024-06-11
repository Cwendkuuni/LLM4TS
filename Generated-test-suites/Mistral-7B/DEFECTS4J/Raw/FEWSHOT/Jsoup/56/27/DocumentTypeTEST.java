package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "{-//W3C//DTD HTML 4.01//EN}";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(name, documentType.attr(NAME));
        assertEquals(publicId, documentType.attr(PUBLIC_ID));
        assertEquals(systemId, documentType.attr(SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeNodeName() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() throws IOException {
        String name = "html";
        String publicId = "{-//W3C//DTD HTML 4.01//EN}";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accum = new StringBuffer();
        documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html).build());

        assertEquals("<!doctype html>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithPublicId() throws IOException {
        String name = "html";
        String publicId = "{-//W3C//DTD HTML 4.01//EN}";
        String systemId = null;
        String baseUri = "https://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accum = new StringBuffer();
        documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html).build());

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" >\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithSystemId() throws IOException {
        String name = "html";
        String publicId = null;
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accum = new StringBuffer();
        documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html).build());

        assertEquals("<!DOCTYPE html SYSTEM \"http://www.w3.org/TR/html4/strict.dtd\">\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNameOnly() throws IOException {
        String name = "html";
        String publicId = null;
        String systemId = null;
        String baseUri = "https://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accum = new StringBuffer();
        documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html).build());

        assertEquals("<!DOCTYPE html>\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithAllAttributes() throws IOException {
        String name = "html";
        String publicId = "{-//W3C//DTD HTML 4.01//EN}";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accum = new StringBuffer();
        documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html).build());

        assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" >\n", accum.toString());
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() throws IOException {
        String name = "html";
        String publicId = null;
        String systemId = null;
        String baseUri = "https://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        Appendable accum = new StringBuffer();
        documentType.outerHtmlTail(accum, 0, Document.OutputSettings.syntax(Syntax.html).build());

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testDocumentTypeHasAttribute() {
        String name = "html";
        String publicId = "{-//W3C//DTD HTML 4.01//EN}";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "https://example.com";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertTrue(documentType.has(NAME));
        assertTrue(documentType.has(PUBLIC_ID));
        assertTrue(documentType.has(SYSTEM_ID));

        String emptyBaseUri = "";
        DocumentType documentTypeEmptyBaseUri = new DocumentType(name, publicId, systemId, emptyBaseUri);

        assertFalse(documentTypeEmptyBaseUri.has(baseUri));
    }

    @Test
    public void testDocumentTypeNullAttributes() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertTrue(documentType.has(NAME));
        assertNull(documentType.attr(PUBLIC_ID));
        assertNull(documentType.attr(SYSTEM_ID));
    }
}