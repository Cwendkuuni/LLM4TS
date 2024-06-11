package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<?xml version=\"null\" encoding=\"null\"?>", xmlDeclaration.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("", xmlDeclaration.outerHtmlTail());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<?xml version=\"null\" encoding=\"null\"?>", xmlDeclaration.toString());
    }
}