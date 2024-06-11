package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

public class XmlDeclarationTest {
    private XmlDeclaration xmlDeclaration;

    @Before
    public void setUp() {
        xmlDeclaration = new XmlDeclaration("test", "http://www.example.com", false);
    }

    @Test
    public void testNodeName() {
        assertEquals("#declaration", xmlDeclaration.nodeName());
    }

    @Test
    public void testConstructor() {
        assertNotNull(xmlDeclaration);
    }

    @Test
    public void testGetName() {
        assertEquals("test", xmlDeclaration.name());
    }

    @Test
    public void testGetWholeDeclaration() {
        assertEquals("test", xmlDeclaration.getWholeDeclaration());
    }

    @Test
    public void testOuterHtmlHead() {
        StringBuilder builder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlHead(builder, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(builder.toString().contains("<!test>"));
    }

    @Test
    public void testOuterHtmlTail() {
        StringBuilder builder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            xmlDeclaration.outerHtmlTail(builder, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(builder.toString().isEmpty());
    }

    @Test
    public void testToString() {
        assertNotNull(xmlDeclaration.outerHtml());
    }
}