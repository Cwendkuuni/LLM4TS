package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

public class CommentTest {

    private Comment comment;

    @Before
    public void setup() {
        comment = new Comment("Test Comment");
    }

    @Test
    public void testConstructor() {
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testOuterHtml() {
        StringBuilder accum = new StringBuilder();
        OutputSettings out = new OutputSettings();
        try {
            comment.outerHtmlHead(accum, 0, out);
            comment.outerHtmlTail(accum, 0, out);
            assertEquals("<!--Test Comment-->", accum.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testToString() {
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration_ValidXmlDeclaration() {
        Comment xmlDec = new Comment("!DOCTYPE html");
        XmlDeclaration declaration = xmlDec.asXmlDeclaration();
        assertTrue(declaration != null && declaration.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration_InvalidXmlDeclaration() {
        Comment xmlDec = new Comment("Not an XML Declaration");
        assertNull(xmlDec.asXmlDeclaration());
    }
}