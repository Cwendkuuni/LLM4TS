package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommentTest {
    @Test
    public void testNewComment() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testNewCommentDeprecated() {
        Comment comment = new Comment("This is a comment", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());

        comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentIsNotXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclarationNull() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclarationValid() {
        Comment comment = new Comment("<!DOCTYPE html>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentAsXmlDeclarationInvalid() {
        Comment comment = new Comment("This is a comment");
        comment.asXmlDeclaration();
    }
}