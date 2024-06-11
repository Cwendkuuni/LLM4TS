package org.jsoup.nodes.test;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for DocumentType.
 */
public class DocumentTypeTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
        assertNotNull(documentType);
        assertEquals("#doctype", documentType.nodeName());
        assertEquals("html", documentType.attr("name"));
        assertEquals("publicId", documentType.attr("publicId"));
        assertEquals("systemId", documentType.attr("systemId"));
        assertEquals("baseUri", documentType.baseUri());
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
     * Test outerHtmlHead method.
     */
    @Test
    public void testOuterHtmlHead() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test outerHtmlHead method with empty publicId and systemId.
     */
    @Test
    public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "", null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlHead(accum, 0, null);
        String expected = "<!DOCTYPE html>\n";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test outerHtmlTail method.
     */
    @Test
    public void testOuterHtmlTail() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        StringBuilder accum = new StringBuilder();
        documentType.outerHtmlTail(accum, 0, null);
        String expected = "";
        assertEquals(expected, accum.toString());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "publicId", "systemId", null);
        String expected = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n";
        assertEquals(expected, documentType.toString());

        Element parent = new Element("html");
        parent.append(documentType);
        String expectedElement = "<!DOCTYPE html PUBLIC \"publicId\" systemId>\n<html></html>";
        assertEquals(expectedElement, parent.toString());
    }

    /**
     * Test toString method with empty publicId and systemId.
     */
    @Test
    public void testToStringWithEmptyPublicIdAndSystemId() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", "", "", null);
        String expected = "<!DOCTYPE html>\n";
        assertEquals(expected, documentType.toString());

        Element parent = new Element("html");
        parent.append(documentType);
        String expectedElement = "<!DOCTYPE html>\n<html></html>";
        assertEquals(expectedElement, parent.toString());
    }

    /**
     * Test toString method with null baseUri.
     */
    @Test(expected = NullPointerException.class)
    public void testToStringWithNullBaseUri() {
        Document document = new Document();
        DocumentType documentType = new DocumentType("html", null, null, null);
        documentType.toString();
    }

    /**
     * Test attr method with null name.
     */
    @Test(expected = NullPointerException.class)
    public void testAttrWithNullName() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        documentType.attr(null, "value");
    }

    /**
     * Test attr method with empty publicId.
     */
    @Test
    public void testAttrWithEmptyPublicId() {
        DocumentType documentType = new DocumentType("html", "", null, null);
        Attribute attribute = documentType.attr("publicId", "value");
        assertNotNull(attribute);
        assertEquals("value", attribute.toString());
        assertEquals("value", documentType.attr("publicId").toString());
    }

    /**
     * Test attr method with empty systemId.
     */
    @Test
    public void testAttrWithEmptySystemId() {
        DocumentType documentType = new DocumentType("html", null, "", null);
        Attribute attribute = documentType.attr("systemId", "value");
        assertNotNull(attribute);
        assertEquals("value", attribute.toString());
        assertEquals("value", documentType.attr("systemId").toString());
    }

    /**
     * Test attr method with null baseUri.
     */
    @Test(expected = NullPointerException.class)
    public void testAttrWithNullBaseUri() {
        DocumentType documentType = new DocumentType("html", null, null, null);
        documentType.attr("publicId", "value");
    }
}