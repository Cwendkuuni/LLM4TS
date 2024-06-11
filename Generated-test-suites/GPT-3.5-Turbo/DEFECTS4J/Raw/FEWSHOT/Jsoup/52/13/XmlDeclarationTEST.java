package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("#declaration", declaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("name", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", false);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
        
        declaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("name", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlHead(sb, 0, null);
        assertEquals("<!name>", sb.toString());
        
        declaration = new XmlDeclaration("name", "baseUri", false);
        sb = new StringBuilder();
        declaration.outerHtmlHead(sb, 0, null);
        assertEquals("<?name>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        StringBuilder sb = new StringBuilder();
        declaration.outerHtmlTail(sb, 0, null);
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("<!name>", declaration.toString());
    }
}