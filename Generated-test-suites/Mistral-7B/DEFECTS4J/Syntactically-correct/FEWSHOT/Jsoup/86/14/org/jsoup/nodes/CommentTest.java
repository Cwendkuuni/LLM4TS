package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCommentCreationWithBaseUri() {
        Comment comment = new Comment("This is a comment.", "http://test.com");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
        assertEquals("http://test.com", comment.baseUri());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("This is a comment.");
        assertFalse(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        assertTrue(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("!-- This is not an XML Declaration -->");
        assertFalse(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlComment = new Comment("<?xml version='1.0' encoding='UTF-8'?>");
        XmlDeclaration xmlDeclaration = xmlComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.getNamespace());
        assertTrue(xmlDeclaration.isStandalone());

        Comment invalidXmlComment = new Comment("!invalid xml comment");
        assertNull(invalidXmlComment.asXmlDeclaration());
    }
}