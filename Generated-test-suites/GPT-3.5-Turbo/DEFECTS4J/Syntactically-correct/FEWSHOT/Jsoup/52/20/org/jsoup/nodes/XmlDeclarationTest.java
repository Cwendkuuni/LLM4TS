package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://www.example.com", true);
        assertEquals("#declaration", declaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://www.example.com", true);
        assertEquals("declaration", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration1 = new XmlDeclaration("xml", "http://www.example.com", false);
        declaration1.attributes.put("version", "1.0");
        declaration1.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration1.getWholeDeclaration());
        
        XmlDeclaration declaration2 = new XmlDeclaration("declaration", "http://www.example.com", false);
        assertEquals("declaration", declaration2.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration1 = new XmlDeclaration("xml", "http://www.example.com", false);
        declaration1.attributes.put("version", "1.0");
        declaration1.attributes.put("encoding", "UTF-8");
        StringBuilder sb1 = new StringBuilder();
        declaration1.outerHtmlHead(sb1, 0, null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\">", sb1.toString());
        
        XmlDeclaration declaration2 = new XmlDeclaration("declaration", "http://www.example.com", true);
        StringBuilder sb2 = new StringBuilder();
        declaration2.outerHtmlHead(sb2, 0, null);
        assertEquals("<!declaration>", sb2.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://www.example.com", true);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlTail(sb, 0, null);
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://www.example.com", true);
        assertEquals("<!declaration>", declaration.toString());
    }
}