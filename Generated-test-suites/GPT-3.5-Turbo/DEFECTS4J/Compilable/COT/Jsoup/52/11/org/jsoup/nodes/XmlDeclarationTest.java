package org.jsoup.nodes;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("xml", xmlDeclaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclarationWithVersionAndEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testGetWholeDeclarationWithInvalidAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        xmlDeclaration.attributes().put("random", "value");
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("<?xml>", xmlDeclaration.toString());
    }
}