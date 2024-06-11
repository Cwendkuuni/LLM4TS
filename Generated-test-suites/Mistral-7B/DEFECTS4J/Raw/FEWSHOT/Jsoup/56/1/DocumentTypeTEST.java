package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "html";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com/";

        DocumentType htmlDoctype = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals(name, htmlDoctype.attr(NAME));
        assertEquals(publicId, htmlDoctype.attr(PUBLIC_ID));
        assertEquals(systemId, htmlDoctype.attr(SYSTEM_ID));
        assertEquals(baseUri, htmlDoctype.baseUri());
    }

    @Test
    public void testEmptyDocumentTypeCreation() {
        String name = "html";
        String baseUri = "http://example.com/";

        DocumentType htmlDoctype = new DocumentType(name, null, null, baseUri);

        assertEquals(name, htmlDoctype.attr(NAME));
        assertEquals(null, htmlDoctype.attr(PUBLIC_ID));
        assertEquals(null, htmlDoctype.attr(SYSTEM_ID));
        assertEquals(baseUri, htmlDoctype.baseUri());
    }

    @Test
    public void testDocumentTypeToString() {
        String name = "html";
        String publicId = "html";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com/";

        DocumentType htmlDoctype = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("<!DOCTYPE " + name + (publicId != null ? " PUBLIC \"" + publicId + "\"" : "") + (systemId != null ? " \"" + systemId + "\"" : "") + ">", htmlDoctype.toString());
    }

    @Test
    public void testDocumentTypeWithoutAttributesToString() {
        String name = "html";
        String baseUri = "http://example.com/";

        DocumentType htmlDoctype = new DocumentType(name, null, null, baseUri);

        assertEquals("<!DOCTYPE " + name + ">", htmlDoctype.toString());
    }

    @Test
    public void testHasAttribute() {
        String name = "html";
        String publicId = "html";
        String systemId = "http://www.w3.org/TR/html4/strict.dtd";
        String baseUri = "http://example.com/";

        DocumentType htmlDoctype = new DocumentType(name, publicId, systemId, baseUri);

        assertTrue(htmlDoctype.has(NAME));
        assertTrue(htmlDoctype.has(PUBLIC_ID));
        assertTrue(htmlDoctype.has(SYSTEM_ID));

        DocumentType emptyHtmlDoctype = new DocumentType(name, null, null, baseUri);

        assertTrue(emptyHtmlDoctype.has(NAME));
        assertFalse(emptyHtmlDoctype.has(PUBLIC_ID));
        assertFalse(emptyHtmlDoctype.has(SYSTEM_ID));
    }

    @Test
    public void testHasEmptyAttribute() {
        String name = "html";
        String baseUri = "http://example.com/";

        DocumentType htmlDoctype = new DocumentType(name, null, null, baseUri);

        assertTrue(htmlDoctype.has(NAME));
        assertFalse(htmlDoctype.has(PUBLIC_ID));
        assertFalse(htmlDoctype.has(SYSTEM_ID));

        DocumentType emptyHtmlDoctype = new DocumentType(name, "", "", baseUri);

        assertTrue(emptyHtmlDoctype.has(NAME));
        assertFalse(emptyHtmlDoctype.has(PUBLIC_ID));
        assertFalse(emptyHtmlDoctype.has(SYSTEM_ID));
    }

    @Test
    public void testHasNullAttribute() {
        String name = "html";
        String baseUri = "http://example.com/";

        DocumentType htmlDoctype = new DocumentType(name, null, null, baseUri);

        assertNull(htmlDoctype.attr(PUBLIC_ID));
        assertNull(htmlDoctype.attr(SYSTEM_ID));
    }

    @Test
    public void testHasEmptyStringAttribute() {
        String name = "html";
        String baseUri = "http://example.com/";

        DocumentType htmlDoctype = new DocumentType(name, "", "", baseUri);

        assertEquals("", htmlDoctype.attr(PUBLIC_ID));
        assertEquals("", htmlDoctype.attr(SYSTEM_ID));
    }
}