package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testConstructor() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testConstructorWithBaseUri() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test Comment");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        StringBuilder sb = new StringBuilder();
        try {
            comment.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!--Test Comment-->", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test Comment");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder sb = new StringBuilder();
        try {
            comment.outerHtmlTail(sb, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("! Test Comment");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("? Test Comment");
        assertTrue(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("Regular Comment");
        assertFalse(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("! Test Comment");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertEquals("!", xmlDeclaration.nodeName());

        Comment comment2 = new Comment("? Test Comment");
        XmlDeclaration xmlDeclaration2 = comment2.asXmlDeclaration();
        assertEquals("?", xmlDeclaration2.nodeName());

        Comment comment3 = new Comment("Regular Comment");
        XmlDeclaration xmlDeclaration3 = comment3.asXmlDeclaration();
        assertNull(xmlDeclaration3);
    }

}