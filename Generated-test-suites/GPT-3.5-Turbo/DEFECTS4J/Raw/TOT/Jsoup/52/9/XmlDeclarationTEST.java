
package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    
    // Testing the constructor
    @Test
    public void testXmlDeclarationConstructor() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", true);
        
        assertEquals("xml", declaration.name());
        assertEquals("http://www.example.com", declaration.baseUri());
        assertTrue(declaration.isProcessingInstruction());
    }
    
    // Testing the nodeName() method
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", true);
        
        assertEquals("#declaration", declaration.nodeName());
    }
    
    // Testing the getWholeDeclaration() method
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", true);
        
        assertEquals("<?xml?>", declaration.getWholeDeclaration());
    }
    
    // Testing the outerHtmlHead() method
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", true);
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        try {
            declaration.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("An exception occurred");
        }
        
        assertEquals("<?xml?>", accum.toString());
    }
    
    // Testing the outerHtmlTail() method
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", true);
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        
        try {
            declaration.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail("An exception occurred");
        }
        
        assertEquals("", accum.toString());
    }
    
    // Testing the toString() method
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", true);
        
        assertEquals("<?xml?>", declaration.toString());
    }
}
