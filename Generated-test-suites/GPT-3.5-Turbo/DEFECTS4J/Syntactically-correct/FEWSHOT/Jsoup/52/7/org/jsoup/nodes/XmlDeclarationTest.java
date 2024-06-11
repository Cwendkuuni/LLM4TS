package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testGetName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("name", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("name", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlHead(sb, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!name>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlTail(sb, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("<!name>", declaration.toString());
    }
}