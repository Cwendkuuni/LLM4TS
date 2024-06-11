package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("name", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("name", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", false);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertNotNull(sb.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertNotNull(sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", false);
        assertNotNull(declaration.toString());
    }
}