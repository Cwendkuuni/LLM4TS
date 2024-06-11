
package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class CommentTest {
    
    @Test
    public void testConstructorWithData() {
        String data = "Test Comment";
        Comment comment = new Comment(data);
        Assert.assertEquals(data, comment.getData());
        Assert.assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testConstructorWithDataAndBaseUri() {
        String data = "Test Comment";
        String baseUri = "http://example.com";
        Comment comment = new Comment(data, baseUri);
        Assert.assertEquals(data, comment.getData());
        Assert.assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testGetData() {
        String data = "Test Comment";
        Comment comment = new Comment(data);
        Assert.assertEquals(data, comment.getData());
    }
    
    @Test
    public void testOuterHtmlHead() {
        String data = "Test Comment";
        Comment comment = new Comment(data);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        
        try {
            comment.outerHtmlHead(accum, depth, out);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        
        Assert.assertEquals("<!--Test Comment-->", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        String data = "Test Comment";
        Comment comment = new Comment(data);
        StringBuilder accum = new StringBuilder();
        int depth = 0;
        Document.OutputSettings out = new Document.OutputSettings();
        
        try {
            comment.outerHtmlTail(accum, depth, out);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        
        Assert.assertEquals("", accum.toString());
    }
    
    @Test
    public void testToString() {
        String data = "Test Comment";
        Comment comment = new Comment(data);
        Assert.assertEquals("<!--Test Comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        Assert.assertTrue(comment.isXmlDeclaration());
        
        comment = new Comment("?xml version=\"1.0\"?");
        Assert.assertTrue(comment.isXmlDeclaration());
        
        comment = new Comment("Test Comment");
        Assert.assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        Assert.assertNotNull(declaration);
        Assert.assertEquals("?xml", declaration.nodeName());
        Assert.assertEquals("version=\"1.0\"", declaration.getAttributes().html());
        
        comment = new Comment("Test Comment");
        declaration = comment.asXmlDeclaration();
        Assert.assertNull(declaration);
    }
}
