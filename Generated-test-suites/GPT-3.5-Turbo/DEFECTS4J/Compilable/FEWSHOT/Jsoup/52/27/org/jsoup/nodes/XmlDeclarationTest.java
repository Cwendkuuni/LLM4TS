package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", true);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", true);
        assertEquals("declaration", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration1 = new XmlDeclaration("xml", "baseUri", true);
        declaration1.attributes.put("version", "1.0");
        declaration1.attributes.put("encoding", "UTF-8");
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", declaration1.getWholeDeclaration());

        XmlDeclaration declaration2 = new XmlDeclaration("html", "baseUri", false);
        assertEquals("html", declaration2.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration1 = new XmlDeclaration("declaration", "baseUri", true);
        StringBuilder sb1 = new StringBuilder();
        Document.OutputSettings out1 = new Document.OutputSettings();
        try {
            declaration1.outerHtmlHead(sb1, 0, out1);
        } catch (Exception e) {
            fail();
        }
        assertEquals("<!declaration>", sb1.toString());

        XmlDeclaration declaration2 = new XmlDeclaration("declaration", "baseUri", false);
        StringBuilder sb2 = new StringBuilder();
        Document.OutputSettings out2 = new Document.OutputSettings();
        try {
            declaration2.outerHtmlHead(sb2, 0, out2);
        } catch (Exception e) {
            fail();
        }
        assertEquals("<?declaration>", sb2.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", true);
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            declaration.outerHtmlTail(sb, 0, out);
        } catch (Exception e) {
            fail();
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "baseUri", true);
        assertEquals("<!declaration>", declaration.toString());
    }
}