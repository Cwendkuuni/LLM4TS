package org.jsoup.nodes;
import org.jsoup.nodes.Comment;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommentTest {

    private Comment comment;

    @Before
    public void setUp() {
        comment = new Comment("This is a test comment");
    }

    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclarationTrue() {
        Comment xmlDeclaration = new Comment("<!DOCTYPE html>");
        assertEquals(true, xmlDeclaration.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationFalse() {
        assertEquals(false, comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationNotNull() {
        Comment xmlDeclaration = new Comment("<!--!DOCTYPE html-->");
        assertEquals(true, xmlDeclaration.asXmlDeclaration() != null);
    }

    @Test
    public void testAsXmlDeclarationNull() {
        assertEquals(null, comment.asXmlDeclaration());
    }
}