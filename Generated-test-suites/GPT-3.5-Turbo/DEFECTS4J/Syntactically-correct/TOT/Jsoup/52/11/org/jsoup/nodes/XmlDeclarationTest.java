package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {
   
    private XmlDeclaration xmlDeclaration;
    private final String name = "xml";
    private final String baseUri = "https://www.example.com";
    private final boolean isProcessingInstruction = false;

    @Before
    public void setup() {
       xmlDeclaration = new XmlDeclaration(name, baseUri, isProcessingInstruction);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testName() {
        assertEquals(name, xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclarationWithXmlAndAttributes() {
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        String expected = "xml version=\"1.0\" encoding=\"UTF-8\"";
        assertEquals(expected, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithXmlAndNoAttributes() {
        String expected = "xml";
        assertEquals(expected, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testGetWholeDeclarationWithOtherDeclaration() {
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        xmlDeclaration.attributes.put("other", "value");
        String expected = "xml";
        assertEquals(expected, xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHeadWithProcessingInstruction() {
        StringBuilder sb = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        xmlDeclaration.outerHtmlHead(sb, depth, out);
        String expected = "<?xml>";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testOuterHtmlHeadWithDeclaration() {
        xmlDeclaration.attributes.put("version", "1.0");
        xmlDeclaration.attributes.put("encoding", "UTF-8");
        StringBuilder sb = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        xmlDeclaration.outerHtmlHead(sb, depth, out);
        String expected = "<xml version=\"1.0\" encoding=\"UTF-8\">";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        xmlDeclaration.outerHtmlTail(sb, depth, out);
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        String expected = "<xml>";
        assertEquals(expected, xmlDeclaration.toString());
    }

}