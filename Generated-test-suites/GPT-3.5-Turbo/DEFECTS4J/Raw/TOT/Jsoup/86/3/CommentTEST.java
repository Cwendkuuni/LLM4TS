import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testConstructor() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testDeprecatedConstructor() {
        Comment comment = new Comment("Deprecated comment", "http://example.com");
        assertEquals("Deprecated comment", comment.getData());
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testOuterHtmlHead() {
        Comment comment = new Comment("Test comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        out.prettyPrint(true);
        try {
            comment.outerHtmlHead(accum, 0, out);
        } catch (Exception e) {
            fail("Got exception: " + e.getMessage());
        }
        assertEquals("<!--Test comment-->", accum.toString());
    }
    
    @Test
    public void testOuterHtmlTail() {
        Comment comment = new Comment("Test comment");
        StringBuilder accum = new StringBuilder();
        Document.OutputSettings out = new Document.OutputSettings();
        try {
            comment.outerHtmlTail(accum, 0, out);
        } catch (Exception e) {
            fail("Got exception: " + e.getMessage());
        }
        assertEquals("", accum.toString());
    }
    
    @Test
    public void testToString() {
        Comment comment = new Comment("Test comment");
        assertEquals("<!--Test comment-->", comment.toString());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment1 = new Comment("<!--XML Declaration-->");
        assertTrue(comment1.isXmlDeclaration());
        
        Comment comment2 = new Comment("<!--Not an XML Declaration-->");
        assertFalse(comment2.isXmlDeclaration());
        
        Comment comment3 = new Comment("");
        assertFalse(comment3.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment1 = new Comment("<!--!DOCTYPE html-->");
        XmlDeclaration declaration1 = comment1.asXmlDeclaration();
        assertNotNull(declaration1);
        assertEquals("!DOCTYPE", declaration1.nodeName());
        
        Comment comment2 = new Comment("<!--Not an XML Declaration-->");
        assertNull(comment2.asXmlDeclaration());
    }
}