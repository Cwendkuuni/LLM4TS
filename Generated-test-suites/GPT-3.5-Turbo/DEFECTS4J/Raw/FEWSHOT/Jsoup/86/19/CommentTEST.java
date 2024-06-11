package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

public class CommentTest {
    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCommentConstructorWithBaseUri() {
        Comment comment = new Comment("This is a comment", "https://example.com");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.outerHtml());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("! This is an XML declaration");
        assertTrue(comment.isXmlDeclaration());
        
        Comment comment2 = new Comment("? This is an XML declaration");
        assertTrue(comment2.isXmlDeclaration());
        
        Comment comment3 = new Comment("Not an XML declaration");
        assertFalse(comment3.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!xml version=\"1.0\"");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertEquals("xml", declaration.nodeName());
        assertEquals("version=\"1.0\"", declaration.attributes().toString());
    }
    
    @Test
    public void testAsXmlDeclarationNotParsed() {
        Comment comment = new Comment("Not an XML declaration");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNull(declaration);
    }
}