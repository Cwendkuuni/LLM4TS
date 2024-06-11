package org.jsoup.nodes;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlDeclarationTest {
    private XmlDeclaration decl;

    @Before
    public void setUp() {
        decl = new XmlDeclaration("declaration", "http://example.com", true);
        decl.attributes().put("version", "1.0");
        decl.attributes().put("encoding", "UTF-8");
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", decl.nodeName());
    }

    @Test
    public void testName() {
        assertEquals("declaration", decl.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        assertEquals("declaration version=\"1.0\" encoding=\"UTF-8\"", decl.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder sb = new StringBuilder();
        try {
            decl.outerHtmlHead(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("<!declaration version=\"1.0\" encoding=\"UTF-8\">", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder sb = new StringBuilder();
        try {
            decl.outerHtmlTail(sb, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        assertEquals("<!declaration version=\"1.0\" encoding=\"UTF-8\">", decl.toString());
    }
}