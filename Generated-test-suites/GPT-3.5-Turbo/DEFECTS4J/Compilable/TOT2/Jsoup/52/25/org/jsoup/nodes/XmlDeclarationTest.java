package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("#declaration", declaration.nodeName());
        assertEquals("xml", declaration.name());
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", declaration.getWholeDeclaration());

        declaration = new XmlDeclaration("xml", "http://example.com", false);
        declaration.attributes().put("version", "1.0");
        assertEquals("xml version=\"1.0\"", declaration.getWholeDeclaration());

        declaration = new XmlDeclaration("xml", "http://example.com", false);
        declaration.attributes().put("version", "1.0");
        declaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtml() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<?xml>", declaration.outerHtml());
    }
}