package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "XML 1.0";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        DocumentType docType = new DocumentType(name, publicId, systemId, null);

        assertEquals(name, docType.name());
        assertEquals(publicId, docType.publicId());
        assertEquals(systemId, docType.systemId());
    }

    @Test
    public void testDocumentTypeCreationWithBaseUri() {
        String name = "html";
        String publicId = "XML 1.0";
        String systemId = "http://www.w3.org/TR/REC-html40/strict.dtd";
        String baseUri = "http://example.com";
        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(name, docType.name());
        assertEquals(publicId, docType.publicId());
        assertEquals(systemId, docType.systemId());
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", null, null, null);
        assertEquals("#doctype", docType.nodeName());
    }

    @Test
    public void testOuterHtmlHeadHtml5() {
        DocumentType docType = new DocumentType("html", null, null, null);

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!doctype html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithAttributes() {
        DocumentType docType = new DocumentType("html", "XML 1.0", "http://www.w3.org/TR/REC-html40/strict.dtd", null);

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"XML 1.0\" \"`http://www.w3.org/TR/REC-html40/strict.dtd`\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyAttributes() {
        DocumentType docType = new DocumentType("html", null, null, null);

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithBlankAttributes() {
        DocumentType docType = new DocumentType("html", "XML 1.0", "", null);

        Appendable accum = new StringBuilder();
        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"\" \"\"\">";
        assertEquals(expected, accum.toString());
    }

    @Test
    public void testHasAttribute() {
        DocumentType docType = new DocumentType("html", "XML 1.0", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        docType.attr(NAME, "html");

        boolean hasName = docType.has(NAME);
        assertTrue(hasName);

        boolean hasPublicId = docType.has(PUBLIC_ID);
        assertFalse(hasPublicId);

        boolean hasSystemId = docType.has(SYSTEM_ID);
        assertFalse(hasSystemId);
    }

    @Test
    public void testHasAttributeWithPublicId() {
        DocumentType docType = new DocumentType("html", "XML 1.0", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        docType.attr(PUBLIC_ID, "XML 1.0");

        boolean hasPublicId = docType.has(PUBLIC_ID);
        assertTrue(hasPublicId);

        boolean hasName = docType.has(NAME);
        assertTrue(hasName);

        boolean hasSystemId = docType.has(SYSTEM_ID);
        assertFalse(hasSystemId);
    }

    @Test
    public void testHasAttributeWithSystemId() {
        DocumentType docType = new DocumentType("html", "XML 1.0", "http://www.w3.org/TR/REC-html40/strict.dtd", null);
        docType.attr(SYSTEM_ID, "http://www.w3.org/TR/REC-html40/strict.dtd");

        boolean hasSystemId = docType.has(SYSTEM_ID);
        assertTrue(hasSystemId);

        boolean hasName = docType.has(NAME);
        assertTrue(hasName);

        boolean hasPublicId = docType.has(PUBLIC_ID);
        assertFalse(hasPublicId);
    }

    @Test
    public void testHasAttributeWithNullAttribute() {
        DocumentType docType = new DocumentType("html", "XML 1.0", "http://www.w3.org/TR/REC-html40/strict.dtd", null);

        boolean hasNullAttribute = docType.has(null);
        assertFalse(hasNullAttribute);
    }

    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", null, null, null);

        Appendable accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, null);
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlTailWithIOException() {
        DocumentType docType = new DocumentType("html", null, null, null);

        Appendable accum = new StringBuilder();
        docType.outerHtmlTail(accum, 0, new IOException("An error occurred"));
    }
}