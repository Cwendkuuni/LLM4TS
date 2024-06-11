package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com", true);
        assertEquals("#declaration", declaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com", true);
        assertEquals("declaration", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        declaration.attributes().put("version", "1.0");
        declaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclarationWithoutAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder expected = new StringBuilder();
        expected.append("<!xml>");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            declaration.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception occurred while executing outerHtmlHead: " + e.getMessage());
        }
        assertEquals(expected.toString(), accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder expected = new StringBuilder();
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            declaration.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail("Exception occurred while executing outerHtmlTail: " + e.getMessage());
        }
        assertEquals(expected.toString(), accum.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<!xml>", declaration.toString());
    }
}

