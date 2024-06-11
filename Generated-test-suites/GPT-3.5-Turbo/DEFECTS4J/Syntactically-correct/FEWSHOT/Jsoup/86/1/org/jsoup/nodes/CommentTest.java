
package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {
    
    @Test
    public void testConstructor() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testConstructorWithBaseUri() {
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("This is a comment");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        comment.outerHtmlHead(sb, 0, out);
        assertEquals("<!--This is a comment-->", sb.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("This is a comment");
        StringBuilder sb = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        comment.outerHtmlTail(sb, 0, out);
        assertEquals("", sb.toString());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!-- xml declaration -->");
        assertTrue(comment1.isXmlDeclaration());
        
        Comment comment2 = new Comment("<!-- ? xml declaration -->");
        assertTrue(comment2.isXmlDeclaration());
        
        Comment comment3 = new Comment("<!-- ! xml declaration -->");
        assertTrue(comment3.isXmlDeclaration());
        
        Comment comment4 = new Comment("<!--not an xml declaration-->");
        assertFalse(comment4.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<!-- xml declaration -->");
        assertNull(comment1.asXmlDeclaration());
        
        Comment comment2 = new Comment("<!-- ? xml declaration -->");
        XmlDeclaration declaration2 = comment2.asXmlDeclaration();
        assertNotNull(declaration2);
        assertEquals("?xml", declaration2.nodeName());
        
        Comment comment3 = new Comment("<!-- ! xml declaration -->");
        XmlDeclaration declaration3 = comment3.asXmlDeclaration();
        assertNotNull(declaration3);
        assertEquals("!xml", declaration3.nodeName());
        
        Comment comment4 = new Comment("<!--not an xml declaration-->");
        assertNull(comment4.asXmlDeclaration());
    }
}
