package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

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
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!-- Test Comment -->");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.outerHtml());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testConstructor() {
        Comment comment = new Comment("Test Comment");
        assertNotNull(comment);
        assertEquals("Test Comment", comment.getData());
    }

}