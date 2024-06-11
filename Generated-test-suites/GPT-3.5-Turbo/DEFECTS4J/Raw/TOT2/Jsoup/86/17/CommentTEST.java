package org.jsoup.nodes;

import org.jsoup.nodes.Comment;
import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclarationWithData() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithoutData() {
        Comment comment = new Comment("Test Comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithValidXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        assertNotNull(comment.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidXmlDeclaration() {
        Comment comment = new Comment("Test Comment");
        assertNull(comment.asXmlDeclaration());
    }

    // Add more edge cases and exception handling scenarios tests

}