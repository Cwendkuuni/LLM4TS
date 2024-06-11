package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("#declaration", declaration.nodeName());
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("xml", declaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        String wholeDeclaration = declaration.getWholeDeclaration();
        assertEquals("xml", wholeDeclaration);
    }

    @Test
    public void testGetWholeDeclarationWithAttributes() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        declaration.attributes().put("version", "1.0");
        declaration.attributes().put("encoding", "UTF-8");
        String wholeDeclaration = declaration.getWholeDeclaration();
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", wholeDeclaration);
    }

    @Test
    public void testOuterHtmlHead() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            declaration.outerHtmlHead(accum, depth, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!xml>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            declaration.outerHtmlTail(accum, depth, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", true);
        assertEquals("<!xml>", declaration.toString());
    }
}