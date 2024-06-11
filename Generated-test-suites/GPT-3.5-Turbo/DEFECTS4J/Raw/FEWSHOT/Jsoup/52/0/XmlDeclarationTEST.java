package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "example.com", true);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "example.com", true);
        assertEquals("xml", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "example.com", true);
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "example.com", true);
        StringBuilder expected = new StringBuilder();
        expected.append("<!xml>");
        StringBuilder actual = new StringBuilder();
        try {
            declaration.outerHtmlHead(actual, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "example.com", true);
        StringBuilder expected = new StringBuilder();
        expected.append(">");
        StringBuilder actual = new StringBuilder();
        try {
            declaration.outerHtmlTail(actual, 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "example.com", true);
        assertEquals("<!xml>", declaration.toString());
    }
}