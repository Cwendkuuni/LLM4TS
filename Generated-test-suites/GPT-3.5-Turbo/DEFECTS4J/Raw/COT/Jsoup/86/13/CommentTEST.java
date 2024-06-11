
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    
    @Test
    public void testCommentConstructorWithData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }
    
    @Test
    public void testCommentConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("Test comment", "http://example.com");
        assertEquals("Test comment", comment.getData());
    }
    
    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test comment");
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testGetData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }
    
    @Test
    public void testOuterHtml() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.outerHtml());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!DOCTYPE html");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!DOCTYPE html");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("!DOCTYPE", declaration.tagName());
        assertTrue(declaration.isDeclaration());
        assertEquals("html", declaration.attr("html"));
    }
}
