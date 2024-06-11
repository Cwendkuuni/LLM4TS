import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
   
    @Test
    public void testCommentNodeName() {
        Comment comment = new Comment("This is a comment");
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testCommentGetData() {
        Comment comment = new Comment("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCommentIsXmlDeclaration_true() {
        // Test with XML Declaration-like comment
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testCommentIsXmlDeclaration_false() {
        // Test with regular comment
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testCommentAsXmlDeclaration_validXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("xml", declaration.nodeName());
        assertTrue(declaration.isProcessingInstruction());
        assertEquals("version", declaration.attributes().get("version"));
    }
    
    @Test
    public void testCommentAsXmlDeclaration_notXmlDeclaration() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());
    }
    
    @Test
    public void testCommentOuterHtml() {
        Comment comment = new Comment("This is a comment");
        String expected = "<!--This is a comment-->";
        assertEquals(expected, comment.outerHtml());
    }
}