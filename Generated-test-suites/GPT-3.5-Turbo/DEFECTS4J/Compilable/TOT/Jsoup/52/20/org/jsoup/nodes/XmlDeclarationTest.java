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
        assertEquals("xml version=\"null\" encoding=\"null\"", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder sb = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<?xml version=\"null\" encoding=\"null\">", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder sb = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<?xml version=\"null\" encoding=\"null\">", xmlDeclaration.toString());
    }
}