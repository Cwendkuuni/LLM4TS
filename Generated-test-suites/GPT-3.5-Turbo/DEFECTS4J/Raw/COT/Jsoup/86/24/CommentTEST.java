
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    
    // Method: nodeName()
    @Test
    public void testNodeName() {
        Comment comment = new Comment("This is a comment");
        String expected = "#comment";
        assertEquals(expected, comment.nodeName());
    }
    
    // Method: getData()
    @Test
    public void testGetData() {
        Comment comment = new Comment("This is a comment");
        String expected = "This is a comment";
        assertEquals(expected, comment.getData());
    }
    
    // Method: isXmlDeclaration()
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());
    }
    
    @Test
    public void testIsXmlDeclarationFalse() {
        Comment comment = new Comment("This is a comment");
        assertFalse(comment.isXmlDeclaration());
    }
    
    // Method: asXmlDeclaration()
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("!DOCTYPE html", declaration.getData());
    }
    
    @Test
    public void testAsXmlDeclarationNull() {
        Comment comment = new Comment("This is a comment");
        assertNull(comment.asXmlDeclaration());
    }
}
