package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {

    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
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

        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {

        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();

        try {
            xmlDeclaration.outerHtmlHead(accum, 0, settings);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertEquals("<?xml", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings settings = new Document.OutputSettings();

        xmlDeclaration.outerHtmlTail(accum, 0, settings);

        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

        xmlDeclaration.attributes().put("version", "1.0");
        xmlDeclaration.attributes().put("encoding", "UTF-8");

        assertEquals(expected, xmlDeclaration.toString());
    }
}