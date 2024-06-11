package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetCommentData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("This is a comment");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            comment.outerHtmlHead(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown during outerHtmlHead");
        }
        assertEquals("<!--This is a comment-->", stringBuilder.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("This is a comment");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            comment.outerHtmlTail(stringBuilder, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown during outerHtmlTail");
        }
        assertEquals("", stringBuilder.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());

        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());

        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(declaration);
        assertTrue(declaration.toString().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
    }

}