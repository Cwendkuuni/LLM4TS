package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("name", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithVersion() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        xmlDeclaration.attributes.put("version", "1.0");
        assertEquals("xml version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithEncoding() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws Exception {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<?xml?>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadProcessingInstruction() throws Exception {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!xml?>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws Exception {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        assertEquals("<?xml?>", xmlDeclaration.toString());
    }
}

