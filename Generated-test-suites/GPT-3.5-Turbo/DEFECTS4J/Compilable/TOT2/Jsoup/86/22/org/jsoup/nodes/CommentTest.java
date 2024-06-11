package org.jsoup.nodes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("Test Comment", "http://example.com");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!DOCTYPE html>");
        assertTrue(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        assertTrue(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("Regular Comment");
        assertFalse(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("xml", xmlDeclaration.attributes().get("version"));
        assertEquals("UTF-8", xmlDeclaration.attributes().get("encoding"));
    }
}