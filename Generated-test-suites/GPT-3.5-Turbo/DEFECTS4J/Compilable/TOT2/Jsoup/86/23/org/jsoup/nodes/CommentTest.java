package org.jsoup.nodes;
import org.jsoup.nodes.Comment;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("test comment");
        assertEquals("test comment", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
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
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"utf-8\"?>-->");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"utf-8\"?>-->");
        assertNotNull(comment.asXmlDeclaration());
    }

    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("test comment");
        assertEquals("<!--test comment-->", comment.outerHtml());
    }

}