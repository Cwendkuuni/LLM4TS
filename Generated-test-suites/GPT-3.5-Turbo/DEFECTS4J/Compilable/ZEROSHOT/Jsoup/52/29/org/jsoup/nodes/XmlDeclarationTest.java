package org.jsoup.nodes;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlDeclarationTest {

    @Test
    public void testConstructor() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "baseUri", false);
        assertNotNull(xmlDeclaration);
        assertEquals("declaration", xmlDeclaration.name());
    }

    @Test
    public void testNodeName() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "baseUri", false);
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "baseUri", false);
        assertEquals("declaration", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", false);
        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "baseUri", false);
        StringBuilder stringBuilder = new StringBuilder();
        xmlDeclaration.outerHtmlHead(stringBuilder, 0, new Document.OutputSettings());
        assertEquals("<?declaration>", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "baseUri", false);
        StringBuilder stringBuilder = new StringBuilder();
        xmlDeclaration.outerHtmlTail(stringBuilder, 0, new Document.OutputSettings());
        assertTrue(stringBuilder.toString().isEmpty());
    }

    @Test
    public void testToString() {
        XmlDeclaration xmlDeclaration = new XmlDeclaration("declaration", "baseUri", false);
        assertEquals("<?declaration>", xmlDeclaration.toString());
    }
}

