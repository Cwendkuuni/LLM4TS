package org.jsoup.nodes;

import org.jsoup.helper.Validate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class XmlDeclarationTest {

    @Test
    public void testConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("name", xmlDeclaration.name());
        assertEquals("baseUri", xmlDeclaration.baseUri());
        assertFalse(xmlDeclaration.isProcessingInstruction());
    }

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("name", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        StringBuilder sb = new StringBuilder();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        xmlDeclaration.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<?name>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        StringBuilder sb = new StringBuilder();
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        xmlDeclaration.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("name", "baseUri", false);
        assertEquals("<?name>", xmlDeclaration.toString());
    }
}