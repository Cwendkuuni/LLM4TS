package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class XmlDeclarationTest {

    @Test
    public void testConstructor() {
        String name = "xml";
        String baseUri = "http://example.com";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertNotNull(xmlDeclaration);
        assertEquals(name, xmlDeclaration.name());
    }

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);

        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration1 = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", xmlDeclaration1.getWholeDeclaration());

        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("xml", "http://example.com", false);
        assertEquals("xml", xmlDeclaration2.getWholeDeclaration());

        xmlDeclaration2.attributes().put("version", "1.0");
        xmlDeclaration2.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration2.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration xmlDeclaration1 = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder accum1 = new StringBuilder();
        int depth1 = 0;
        Document.OutputSettings out1 = new Document.OutputSettings();
        out1.prettyPrint(true);
        try {
            xmlDeclaration1.outerHtmlHead(accum1, depth1, out1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!xml>", accum1.toString());

        XmlDeclaration xmlDeclaration2 = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder accum2 = new StringBuilder();
        int depth2 = 0;
        Document.OutputSettings out2 = new Document.OutputSettings();
        out2.prettyPrint(true);
        try {
            xmlDeclaration2.outerHtmlHead(accum2, depth2, out2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<?xml>", accum2.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        try {
            xmlDeclaration.outerHtmlTail(accum, depth, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<!xml>", xmlDeclaration.toString());
    }
}