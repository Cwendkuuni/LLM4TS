package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {
    @Test
    public void testXmlNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testXmlDeclarationName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("xml", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("xml", declaration.getWholeDeclaration());
        
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        try {
            declaration.outerHtmlHead(sb, 0, settings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<?xml>", sb.toString());

        declaration = new XmlDeclaration("xml", "baseUri", true);
        sb = new StringBuilder();
        try {
            declaration.outerHtmlHead(sb, 0, settings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!xml>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();
        try {
            declaration.outerHtmlTail(sb, 0, settings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("<?xml>", declaration.toString());
    }
}