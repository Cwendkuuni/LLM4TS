package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class XmlDeclarationTest {
    
    @Test
    public void testXmlDeclarationName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            // Handle exception
        }
        assertEquals("<!xml>", sb.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("<!xml>", xmlDeclaration.toString());
    }
}