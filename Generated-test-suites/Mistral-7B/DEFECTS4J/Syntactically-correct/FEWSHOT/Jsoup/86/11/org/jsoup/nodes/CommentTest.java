package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import java.io.IOException;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCommentCreationWithBaseUri() {
        Comment comment = new Comment("This is a comment", "/test.html");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
        assertEquals("/test.html", comment.baseUri());
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

        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- This is a comment -->");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());

        Comment xmlDeclarationCommentEmpty = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationCommentEmpty.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() throws IOException {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());

        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- This is a comment -->");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertSame(XmlDeclaration.class, xmlDeclaration.getClass());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<declaration xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xmlns=\"http://www.w3.org/2000/xmlns/\">", xmlDeclaration.outerHtml());
        assertEquals("version", xmlDeclaration.version());
        assertEquals("UTF-8", xmlDeclaration.encoding());

        Comment xmlDeclarationCommentEmpty = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclarationEmpty = xmlDeclarationCommentEmpty.asXmlDeclaration();
        assertSame(XmlDeclaration.class, xmlDeclarationEmpty.getClass());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclarationEmpty.outerHtml());
        assertEquals("version", xmlDeclarationEmpty.version());
        assertEquals("UTF-8", xmlDeclarationEmpty.encoding());
    }
}