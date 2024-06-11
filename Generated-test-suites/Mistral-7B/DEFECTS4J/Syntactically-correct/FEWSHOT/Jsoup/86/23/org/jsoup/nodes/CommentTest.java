package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

public class CommentTest {
    @Test
    public void testNewComment() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.outerHtml());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testNewXmlDeclarationComment() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertEquals("#comment", comment.nodeName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", comment.getData());
        assertEquals("<!--<?xml version=\"1.0\" encoding=\"UTF-8\"?>-->", comment.outerHtml());
        assertTrue(comment.isXmlDeclaration());
        assertEquals(comment.asXmlDeclaration(), Jsoup.parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", null, Parser.xmlParser()).xmlDeclaration());
    }

    @Test
    public void testNewCommentWithBaseUri() {
        Comment comment = new Comment("This is a comment", "/test.html");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
        assertEquals("<!--This is a comment-->", comment.outerHtml());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());

        Comment xmlComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());

        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertEquals("1.0", xmlDeclaration.getVersion());
        assertEquals("UTF-8", xmlDeclaration.getEncoding());
    }
}