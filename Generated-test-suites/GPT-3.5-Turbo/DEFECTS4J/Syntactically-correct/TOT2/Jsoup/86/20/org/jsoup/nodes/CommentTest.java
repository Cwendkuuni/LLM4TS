import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testConstructorWithData() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testConstructorWithDataAndBaseUri() {
        Comment comment = new Comment("This is a test comment", "http://example.com");
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test node name");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration_True() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclaration_False() {
        Comment comment = new Comment("Not an XML declaration");
        assertFalse(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration_ValidDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XmlDeclaration declaration = comment.asXmlDeclaration();
        assertNotNull(declaration);
        assertEquals("xml", declaration.nodeName());
    }

    @Test
    public void testAsXmlDeclaration_InvalidDeclaration() {
        Comment comment = new Comment("This is not an XML declaration");
        assertNull(comment.asXmlDeclaration());
    }
}