package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Comment;

public class CommentTest {

    @Test
    public void testCommentNodeCreation() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!DOCTYPE");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!DOCTYPE");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertNotNull(decl);
        assertEquals("!", decl.getDeclarationData());
    }

    @Test
    public void testCommentNodeToString() {
        Comment comment = new Comment("Test Comment");
        assertEquals("<!--Test Comment-->", comment.toString());
    }

    @Test
    public void testEmptyCommentNode() {
        Comment comment = new Comment("");
        assertEquals("", comment.getData());
    }

}