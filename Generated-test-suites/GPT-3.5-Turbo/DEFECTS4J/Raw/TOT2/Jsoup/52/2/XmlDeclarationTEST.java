package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", xmlDeclaration.name());
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        xmlDeclaration.attributes().put("version", "1.0");
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder sb = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown");
        }
        assertEquals("<!xml>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder sb = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown");
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<!xml>", xmlDeclaration.toString());
    }
}