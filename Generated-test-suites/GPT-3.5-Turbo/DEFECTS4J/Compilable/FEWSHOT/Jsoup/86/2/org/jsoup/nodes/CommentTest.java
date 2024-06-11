
package org.jsoup.nodes;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testConstructor() {
        Comment comment = new Comment("test");
        assertEquals("test", comment.getData());
    }

    @Test
    public void testConstructorWithBaseUri() {
        Comment comment = new Comment("test", "https://example.com");
        assertEquals("test", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("test");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("test");
        assertEquals("test", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("test");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlHead(sb, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!--test-->", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("test");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(sb, 0, out);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("test");
        assertEquals("<!--test-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!xml declaration");
        assertTrue(comment.isXmlDeclaration());

        comment = new Comment("?xml declaration");
        assertTrue(comment.isXmlDeclaration());

        comment = new Comment("just a comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!xml declaration");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("!xml", declaration.nodeName());

        comment = new Comment("?xml declaration");
        declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("?xml", declaration.nodeName());

        comment = new Comment("just a comment");
        assertNull(comment.asXmlDeclaration());
    }
}
