import org.jsoup.nodes.Comment;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {
  
    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }
  
    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is a comment", "http://example.com");
        assertEquals("This is a comment", comment.getData());
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
        Comment comment = new Comment("!xml declaration");
        assertTrue(comment.isXmlDeclaration());
      
        Comment comment2 = new Comment("?xml declaration");
        assertTrue(comment2.isXmlDeclaration());
      
        Comment comment3 = new Comment("This is a comment");
        assertFalse(comment3.isXmlDeclaration());
    }
  
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!xml declaration");
        assertNull(comment.asXmlDeclaration());
      
        Comment comment2 = new Comment("?xml declaration");
        XmlDeclaration xmlDecl = comment2.asXmlDeclaration();
        assertNotNull(xmlDecl);
        assertEquals("xml", xmlDecl.tagName());
        assertTrue(xmlDecl.isProcessingInstruction());
    }
  
}