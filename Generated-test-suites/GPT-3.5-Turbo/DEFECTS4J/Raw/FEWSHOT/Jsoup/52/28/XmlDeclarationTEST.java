package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class XmlDeclarationTest {
    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.test.com", true);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.test.com", true);
        assertEquals("xml", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.test.com", true);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xml", "http://www.test.com", false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration = new XmlDeclaration("xml", "http://www.test.com", true);
        xmlDeclaration.attributes.put("version", "1.0");
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());

        xmlDeclaration.attributes.put("standalone", "yes");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.test.com", true);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<?xml>", sb.toString());
    }

    @Test
    public void testOuterHtmlHeadProcessingInstruction() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.test.com", false);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!xml>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.test.com", true);
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://www.test.com", true);
        assertEquals("<?xml>", xmlDeclaration.toString());
    }
}