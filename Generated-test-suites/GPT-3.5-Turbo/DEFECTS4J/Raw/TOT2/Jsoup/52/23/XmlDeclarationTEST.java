package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", false);
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
    public void testGetWholeDeclaration() {
        assertEquals("xml", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testIsProcessingInstruction() {
        xmlDeclaration = new XmlDeclaration("test", "http://example.com", true);
        assertEquals("test version=\"1.0\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<?xml version=\"1.0\">", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        xmlDeclaration.outerHtmlTail(sb, 0, new Document.OutputSettings()); // no operation in the method, nothing to test
    }

    @Test
    public void testToString() {
        assertEquals("<?xml version=\"1.0\">", xmlDeclaration.toString());
    }
}