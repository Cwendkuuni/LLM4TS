package org.jsoup.nodes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CommentTest {

    @Test
    public void testNewComment() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testNewCommentDeprecated() {
        Comment comment = new Comment("This is a comment", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("This is a comment");
        assertFalse(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("!DOCTYPE html SYSTEM \"about:legacy-compat\");");
        assertTrue(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.toString());
    }

    @Test
    public void testAsXmlDeclarationNull() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());
    }
}