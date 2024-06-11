package org.jsoup.nodes;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {
    
    @Test
    public void testCommentString() {
        Comment comment = new Comment("Testing comment");
        assertEquals("Testing comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testCommentStringBaseUri() {
        Comment comment = new Comment("Testing comment", "http://example.com");
        assertEquals("Testing comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Testing comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(accum.toString().startsWith("<!--Testing comment"));
        assertTrue(accum.toString().endsWith("comment-->"));
    }
    
    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Testing comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("Testing comment");
        assertEquals("<!--Testing comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());
        
        Comment comment2 = new Comment("<?xml version=\"1.0\"?>");
        assertTrue(comment2.isXmlDeclaration());
        
        Comment comment3 = new Comment("Testing comment");
        assertFalse(comment3.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("doctype", xmlDeclaration.nodeName());
        
        Comment comment2 = new Comment("<?xml version=\"1.0\"?>");
        XmlDeclaration xmlDeclaration2 = comment2.asXmlDeclaration();
        assertNotNull(xmlDeclaration2);
        assertEquals("xml", xmlDeclaration2.nodeName());
        
        Comment comment3 = new Comment("Testing comment");
        XmlDeclaration xmlDeclaration3 = comment3.asXmlDeclaration();
        assertNull(xmlDeclaration3);
    }
}