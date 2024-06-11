package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {

    @Test
    public void testXmlNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclarationWithOneAttribute() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        xmlDeclaration.attributes.put("version", "1.0");
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithMultipleAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        xmlDeclaration.attributes.put("version", "1.0.1");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0.1\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithDifferentName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "http://www.example.com", false);
        assertEquals("declaration", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<?xml?>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("<?xml?>", xmlDeclaration.toString());
    }
}