
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    
    @Test
    public void testConstructor() {
        Comment comment = new Comment("test");
        assertNotNull(comment);
        assertEquals("#comment", comment.nodeName());
        assertEquals("test", comment.getData());
    }
    
    @Test
    public void testConstructorWithBaseUri() {
        Comment comment = new Comment("test", "example.com");
        assertNotNull(comment);
        assertEquals("#comment", comment.nodeName());
        assertEquals("test", comment.getData());
    }
    
    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("test");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("<!--test-->", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("test");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("test");
        assertEquals("<!--test-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("test");
        assertFalse(comment.isXmlDeclaration());
        
        Comment xmlDecl = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDecl.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("test");
        assertNull(comment.asXmlDeclaration());
        
        Comment xmlDecl = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = xmlDecl.asXmlDeclaration();
        
        assertNotNull(declaration);
        assertEquals("!xml", declaration.baseUri());
        assertEquals(true, declaration.isProcessingInstruction());
    }
    
}
