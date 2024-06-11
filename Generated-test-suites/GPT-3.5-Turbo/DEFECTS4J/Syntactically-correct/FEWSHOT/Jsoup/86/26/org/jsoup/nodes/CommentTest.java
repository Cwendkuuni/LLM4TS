
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CommentTest {
    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertEquals("This is a comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("This is a comment");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            comment.outerHtmlHead(sb, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("<!--This is a comment-->", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("This is a comment");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(sb, 0, outputSettings);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!--This is not an XML Declaration-->");
        Comment comment2 = new Comment("<!DOCTYPE html>");
        Comment comment3 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        
        assertFalse(comment1.isXmlDeclaration());
        assertFalse(comment2.isXmlDeclaration());
        assertTrue(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<!--This is not an XML Declaration-->");
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        Comment comment3 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>");
        
        assertNull(comment1.asXmlDeclaration());
        assertNotNull(comment2.asXmlDeclaration());
        assertNull(comment3.asXmlDeclaration());
    }
}
