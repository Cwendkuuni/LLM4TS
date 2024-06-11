
package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {
    
    private Comment comment;
    
    @Before
    public void setup() {
        comment = new Comment("This is a comment");
    }
    
    @Test
    public void testConstructorWithData() {
        assertNotNull(comment);
        assertEquals("#comment", comment.nodeName());
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment commentWithBaseUri = new Comment("This is a comment", "http://example.com");
        
        assertNotNull(commentWithBaseUri);
        assertEquals("#comment", commentWithBaseUri.nodeName());
        assertEquals("This is a comment", commentWithBaseUri.getData());
    }
    
    @Test
    public void testOuterHtmlHead() {
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        
        try {
            comment.outerHtmlHead(accum, 0, outputSettings);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("<!--This is a comment-->", accum.toString());
    }
    
    @Test
    public void testToString() {
        assertEquals("<!--This is a comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclarationWithNormalComment() {
        assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testIsXmlDeclarationWithQuestionMarkStart() {
        Comment xmlDeclaration = new Comment("?xml version=\"1.0\" encoding=\"UTF-8\"?");
        assertTrue(xmlDeclaration.isXmlDeclaration());
    }
    
    @Test
    public void testIsXmlDeclarationWithExclamationMarkStart() {
        Comment xmlDeclaration = new Comment("!DOCTYPE html");
        assertTrue(xmlDeclaration.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclarationWithNormalComment() {
        assertNull(comment.asXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclarationWithQuestionMarkStart() {
        Comment xmlDeclaration = new Comment("?xml version=\"1.0\" encoding=\"UTF-8\"?");
        assertNull(xmlDeclaration.asXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclarationWithExclamationMarkStart() {
        Comment xmlDeclaration = new Comment("!DOCTYPE html");
        XmlDeclaration declaration = xmlDeclaration.asXmlDeclaration();
        
        assertNotNull(declaration);
        assertEquals("!DOCTYPE", declaration.nodeName());
        assertTrue(declaration.attributes().isEmpty());
    }
}
