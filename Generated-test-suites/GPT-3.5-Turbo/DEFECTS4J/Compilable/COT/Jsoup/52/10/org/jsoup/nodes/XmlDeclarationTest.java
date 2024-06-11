
package org.jsoup.nodes;

import org.jsoup.helper.Validate;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("name", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration_xmlWithAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "baseUri", true);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclaration_otherDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("other", "baseUri", true);
        assertEquals("other", declaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        StringBuilder sb = new StringBuilder();
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        declaration.outerHtmlHead(sb, 0, null);
        assertEquals("<!name>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        StringBuilder sb = new StringBuilder();
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        declaration.outerHtmlTail(sb, 0, null);
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("name", "baseUri", true);
        assertEquals("<!name>", declaration.toString());
    }
}

