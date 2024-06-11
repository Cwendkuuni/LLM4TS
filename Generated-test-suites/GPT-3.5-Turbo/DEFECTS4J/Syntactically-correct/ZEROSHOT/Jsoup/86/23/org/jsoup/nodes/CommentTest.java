import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testGetData() {
        Comment comment = new Comment("Test comment");
        assertEquals("Test comment", comment.getData());
    }

    @Test
    public void testIsXmlDeclaration_WithXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclaration_WithoutXmlDeclaration() {
        Comment comment = new Comment("Test comment");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration_WithXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration xmlDeclaration = comment.asXmlDeclaration();
        assertNotNull(xmlDeclaration);
        assertEquals("#comment", xmlDeclaration.nodeName());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", xmlDeclaration.getData());
    }

    @Test
    public void testAsXmlDeclaration_WithoutXmlDeclaration() {
        Comment comment = new Comment("Test comment");
        assertNull(comment.asXmlDeclaration());
    }
}