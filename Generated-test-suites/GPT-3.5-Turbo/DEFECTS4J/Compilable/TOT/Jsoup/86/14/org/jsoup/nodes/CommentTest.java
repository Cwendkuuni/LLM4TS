package org.jsoup.nodes;

import org.jsoup.nodes.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class CommentTest {

    @Test
    public void testConstructor() {
        String data = "This is a comment";
        Comment comment = new Comment(data);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("This is a comment");
        String expectedHtml = "<!--This is a comment-->";
        assertEquals(expectedHtml, comment.outerHtml());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("! This is an XML Declaration");
        assertTrue(comment.isXmlDeclaration());

        comment = new Comment("? This is an XML Declaration");
        assertTrue(comment.isXmlDeclaration());

        comment = new Comment("This is not an XML Declaration");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("! This is an XML Declaration");
        assertNotNull(comment.asXmlDeclaration());

        comment = new Comment("? This is an XML Declaration");
        assertNotNull(comment.asXmlDeclaration());

        comment = new Comment("This is not an XML Declaration");
        assertNull(comment.asXmlDeclaration());
    }
}
