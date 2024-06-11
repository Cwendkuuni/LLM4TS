
package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testCommentGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCommentIsXmlDeclaration() {
        Comment comment = new Comment("! Test XML Declaration");
        assertTrue(comment.isXmlDeclaration());

        comment = new Comment("<? Test XML Declaration");
        assertTrue(comment.isXmlDeclaration());

        comment = new Comment("Regular Comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testCommentAsXmlDeclaration() {
        Comment comment = new Comment("! Test XML Declaration");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("!", declaration.getData());
        assertFalse(declaration.attributes().isEmpty());

        comment = new Comment("Regular Comment");
        assertNull(comment.asXmlDeclaration());
    }

}
