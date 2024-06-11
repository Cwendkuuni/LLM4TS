
import org.jsoup.nodes.Comment;

import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    
    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("This is a comment");
        assertNotNull(comment);
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertNotNull(comment);
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
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());
        
        Comment comment2 = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment2.isXmlDeclaration());
        
        Comment comment3 = new Comment("This is a comment");
        assertFalse(comment3.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertNotNull(decl);
        assertEquals("xml", decl.getNodeName());
        assertEquals("1.0", decl.attr("version"));
        assertEquals("UTF-8", decl.attr("encoding"));
        
        Comment comment2 = new Comment("This is a comment");
        XmlDeclaration decl2 = comment2.asXmlDeclaration();
        assertNull(decl2);
    }
}
