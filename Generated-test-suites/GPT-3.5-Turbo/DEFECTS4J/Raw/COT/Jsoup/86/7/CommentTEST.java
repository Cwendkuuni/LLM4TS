
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    
    @Test
    public void testData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("!This is an XML Declaration");
        assertTrue(comment.isXmlDeclaration());
        
        Comment comment2 = new Comment("This is a regular comment");
        assertFalse(comment2.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("!DOCTYPE html");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("!DOCTYPE", declaration.tagName());
        assertTrue(declaration.data().startsWith("!"));
    }
    
}
