
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlDeclarationTest {
    @Test
    public void testXmlDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com/xml", false);
        assertNotNull(xmlDeclaration);
        assertEquals("http://www.example.com/xml", xmlDeclaration.baseUri());
        assertFalse(xmlDeclaration.isProcessingInstruction());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com/xml", false);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com/xml", false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com/xml", false);
        xmlDeclaration.attributes().put("version", "1.0");
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com/xml", false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("foo", "http://www.example.com/xml", false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("foo", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtml() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com/xml", false);
        assertEquals("<?xml>", xmlDeclaration.outerHtml());

        xmlDeclaration = new XmlDeclaration("foo", "http://www.example.com/xml", false);
        assertEquals("<?foo>", xmlDeclaration.outerHtml());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.example.com/xml", false);
        assertEquals("<?xml>", xmlDeclaration.toString());

        xmlDeclaration = new XmlDeclaration("foo", "http://www.example.com/xml", false);
        assertEquals("<?foo>", xmlDeclaration.toString());
    }
}
