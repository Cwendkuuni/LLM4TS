
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CommentTest {
    
    private Comment comment;
    
    @Before
    public void setUp() {
        comment = new Comment("Test Comment");
    }
    
    @Test
    public void testConstructorWithData() {
        assertEquals("Test Comment", comment.getData());
    }
    
    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment commentWithBaseUri = new Comment("Test Comment", "http://example.com");
        assertEquals("Test Comment", commentWithBaseUri.getData());
    }
    
    @Test
    public void testNodeName() {
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testGetData() {
        assertEquals("Test Comment", comment.getData());
    }
    
    @Test
    public void testOuterHtmlHead() throws IOException {
        Appendable accum = new StringBuilder();
        comment.outerHtmlHead(accum, 0, new Document.OutputSettings());
        assertEquals("<!--Test Comment-->", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() throws IOException {
        Appendable accum = new StringBuilder();
        comment.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testToString() {
        assertEquals("<!--Test Comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclarationReturnsFalseForNormalComment() {
        assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testIsXmlDeclarationReturnsTrueForXmlDeclaration() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclarationComment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclarationReturnsNullForNormalComment() {
        assertNull(comment.asXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclarationReturnsXmlDeclarationForXmlDeclarationComment() {
        Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("!xml", xmlDeclaration.tagName());
        assertTrue(xmlDeclaration.isProcessingInstruction());
        assertEquals("version", xmlDeclaration.attributes().get(0).getKey());
        assertEquals("1.0", xmlDeclaration.attributes().get(0).getValue());
        assertEquals("encoding", xmlDeclaration.attributes().get(1).getKey());
        assertEquals("UTF-8", xmlDeclaration.attributes().get(1).getValue());
    }
    
}

