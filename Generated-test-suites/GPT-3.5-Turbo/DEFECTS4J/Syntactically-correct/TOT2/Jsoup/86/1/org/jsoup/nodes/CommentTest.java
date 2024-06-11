import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.IOException;

public class CommentTest {
    
    @Test
    public void testCommentCreationWithData() {
        Comment comment = new Comment("This is a test comment");
        assertEquals("This is a test comment", comment.getData());
    }

    @Test
    public void testCommentCreationWithDataAndBaseUri() {
        Comment comment = new Comment("Another test comment", "http://example.com");
        assertEquals("Another test comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test", "http://example.com");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment xmlDeclaration = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertTrue(xmlDeclaration.isXmlDeclaration());
    }

    @Test
    public void testNonXmlDeclaration() {
        Comment nonDeclaration = new Comment("This is a regular comment");
        assertFalse(nonDeclaration.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlDeclaration = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertNotNull(xmlDeclaration.asXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclarationWithInvalidData() {
        Comment comment = new Comment("This is a regular comment");
        assertNull(comment.asXmlDeclaration());
    }
}