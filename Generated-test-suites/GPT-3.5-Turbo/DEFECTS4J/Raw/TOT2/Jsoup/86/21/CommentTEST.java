import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.XmlDeclaration;

import static org.junit.Assert.*;
import org.junit.Test;

public class CommentTest {

    @Test
    public void testConstructor1() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testConstructor2() {
        Comment comment = new Comment("Test comment", "http://example.com");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration decl = comment.asXmlDeclaration();
        assertNotNull(decl);
        assertEquals("xml", decl.nodeName());
    }
    
    // Edge cases and error scenarios could be tested here

}