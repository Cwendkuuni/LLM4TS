package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.io.IOException;

public class CommentTest {
    @Test
    public void testCommentConstructorWithData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCommentConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("This is a comment");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder accum = new StringBuilder();
        try {
            comment.outerHtmlHead(accum, 0, outputSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("<!--This is a comment-->", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("This is a comment");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        StringBuilder accum = new StringBuilder();
        try {
            comment.outerHtmlTail(accum, 0, outputSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!This is an XML Declaration");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testIsNotXmlDeclaration() {
        Comment comment = new Comment("This is not an XML Declaration");
        assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!This is an XML Declaration");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("!", xmlDeclaration.getData());
    }
    
    @Test
    public void testAsXmlDeclarationNotParsed() {
        Comment comment = new Comment("This is not an XML Declaration");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNull(xmlDeclaration);
    }
}