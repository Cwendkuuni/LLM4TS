package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testDeprecatedCommentConstructor() {
        Comment comment = new Comment("This is a comment", "https://example.com");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (IOException e) {
            fail("Exception thrown");
        }

        assertEquals("<!--This is a comment-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        comment.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclarationWithXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationWithoutXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithXmlDeclaration() {
        Comment comment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        assertNotNull(comment.asXmlDeclaration());
        assertEquals("<?xml version='1.0' encoding='UTF-8'?>", comment.asXmlDeclaration().toString());
    }

    @Test
    public void testAsXmlDeclarationWithoutXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());
    }
}