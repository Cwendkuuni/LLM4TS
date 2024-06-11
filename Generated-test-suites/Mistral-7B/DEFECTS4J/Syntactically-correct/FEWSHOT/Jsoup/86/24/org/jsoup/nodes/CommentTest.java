package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentDeprecatedConstructor() {
        Comment comment = new Comment("This is a comment", "");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("!-- XML declaration --");
        assertTrue(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("-- This is a comment --");
        assertFalse(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("This is a comment");
        assertFalse(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version='1.0' encoding='UTF-8'?>", xmlDeclaration.toString());

        Comment emptyComment = new Comment("");
        assertNull(emptyComment.asXmlDeclaration());
    }

    @Test
    public void testCommentOuterHtmlHead() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, null);
        assertEquals("<!--This is a comment-->", accum.toString());
    }

    @Test
    public void testCommentOuterHtmlTail() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlTail(accum, 0, null);
        assertEquals("", accum.toString());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }
}