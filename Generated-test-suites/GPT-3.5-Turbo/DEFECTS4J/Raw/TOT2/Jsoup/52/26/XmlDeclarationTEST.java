package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertNotNull(declaration);
        assertEquals("xml", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<?xml", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<?xml", declaration.toString());
    }
}