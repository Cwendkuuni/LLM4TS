package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class XmlDeclarationTest {
    @Test
    public void testConstructor() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertNotNull(declaration);
        assertEquals("xml", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", declaration.getWholeDeclaration());

        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!xml>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<!xml>", declaration.toString());
    }
}