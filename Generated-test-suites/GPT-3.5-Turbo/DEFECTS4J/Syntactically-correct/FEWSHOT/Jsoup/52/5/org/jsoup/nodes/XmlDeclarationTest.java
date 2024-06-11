package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertNotNull(declaration);
        assertEquals("xml", declaration.name());
        assertFalse(declaration.isProcessingInstruction());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("xml", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            // do nothing
        }
        assertEquals("<?xml>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder sb = new StringBuilder();
        try {
            declaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            // do nothing
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("<?xml>", declaration.toString());
    }
}