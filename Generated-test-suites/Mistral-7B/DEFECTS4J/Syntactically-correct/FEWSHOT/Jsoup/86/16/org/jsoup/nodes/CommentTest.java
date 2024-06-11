package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!-- This is a comment -->");
        assertTrue(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("-- This is a comment --");
        assertTrue(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("This is a comment");
        assertFalse(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element />\n</root>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
        assertEquals("xml", xmlDeclaration.getNamespace());
        assertTrue(xmlDeclaration.isStandalone());

        assertNull(new Comment("This is a comment").asXmlDeclaration());
        assertNull(new Comment("<!-- This is a comment -->").asXmlDeclaration());
    }
}