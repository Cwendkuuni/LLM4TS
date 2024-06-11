
package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.io.IOException;

import static org.junit.Assert.*;

public class CommentTest {
    @Test
    public void testComment() {
        Comment comment = new Comment("test");

        assertEquals("test", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testCommentWithBaseUri() {
        Comment comment = new Comment("test", "http://example.com");

        assertEquals("test", comment.getData());
        assertEquals("#comment", comment.nodeName());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Comment comment = new Comment("test");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        comment.outerHtmlHead(accum, 0, out);

        assertEquals("<!--test-->", accum.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Comment comment = new Comment("test");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();

        comment.outerHtmlTail(accum, 0, out);

        assertEquals("", accum.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("test");

        assertEquals("<!--test-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("!");

        assertTrue(comment1.isXmlDeclaration());

        Comment comment2 = new Comment("?");

        assertTrue(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("!");

        assertFalse(comment3.isXmlDeclaration());

        Comment comment4 = new Comment("test");

        assertFalse(comment4.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        XmlDeclaration xmlDeclaration1 = comment1.asXmlDeclaration();

        assertNotNull(xmlDeclaration1);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration1.outerHtml());

        Comment comment2 = new Comment("test");

        XmlDeclaration xmlDeclaration2 = comment2.asXmlDeclaration();

        assertNull(xmlDeclaration2);
    }
}
