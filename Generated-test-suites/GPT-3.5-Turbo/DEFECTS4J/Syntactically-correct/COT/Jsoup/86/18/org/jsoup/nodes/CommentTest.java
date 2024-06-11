
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {
    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Comment");
        assertEquals("#comment", comment.nodeName());
    }
    
    @Test
    public void testGetData() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }
    
    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("! XML Declaration");
        assertTrue(comment.isXmlDeclaration());
        
        comment = new Comment("? XML Declaration");
        assertTrue(comment.isXmlDeclaration());
        
        comment = new Comment("Not an XML Declaration");
        assertFalse(comment.isXmlDeclaration());
    }
    
    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        
        assertNotNull(xmlDeclaration);
        assertTrue(xmlDeclaration.getAttributes().containsKey("version"));
        assertTrue(xmlDeclaration.getAttributes().containsKey("encoding"));
        
        comment = new Comment("Not an XML Declaration");
        assertNull(comment.asXmlDeclaration());
    }
}
