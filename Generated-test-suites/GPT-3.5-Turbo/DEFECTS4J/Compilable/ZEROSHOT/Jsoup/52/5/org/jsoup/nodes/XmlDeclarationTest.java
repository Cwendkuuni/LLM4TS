package org.jsoup.nodes;
import org.junit.*;
import static org.junit.Assert.*;

public class XmlDeclarationTest {

    @Test
    public void testNodeName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        String expected = "#declaration";
        String actual = declaration.nodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testName() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        String expected = "xml";
        String actual = declaration.name();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWholeDeclaration() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        String expected = "xml";
        String actual = declaration.getWholeDeclaration();
        assertEquals(expected, actual);
    }

    @Test
    public void testOuterHtmlHead() {
        // Test case for isProcessingInstruction = false
        XmlDeclaration declaration1 = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder accum1 = new StringBuilder();
        int depth1 = 1;
        Document.OutputSettings out1 = new Document.OutputSettings();

        try {
            declaration1.outerHtmlHead(accum1, depth1, out1);
        } catch (Exception e) {
            fail("Exception occurred");
        }

        String expected1 = "<?xml>";
        String actual1 = accum1.toString();
        assertEquals(expected1, actual1);


        // Test case for isProcessingInstruction = true
        XmlDeclaration declaration2 = new XmlDeclaration("xml", "http://example.com", true);
        StringBuilder accum2 = new StringBuilder();
        int depth2 = 1;
        Document.OutputSettings out2 = new Document.OutputSettings();

        try {
            declaration2.outerHtmlHead(accum2, depth2, out2);
        } catch (Exception e) {
            fail("Exception occurred");
        }

        String expected2 = "<!xml>";
        String actual2 = accum2.toString();
        assertEquals(expected2, actual2);
    }

    @Test
    public void testOuterHtmlTail() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        StringBuilder accum = new StringBuilder();
        int depth = 1;
        Document.OutputSettings out = new Document.OutputSettings();

        try {
            declaration.outerHtmlTail(accum, depth, out);
        } catch (Exception e) {
            fail("Exception occurred");
        }

        String expected = "";
        String actual = accum.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        XmlDeclaration declaration = new XmlDeclaration("xml", "http://example.com", false);
        String expected = "<?xml>";
        String actual = declaration.toString();
        assertEquals(expected, actual);
    }

}