package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {
   
    private XmlDeclaration xmlDeclaration;
    
    @Before 
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("declaration", "baseUri", true);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }
    
    @Test
    public void testName() {
        assertEquals("declaration", xmlDeclaration.name());
    }
    
    @Test
    public void testGetWholeDeclaration() {
        assertEquals("declaration", xmlDeclaration.getWholeDeclaration());
    }
    
    @Test
    public void testOuterHtmlHead() {
        //  ADD ASSERTION HERE
    }
    
    @Test
    public void testOuterHtmlTail() {
        //  ADD ASSERTION HERE
    }
    
    @Test
    public void testToString() {
        //  ADD ASSERTION HERE
    }
}