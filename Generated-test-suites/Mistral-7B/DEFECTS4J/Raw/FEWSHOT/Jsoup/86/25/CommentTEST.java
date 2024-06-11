package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.IOException;

public class CommentTest {

    @Test
    public void testNewComment() {
        Comment comment = new Comment("Hello World");
        assertEquals("#comment", comment.nodeName());
        assertEquals("Hello World", comment.getData());
    }

    @Test
    public void testNewCommentDeprecated() {
        Comment comment = new Comment("Hello World", null);
        assertEquals("#comment", comment.nodeName());
        assertEquals("Hello World", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("# comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        Comment comment = new Comment("# comment");
        assertEquals("# comment", comment.getData());
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Comment comment = new Comment("Hello World");
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlHead(sb, 0, null);
        assertEquals("<!--Hello World-->", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Comment comment = new Comment("Hello World");
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlTail(sb, 0, null);
        assertTrue(sb.toString().isEmpty());
    }

    @Test
    public void testToString() throws IOException {
        Comment comment = new Comment("Hello World");
        assertEquals("<!--Hello World-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!-- foobar -->");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("Hello World");
        assertFalse(comment2.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationNull() {
        Comment comment = new Comment("<!-- foobar -->");
        assertNull(comment.asXmlDeclaration());

        Comment comment2 = new Comment("Hello World");
        assertNull(comment2.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationValid() throws IOException {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertSame(XmlDeclaration.class, xmlDeclaration.getClass());
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertEquals("1.0", xmlDeclaration.version());
        assertTrue(xmlDeclaration.isStandalone());
        assertFalse(xmlDeclaration.isExternalSubset());
    }
}