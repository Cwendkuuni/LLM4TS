package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testNewComment() {
        Comment comment = new Comment("This is a test comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a test comment.", comment.getData());
    }

    @Test
    public void testNewCommentWithBaseUri() {
        Comment comment = new Comment("This is a test comment.", "http://example.com");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a test comment.", comment.getData());
        assertEquals("http://example.com", comment.baseUri());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("#comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testgetData() {
        Comment comment = new Comment("This is a test comment.");
        assertEquals("This is a test comment.", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("This is a test comment.");
        assertFalse(comment.isXmlDeclaration());

        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());

        Comment xmlDeclarationCommentWithData = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>This is a test comment.");
        assertTrue(xmlDeclarationCommentWithData.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());

        Comment xmlDeclarationCommentWithData = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>This is a test comment.");
        XmlDeclaration xmlDeclarationWithData = xmlDeclarationCommentWithData.asXmlDeclaration();
        assertNotNull(xmlDeclarationWithData);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclarationWithData.toString());

        Comment comment = new Comment("This is a test comment.");
        assertNull(comment.asXmlDeclaration());
    }
}