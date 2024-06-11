package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CommentTest {
    @Test
    public void testComment() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());        
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());        
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlHead(sb, 0, new Document.OutputSettings());
        assertEquals("<!--This is a comment-->", sb.toString());
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        Comment comment = new Comment("This is a comment");
        StringBuilder sb = new StringBuilder();
        comment.outerHtmlTail(sb, 0, new Document.OutputSettings());
        assertEquals("", sb.toString());
    }

    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!This is a comment");
        assertTrue(comment.isXmlDeclaration());

        Comment comment2 = new Comment("?This is a comment");
        assertTrue(comment2.isXmlDeclaration());

        Comment comment3 = new Comment("This is not an XML Declaration");
        assertFalse(comment3.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertEquals("xml", declaration.nodeName());
        assertTrue(declaration.hasAttributes());
    }
}