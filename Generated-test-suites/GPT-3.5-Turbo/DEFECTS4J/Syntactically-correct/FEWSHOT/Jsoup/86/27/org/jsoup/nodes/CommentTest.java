package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class CommentTest {
    
    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }
    
    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.outerHtml());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertFalse(comment.isXmlDeclaration());
        
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment2.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertNull(comment.asXmlDeclaration());
        
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment2.asXmlDeclaration();
        assertEquals("xml", declaration.tagName());
        assertEquals(1, declaration.attributes().size());
    }
    
}