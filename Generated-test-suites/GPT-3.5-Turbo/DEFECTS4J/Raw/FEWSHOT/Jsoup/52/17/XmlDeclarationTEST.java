package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testConstructor() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertNotNull(declaration);
        assertEquals("xml", declaration.name());
        assertFalse(declaration.isProcessingInstruction());
    }

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("xml", declaration.getWholeDeclaration());
        
        declaration.addAttribute("version", "1.0");
        assertEquals("xml version=\"1.0\"", declaration.getWholeDeclaration());
        
        declaration.addAttribute("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            declaration.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(sb.toString().startsWith("<?xml"));

        declaration = new XmlDeclaration("xml", "http://www.example.com", true);
        sb = new StringBuilder();
        try {
            declaration.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(sb.toString().startsWith("<!xml"));
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            declaration.outerHtmlTail(sb, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("<?xml?>", declaration.toString());

        declaration = new XmlDeclaration("xml", "http://www.example.com", true);
        assertEquals("<!xml?>", declaration.toString());
    }
}