package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Validate;

import org.junit.Test;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", null);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertNull(documentType.attr("publicId"));
        assertNull(documentType.attr("systemId"));
    }

    /**
     * Test constructor with empty name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorEmptyName() {
        new DocumentType(null, "publicId1", "systemId1", null);
    }

    /**
     * Test constructor with null attributes.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullAttributes() {
        new DocumentType("html", null, null, null);
    }

    /**
     * Test nodeName method.
     */
    @Test
    public void testNodeName() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", null);
        assertEquals("#doctype", documentType.nodeName());
    }

    /**
     * Test outerHtmlHead method with valid arguments.
     */
    @Test
    public void testOuterHtmlHead() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId1\" systemId1>\n", accum.toString());
    }

    /**
     * Test outerHtmlHead method with empty publicId and systemId.
     */
    @Test
    public void testOuterHtmlHeadEmptyIds() {
        DocumentType documentType = new DocumentType("html", "", "", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        assertEquals("<!DOCTYPE html>\n", accum.toString());
    }

    /**
     * Test outerHtmlHead method with null accumulator.
     */
    @Test(expected = NullPointerException.class)
    public void testOuterHtmlHeadNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", null);
        documentType.outerHtmlHead(null, 0, null);
    }

    /**
     * Test outerHtmlTail method with valid arguments.
     */
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    /**
     * Test outerHtmlTail method with null accumulator.
     */
    @Test(expected = NullPointerException.class)
    public void testOuterHtmlTailNullAccumulator() {
        DocumentType documentType = new DocumentType("html", "publicId1", "systemId1", null);
        documentType.outerHtmlTail(null, 0, null);
    }
}