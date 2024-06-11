package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationWithNameAndVersion() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
        assertEquals("<declaration xml version=\"1.0\">", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationWithNameAndEncoding() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
        assertEquals("<declaration encoding=\"UTF-8\">", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationWithNameAndVersionAndEncoding() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
        assertEquals("<declaration xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlProcessingInstruction() {
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml-data", baseUri, true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("<?xml-data?>", xmlDeclaration.getWholeDeclaration());
        assertEquals("<?xml-data?>", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationWithEmptyName() {
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration("", baseUri, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNull(xmlDeclaration.name());
        assertEquals("", xmlDeclaration.getWholeDeclaration());
        assertEquals("", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationWithNullName() {
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration(null, baseUri, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNull(xmlDeclaration.name());
        assertEquals("", xmlDeclaration.getWholeDeclaration());
        assertEquals("", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationWithNullBaseUri() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertNotNull(xmlDeclaration.baseUri());
        assertEquals("", xmlDeclaration.getWholeDeclaration());
        assertEquals("<declaration xmlns=\"\">", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationToString() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("version", "1.0");
        attributes.put("encoding", "UTF-8");
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false, attributes);
        assertEquals("<declaration xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.outerHtml());
        assertEquals("<declaration xml version=\"1.0\" encoding=\"UTF-8\">", xmlDeclaration.toString());
    }

    @Test
    public void testXmlDeclarationWithNoAttributes() {
        String baseUri = "http://example.com";
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
        assertEquals("<declaration xml:space=\"preserve\">", xmlDeclaration.outerHtml());
    }

    @Test
    public void testXmlDeclarationWithEmptyAttributes() {
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", baseUri, false, attributes);
        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
        assertEquals("<declaration xml:space=\"preserve\"></declaration>", xmlDeclaration.outerHtml());
    }
}