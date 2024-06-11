package org.jsoup.nodes;

import org.jsoup.helper.Validate;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 An XML Declaration.

 @author Jonathan Hedley, jonathan@hedley.net */
public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("#declaration", declaration.nodeName());
    }
    
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("xml", declaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        try {
            declaration.outerHtmlHead(sb, 0, outputSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertEquals("<?xml>", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        try {
            declaration.outerHtmlTail(sb, 0, outputSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://www.example.com", false);
        assertEquals("<?xml>", declaration.toString());
    }
}