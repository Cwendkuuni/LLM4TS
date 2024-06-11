package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {
    @Test
    public void testComment() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
        assertFalse(comment.isXmlDeclaration());
        assertNull(comment.asXmlDeclaration());
    }
    
    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.outerHtml());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("?", xmlDeclaration.nodeName());
        assertEquals("version", xmlDeclaration.attr("version"));
        assertEquals("1.0", xmlDeclaration.attr("1.0"));
        assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
    }
}