package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testXmlDeclaration() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertNotNull(xmlDeclaration.getWholeDeclaration());
        assertSame(baseUri, xmlDeclaration.baseUri());
        assertEquals(isProcessingInstruction, xmlDeclaration.isProcessingInstruction());
    }

    @Test
    public void testXmlDeclarationWithoutAttributes() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertNotNull(xmlDeclaration.getWholeDeclaration());
        assertSame(baseUri, xmlDeclaration.baseUri());
        assertFalse(xmlDeclaration.isProcessingInstruction());
    }

    @Test
    public void testXmlDeclarationWithAttributes() {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("#declaration", xmlDeclaration.nodeName());
        assertEquals(name, xmlDeclaration.name());
        assertNotNull(xmlDeclaration.getWholeDeclaration());
        assertSame(baseUri, xmlDeclaration.baseUri());
        assertFalse(xmlDeclaration.isProcessingInstruction());
        assertEquals("version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);

        assertEquals("<?xml>", accum.toString());
    }

    @Test
    public void testOuterHtmlHeadWithAttributes() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlHead(accum, 0, null);

        assertEquals("<declaration version=\"1.0\" encoding=\"UTF-8\">", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        StringBuilder accum = new StringBuilder();
        xmlDeclaration.outerHtmlTail(accum, 0, null);

        assertTrue(accum.toString().isEmpty());
    }

    @Test
    public void testToString() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = true;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);

        assertEquals(xmlDeclaration.outerHtml(), xmlDeclaration.toString());
    }

    @Test
    public void testToStringWithAttributes() throws IOException {
        String name = "xml";
        String baseUri = "baseUri";
        boolean isProcessingInstruction = false;

        XmlDeclaration xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals(xmlDeclaration.outerHtml(), xmlDeclaration.toString());
    }
}