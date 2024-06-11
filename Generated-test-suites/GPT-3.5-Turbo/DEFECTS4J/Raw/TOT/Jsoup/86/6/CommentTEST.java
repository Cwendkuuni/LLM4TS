import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    
    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("test comment");
        assertEquals("test comment", comment.getData());
    }
    
    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("test comment", "example.com");
        assertEquals("test comment", comment.getData());
    }
    
    @Test
    public void testNodeName() {
        Comment comment = new Comment("test comment");
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testGetData() {
        Comment comment = new Comment("test comment");
        assertEquals("test comment", comment.getData());
    }
    
    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("test comment");
        String expected = "<!--test comment-->";
        assertEquals(expected, comment.outerHtml());
    }
    
    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("test comment");
        String expected = "";
        assertEquals(expected, comment.outerHtmlTail());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("test comment");
        String expected = "<!--test comment-->";
        assertEquals(expected, comment.toString());
    }
    
    @Test
    public void testIsXmlDeclarationTrue() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><node/></root>");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("test comment");
        assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><node/></root>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("xml", declaration.nodeName());
    }
    
    @Test
    public void testAsXmlDeclarationInvalid() {
        Comment comment = new Comment("test comment");
        assertNull(comment.asXmlDeclaration());
    }
}