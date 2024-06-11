package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    /**
     * Test constructor of DocumentType.
     */
    @Test
    public void testDocumentTypeConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertNull(documentType.outerHtml());
    }

    /**
     * Test nodeName method.
     */
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        assertEquals("#doctype", documentType.nodeName());
    }

    /**
     * Test outerHtmlHead method with a valid doctype.
     */
    @Test
    public void testOuterHtmlHeadValidDoctype() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertTrue(accum.toString().startsWith("<!DOCTYPE html"));
        if (!StringUtil.isBlank(documentType.attr("publicId"))) {
            assertTrue(accum.toString().contains("PUBLIC \"" + documentType.attr("publicId") + "\""));
        }
        if (!StringUtil.isBlank(documentType.attr("systemId"))) {
            assertTrue(accum.toString().contains("SYSTEM \"" + documentType.attr("systemId") + "\""));
        }
        assertNull(documentType.outerHtmlTail(accum, 0, null));
    }

    /**
     * Test outerHtmlHead method with an empty publicId.
     */
    @Test
    public void testOuterHtmlHeadEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", null, "systemId", null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertTrue(accum.toString().startsWith("<!DOCTYPE html"));
        assertFalse(accum.toString().contains("PUBLIC \""));
        if (!StringUtil.isBlank(documentType.attr("systemId"))) {
            assertTrue(accum.toString().contains("SYSTEM \"" + documentType.attr("systemId") + "\""));
        }
        assertNull(documentType.outerHtmlTail(accum, 0, null));
    }

    /**
     * Test outerHtmlHead method with an empty systemId.
     */
    @Test
    public void testOuterHtmlHeadEmptySystemId() {
        DocumentType documentType = new DocumentType("html", "publicId", null, null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertTrue(accum.toString().startsWith("<!DOCTYPE html"));
        if (!StringUtil.isBlank(documentType.attr("publicId"))) {
            assertTrue(accum.toString().contains("PUBLIC \"" + documentType.attr("publicId") + "\""));
        }
        assertFalse(accum.toString().contains("SYSTEM \""));
        assertNull(documentType.outerHtmlTail(accum, 0, null));
    }

    /**
     * Test outerHtmlHead method with no attributes.
     */
    @Test
    public void testOuterHtmlHeadNoAttributes() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);

        assertTrue(accum.toString().startsWith("<!DOCTYPE html"));
        assertFalse(accum.toString().contains("PUBLIC"));
        assertFalse(accum.toString().contains("SYSTEM"));
        assertNull(documentType.outerHtmlTail(accum, 0, null));
    }

    /**
     * Test outerHtmlTail method.
     */
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", null, null, null);

        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);

        assertNull(documentType.outerHtmlTail(accum, 0, null));
    }
}