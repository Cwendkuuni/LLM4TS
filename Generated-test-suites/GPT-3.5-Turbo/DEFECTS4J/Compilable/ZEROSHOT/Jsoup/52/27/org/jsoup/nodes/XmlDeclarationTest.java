package org.jsoup.nodes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlDeclarationTest {
    private XmlDeclaration xmlDeclaration;


    @Before
    public void setup() {
        xmlDeclaration = new XmlDeclaration("xml", "http://example.com", true);
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
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlHead(accum, depth, out);
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
        assertEquals("<!xml>", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlTail(accum, depth, out);
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<!xml>", xmlDeclaration.toString());
    }
}