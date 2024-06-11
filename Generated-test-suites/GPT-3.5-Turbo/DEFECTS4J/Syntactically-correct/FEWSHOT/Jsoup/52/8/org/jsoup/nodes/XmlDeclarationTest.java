package org.jsoup.nodes;

import org.jsoup.helper.Validate;

import java.io.IOException;

/**
 * Tests for the XmlDeclaration class.
 */
public class XmlDeclarationTest {

    @Test
    public void testGetName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration1 = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", xmlDeclaration1.getWholeDeclaration());
        
        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration2.getWholeDeclaration());
        
        XmlDeclaration xmlDeclaration3 = new XmlDeclaration("xml", "http://example.com", true);
        xmlDeclaration3.attributes.put("version", "1.0");
        xmlDeclaration3.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration3.getWholeDeclaration());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<!xml>", xmlDeclaration.toString());
    }
}