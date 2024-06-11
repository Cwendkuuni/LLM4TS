package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;

public class DocumentTypeTest {

    @Test
    public void testDocumentTypeCreation() {
        String name = "html";
        String publicId = "W3C";
        String systemId = "//DTD HTML401//EN";
        String baseUri = "https://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.attr("baseUri"));
    }

    @Test
    public void testEmptyName() {
        String name = "";
        String publicId = "W3C";
        String systemId = "//DTD HTML401//EN";
        String baseUri = "https://example.com/";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
            fail("Expected IllegalArgumentException due to empty name");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNullName() {
        String name = null;
        String publicId = "W3C";
        String systemId = "//DTD HTML401//EN";
        String baseUri = "https://example.com/";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
            fail("Expected NullPointerException due to null name");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testNullPublicId() {
        String name = "html";
        String publicId = null;
        String systemId = "//DTD HTML401//EN";
        String baseUri = "https://example.com/";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
            fail("Expected NullPointerException due to null publicId");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyPublicId() {
        String name = "html";
        String publicId = "";
        String systemId = "//DTD HTML401//EN";
        String baseUri = "https://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals("", documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals(baseUri, documentType.attr("baseUri"));
    }

    @Test
    public void testNullSystemId() {
        String name = "html";
        String publicId = "W3C";
        String systemId = null;
        String baseUri = "https://example.com/";

        try {
            new DocumentType(name, publicId, systemId, baseUri);
            fail("Expected NullPointerException due to null systemId");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEmptySystemId() {
        String name = "html";
        String publicId = "W3C";
        String systemId = "";
        String baseUri = "https://example.com/";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals("", documentType.attr("systemId"));
        assertEquals(baseUri, documentType.attr("baseUri"));
    }

    @Test
    public void testNullBaseUri() {
        String name = "html";
        String publicId = "W3C";
        String systemId = "//DTD HTML401//EN";
        String baseUri = null;

        try {
            new DocumentType(name, publicId, systemId, baseUri);
            fail("Expected NullPointerException due to null baseUri");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testEmptyBaseUri() {
        String name = "html";
        String publicId = "W3C";
        String systemId = "//DTD HTML401//EN";
        String baseUri = "";

        DocumentType documentType = new DocumentType(name, publicId, systemId, baseUri);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals(name, documentType.attr("name"));
        assertEquals(publicId, documentType.attr("publicId"));
        assertEquals(systemId, documentType.attr("systemId"));
        assertEquals("", documentType.attr("baseUri"));
    }

    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "W3C", "//DTD HTML401//EN", "https://example.com/");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"W3C //DTD HTML401//EN\" \"https://example.com/\">";

        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", "", "//DTD HTML401//EN", "https://example.com/");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>";

        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "W3C", "", "https://example.com/");

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html PUBLIC \"W3C\" \"https://example.com/\">";

        assertEquals(expected, accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "W3C", "//DTD HTML401//EN", "https://example.com/");

        documentType.outerHtmlHead(null, 0, null);

        // No assertion needed, as null accumulator is expected to be ignored
    }

    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadWithNullDocumentOutputSettings() {
        DocumentType documentType = new DocumentType("html", "W3C", "//DTD HTML401//EN", "https://example.com/");

        documentType.outerHtmlHead(null, 0, null);

        // Expected NullPointerException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOuterHtmlHeadWithNegativeDepth() {
        DocumentType documentType = new DocumentType("html", "W3C", "//DTD HTML401//EN", "https://example.com/");

        documentType.outerHtmlHead(new StringBuilder(), -1, null);

        // Expected IllegalArgumentException
    }
}