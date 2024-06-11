package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for Comment class.
 */
public class CommentTest {

    /**
     * Test constructor with data.
     */
    @Test
    public void testCommentWithData() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    /**
     * Test constructor with data and deprecated baseUri.
     */
    @Test
    public void testCommentWithDataAndDeprecatedBaseUri() {
        Comment comment = new Comment("This is a comment.", "baseUri");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    /**
     * Test nodeName method.
     */
    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
    }

    /**
     * Test getData method.
     */
    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
    }

    /**
     * Test outerHtmlHead method.
     */
    @Test
    public void testOuterHtmlHead() throws Exception {
        Comment comment = new Comment("This is a comment.");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, null);
        assertEquals("<!--This is a comment-->", accum.toString());
    }

    /**
     * Test outerHtmlTail method.
     */
    @Test
    public void testOuterHtmlTail() throws Exception {
        Comment comment = new Comment("This is a comment.");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlTail(accum, 0, null);
        assertTrue(accum.toString().isEmpty());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    /**
     * Test isXmlDeclaration method.
     */
    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("This is a comment.");
        Comment comment2 = new Comment("!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"");
        assertFalse(comment1.isXmlDeclaration());
        assertTrue(comment2.isXmlDeclaration());
    }

    /**
     * Test asXmlDeclaration method with valid XML Declaration comment.
     */
    @Test
    public void testAsXmlDeclarationWithValidXmlDeclaration() {
        Comment comment = new Comment("!--<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertTrue(xmlDeclaration != null);
    }

    /**
     * Test asXmlDeclaration method with invalid comment.
     */
    @Test
    public void testAsXmlDeclarationWithInvalidComment() {
        Comment comment = new Comment("This is a comment.");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertTrue(xmlDeclaration == null);
    }
}