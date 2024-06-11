package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import org.junit.Test;

/**
 * Test class for the DocumentType class.
 */
public class DocumentTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertEquals("#doctype", docType.nodeName());
        assertEquals("html", docType.attrs().get("name"));
        assertEquals("publicId", docType.attrs().get("publicId"));
        assertEquals("systemId", docType.attrs().get("systemId"));
        assertEquals("baseUri", docType.baseUri());
    }

    /**
     * Test nodeName method.
     */
    @Test
    public void testNodeName() {
        DocumentType docType = new DocumentType("html", "", "", "");
        assertEquals("#doctype", docType.nodeName());
    }

    /**
     * Test outerHtmlHead method.
     */
    @Test
    public void testOuterHtmlHead() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE htmlPUBLIC \"publicId\" systemId>\n";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test outerHtmlHead method with empty publicId and systemId.
     */
    @Test
    public void testOuterHtmlHeadEmptyIds() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "", "", "baseUri");
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE html>\n";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test outerHtmlHead method with only publicId.
     */
    @Test
    public void testOuterHtmlHeadWithPublicId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "", "baseUri");
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE htmlPUBLIC \"publicId\" systemId>\n";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test outerHtmlHead method with only systemId.
     */
    @Test
    public void testOuterHtmlHeadWithSystemId() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE htmlSYSTEM systemId>\n";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test outerHtmlHead method with both publicId and systemId.
     */
    @Test
    public void testOuterHtmlHeadWithBothIds() {
        Document doc = new Document();
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlHead(accum, 0, null);

        String expected = "<!DOCTYPE htmlPUBLIC \"publicId\" systemId>\n";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test outerHtmlTail method.
     */
    @Test
    public void testOuterHtmlTail() {
        DocumentType docType = new DocumentType("html", "", "", "");
        StringBuilder accum = new StringBuilder();

        docType.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    /**
     * Test getNodeName method when the node is null.
     */
    @Test(expected = NullPointerException.class)
    public void testgetNodeNameNullNode() {
        Node node = null;
        node.nodeName();
    }

    /**
     * Test getNodeName method when the node is not a DocumentType.
     */
    @Test
    public void testgetNodeNameNonDocumentTypeNode() {
        Element element = new Element("div");
        String nodeName = element.nodeName();

        assertNotSame("#doctype", nodeName);
    }

    /**
     * Test attr method with null attributes.
     */
    @Test
    public void testAttrNullAttributes() {
        DocumentType docType = new DocumentType("html", "", "", "");

        Attributes attributes = docType.attrs();
        assertNull(attributes.get("nonExistentAttribute"));
    }

    /**
     * Test attr method with empty attributes.
     */
    @Test
    public void testAttrEmptyAttributes() {
        DocumentType docType = new DocumentType("html", "", "", "");

        Attributes attributes = docType.attrs();
        assertEquals("", attributes.get("name"));
        assertEquals("", attributes.get("publicId"));
        assertEquals("", attributes.get("systemId"));
        assertEquals("", attributes.get("baseUri"));
    }

    /**
     * Test attr method with non-empty attributes.
     */
    @Test
    public void testAttrNonEmptyAttributes() {
        DocumentType docType = new DocumentType("html", "publicId", "systemId", "baseUri");

        Attributes attributes = docType.attrs();
        assertEquals("html", attributes.get("name"));
        assertEquals("publicId", attributes.get("publicId"));
        assertEquals("systemId", attributes.get("systemId"));
        assertEquals("baseUri", attributes.get("baseUri"));
    }
}