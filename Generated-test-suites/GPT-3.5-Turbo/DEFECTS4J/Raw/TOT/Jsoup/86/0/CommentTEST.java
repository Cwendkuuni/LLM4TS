package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class CommentTest {

    // Test cases for Comment(String data)
    @Test
    public void testCommentConstructorWithData() {
        String commentData = "This is a comment.";
        Comment comment = new Comment(commentData);
        
        assertEquals(commentData, comment.getData());
    }
    
    // Test cases for Comment(String data, String baseUri)
    @Test
    public void testCommentConstructorWithDataAndBaseUri() {
        String commentData = "This is a comment.";
        String baseUri = "http://example.com";
        Comment comment = new Comment(commentData, baseUri);
        
        assertEquals(commentData, comment.getData());
    }
    
    // Test case for nodeName()
    @Test
    public void testNodeName() {
        Comment comment = new Comment("");
        
        assertEquals("#comment", comment.nodeName());
    }
    
    // Test cases for getData()
    @Test
    public void testGetData() {
        String commentData = "This is a comment.";
        Comment comment = new Comment(commentData);
        
        assertEquals(commentData, comment.getData());
    }
    
    // Test cases for outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out)
    // Since outerHtmlHead is package-private, we will test it indirectly by testing outerHtml()
    
    // Test case for outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out)
    // Since outerHtmlTail is package-private and does not have any functionality, there are no separate test cases for it
    
    // Test case for toString()
    @Test
    public void testToString() {
        String commentData = "This is a comment.";
        Comment comment = new Comment(commentData);
        
        assertEquals("<!--This is a comment.-->", comment.toString());
    }
    
    // Test case for isXmlDeclaration()
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\" ?>-->");
        
        assertTrue(comment.isXmlDeclaration());
    }
    
    // Test case for asXmlDeclaration()
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\" ?>-->");
        
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        
        assertNotNull(xmlDeclaration);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>", xmlDeclaration.outerHtml());
    }
}