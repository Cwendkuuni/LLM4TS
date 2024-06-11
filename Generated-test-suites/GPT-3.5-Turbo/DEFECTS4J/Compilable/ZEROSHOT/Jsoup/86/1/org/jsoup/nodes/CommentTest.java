package org.jsoup.nodes;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CommentTest {

    private Comment comment;

    @Before
    public void setUp() {
        comment = new Comment("This is a comment");
    }

    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testToString() {
        assertEquals("<!--This is a comment-->", comment.toString());
    }

}