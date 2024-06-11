package org.jsoup.nodes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testCommentCreation() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCommentCreationWithDeprecatedConstructor() {
        Comment comment = new Comment("This is a comment.", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment.");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("This is a comment.");
        assertFalse(comment.isXmlDeclaration());

        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());

        Comment noXmlDeclarationComment = new Comment("This is a comment.");
        assertNull(noXmlDeclarationComment.asXmlDeclaration());
    }
}