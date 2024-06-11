
import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;

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
    public void testOuterHtmlHead() {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        try {
            comment.outerHtmlHead(accum, 0, new Document.OutputSettings());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        assertEquals("<!--This is a comment-->", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("This is a comment");
        StringBuilder accum = new StringBuilder();
        comment.outerHtmlTail(accum, 0, new Document.OutputSettings());
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("This is a comment");
        assertEquals("<!--This is a comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!--This is an XML declaration-->");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDecl = comment.asXmlDeclaration();
        assertNotNull(xmlDecl);
        assertTrue(xmlDecl.attributes().size() > 0);
    }
}

