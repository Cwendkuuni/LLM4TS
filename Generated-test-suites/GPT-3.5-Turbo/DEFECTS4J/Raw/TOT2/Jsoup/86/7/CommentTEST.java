package org.jsoup.nodes;

import org.jsoup.nodes.Comment;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("Test comment");
        assertNotNull(comment);
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("Test comment", "http://example.com");
        assertNotNull(comment);
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!DOCTYPE");
        assertTrue(comment.isXmlDeclaration());

        Comment nonXmlDeclaration = new Comment("Regular comment");
        assertFalse(nonXmlDeclaration.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("!DOCTYPE html");
        assertNotNull(xmlDeclarationComment.asXmlDeclaration());
    }
}