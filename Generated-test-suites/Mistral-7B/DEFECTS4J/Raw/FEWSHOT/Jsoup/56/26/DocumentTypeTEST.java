package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "http://www.w3.org/TR/html5/DTD/html5-document-fragment-minimal-20220322.dtd";
        String baseUri = "https://www.example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(name, documentType.attr(NAME));
        assertEquals(publicId, documentType.attr(PUBLIC_ID));
        assertEquals(systemId, documentType.attr(SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        String name = "html";
        String baseUri = "https://www.example.com/";

        DocumentType documentType = new DocumentType(name, null, null, baseUri);

        assertEquals(name, documentType.attr(NAME));
        assertNull(documentType.attr(PUBLIC_ID));
        assertNull(documentType.attr(SYSTEM_ID));
        assertEquals(baseUri, documentType.baseUri());
    }

    @Test
    public void testStripLeadingHyphens() {
        // Test case for base case
        assertEquals("html", Util.stripLeadingHyphens("#html"));

        // Test case for multiple leading hyphens
        assertEquals("html", Util.stripLeadingHyphens("----html"));

        // Test case for empty string
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testHasAttribute() {
        DocumentType documentType = new DocumentType("html", "HTML5", "http://www.w3.org/TR/html5/DTD/html5-document-fragment-minimal-20220322.dtd", "https://www.example.com/");

        assertTrue(documentType.has(NAME));
        assertTrue(documentType.has(PUBLIC_ID));
        assertTrue(documentType.has(SYSTEM_ID));

        DocumentType documentTypeWithEmptyPublicId = new DocumentType("html", null, "http://www.w3.org/TR/html5/DTD/html5-document-fragment-minimal-20220322.dtd", "https://www.example.com/");

        assertTrue(documentTypeWithEmptyPublicId.has(NAME));
        assertFalse(documentTypeWithEmptyPublicId.has(PUBLIC_ID));
    }

    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "HTML5", "http://www.w3.org/TR/html5/DTD/html5-document-fragment-minimal-20220322.dtd", "https://www.example.com/");

        assertEquals("#doctype", documentType.nodeName());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        DocumentType documentType = new DocumentType("html", "HTML5", "http://www.w3.org/TR/html5/DTD/html5-document-fragment-minimal-20220322.dtd", "https://www.example.com/");

        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlHead(accumulator, 0, null);

        String expectedOutput = "<!DOCTYPE html PUBLIC \"HTML5\" \"http://www.w3.org/TR/html5/DTD/html5-document-fragment-minimal-20220322.dtd\">";
        assertEquals(expectedOutput, accumulator.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        DocumentType documentType = new DocumentType("html", "HTML5", "http://www.w3.org/TR/html5/DTD/html5-document-fragment-minimal-20220322.dtd", "https://www.example.com/");

        Appendable accumulator = new StringBuilder();
        documentType.outerHtmlTail(accumulator, 0, null);

        // No output should be generated
        assertEquals("", accumulator.toString());
    }
}