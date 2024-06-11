package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructorWithData() {
        Comment comment = new Comment("test comment");
        assertEquals("test comment", comment.getData());
    }

    @Test
    public void testCommentConstructorWithBaseUri() {
        Comment comment = new Comment("test comment", "http://example.com");
        assertEquals("test comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertNotNull(comment.asXmlDeclaration());
    }

    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("test comment");
        assertEquals("<!--test comment-->", comment.outerHtml());
    }
}