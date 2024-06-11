package org.jsoup.nodes;

import org.junit.Test;

public class XmlDeclarationTest {

    // Test for nodeName() method
    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com/", false);
        String expected = "#declaration";
        assertEquals(expected, declaration.nodeName());
    }

    // Test for name() method
    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com/", false);
        String expected = "declaration";
        assertEquals(expected, declaration.name());
    }

    // Test for getWholeDeclaration() method - XML declaration with attributes
    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com/", false);
        declaration.attributes.put("version", "1.0");
        declaration.attributes.put("encoding", "UTF-8");
        String expected = "xml version=\"1.0\" encoding=\"UTF-8\"";
        assertEquals(expected, declaration.getWholeDeclaration());
    }

    // Test for getWholeDeclaration() method - XML declaration without attributes
    @Test
    public void testGetWholeDeclarationWithoutAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com/", false);
        String expected = "declaration";
        assertEquals(expected, declaration.getWholeDeclaration());
    }

    // Test for outerHtmlHead() method
    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com/", false);
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            declaration.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        String expected = "<?declaration>";
        assertEquals(expected, accum.toString());
    }

    // Test for outerHtmlTail() method
    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com/", false);
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        declaration.outerHtmlTail(accum, 0, out);
        String expected = "";
        assertEquals(expected, accum.toString());
    }

    // Test for toString() method
    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("declaration", "http://example.com/", false);
        String expected = "<?declaration>";
        assertEquals(expected, declaration.toString());
    }
}