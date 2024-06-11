package org.jsoup.nodes;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;

public class XmlDeclarationTest {
    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("xml", "https://www.example.com", true);
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
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlHead(sb, 0, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("<!xml>", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlTail(sb, 0, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<!xml>", xmlDeclaration.toString());
    }
}