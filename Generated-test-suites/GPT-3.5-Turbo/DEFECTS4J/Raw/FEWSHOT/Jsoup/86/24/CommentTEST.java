
package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CommentTest {
    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("test");
        assertEquals("test", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("test", "http://example.com");
        assertEquals("test", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("test");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetOuterHtml() {
        Comment comment = new Comment("test");
        assertEquals("<!--test-->", comment.outerHtml());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("test");
        assertEquals("<!--test-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!Test");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!Test");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertEquals("!Test", decl.getData());
        assertTrue(decl.attributes().isEmpty());
    }
}
