package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {
    
    XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com", false);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithoutAttributes() {
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<?xml?>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<?xml?>", xmlDeclaration.toString());
    }
}