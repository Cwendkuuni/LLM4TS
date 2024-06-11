package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {

    private Document doc;

    @Before
    public void setUp() {
        doc = new Document();
    }

    @Test
    public void testXmlDeclarationWithNameAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("<?xml xml?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithVersionAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlDeclaration.attributes().put("version", "1.0");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithEncodingAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithVersionEncodingAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNameProcessingInstructionAndBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-pi", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml-pi", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertEquals("<?xml-pi>", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testXmlDeclarationWithNullName() {
        try {
            new XmlDeclaration(null, "baseUri", false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        try {
            new XmlDeclaration("", "baseUri", false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testXmlDeclarationWithNullBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertNull(xmlDeclaration.baseUri());
    }

    @Test
    public void testXmlDeclarationToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("<?xml xml?>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithVersionToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlDeclaration.attributes().put("version", "1.0");
        assertEquals("<?xml version=\"1.0\"?>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithEncodingToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("<?xml encoding=\"UTF-8\"?>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithVersionEncodingToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNameProcessingInstructionAndToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-pi", "baseUri", false);
        assertEquals("<?xml-pi>", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationOuterHtmlHead() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        Element parent = new Element("html");
        parent.appendChild(xmlDeclaration);
        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);
        assertEquals("<xml xml?>", accum.toString());
    }

    @Test
    public void testXmlDeclarationOuterHtmlTail() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }
}