package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertEquals(publicId, docType.attr("publicId"));
        assertEquals(systemId, docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithEmptyAttributes() {
        String name = "html";
        String publicId = "";
        String systemId = "";
        String baseUri = "http://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeCreationWithNullInputs() {
        String name = null;
        String publicId = null;
        String systemId = null;
        String baseUri = "http://example.com/";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        name = "";
        publicId = "HTML5";
        systemId = null;

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", docType.nodeName());
        assertEquals(name, docType.attr("name"));
        assertNull(docType.attr("publicId"));
        assertNull(docType.attr("systemId"));
        assertEquals(baseUri, docType.baseUri());
    }

    @Test
    public void testDocumentTypeToString() {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        String expected = "<!DOCTYPE html PUBLIC \"HTML5\" \"http://www.w3.org/TR/html5/\">";
        String actual = docType.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeToStringWithEmptyPublicId() {
        String name = "html";
        String publicId = "";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        String expected = "<!DOCTYPE html>";
        String actual = docType.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeToStringWithEmptySystemId() {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "";
        String baseUri = "http://example.com/";

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        String expected = "<!DOCTYPE html PUBLIC \"HTML5\">";
        String actual = docType.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeToStringWithNullInputs() {
        String name = null;
        String publicId = null;
        String systemId = null;
        String baseUri = null;

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        String expected = "<!DOCTYPE>";
        String actual = docType.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeOuterHtmlHead() {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        StringBuilder accum = new StringBuilder();

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML5\" \"http://www.w3.org/TR/html5/\">";
        String actual = accum.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptyPublicId() {
        String name = "html";
        String publicId = "";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        StringBuilder accum = new StringBuilder();

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";
        String actual = accum.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithEmptySystemId() {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "";
        String baseUri = "http://example.com/";

        StringBuilder accum = new StringBuilder();

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"HTML5\"";
        String actual = accum.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeOuterHtmlHeadWithNullInputs() {
        String name = null;
        String publicId = null;
        String systemId = null;
        String baseUri = null;

        StringBuilder accum = new StringBuilder();

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE";
        String actual = accum.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDocumentTypeOuterHtmlTail() {
        String name = "html";
        String publicId = "HTML5";
        String systemId = "http://www.w3.org/TR/html5/";
        String baseUri = "http://example.com/";

        StringBuilder accum = new StringBuilder();

        DocumentType docType = new DocumentType(name, publicId, systemId, baseUri);

        docType.outerHtmlTail(accum, 0, null);

        String expected = "";
        String actual = accum.toString();

        assertEquals(expected, actual);
    }
}