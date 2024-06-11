import org.jsoup.nodes.Comment;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommentTest {

    @Test
    public void testCommentConstructor() {
        Comment comment = new Comment("Test Comment");
        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testDeprecatedCommentConstructor() {
        Comment comment = new Comment("Deprecated Comment", "http://example.com");
        assertEquals("Deprecated Comment", comment.getData());
    }

    @Test
    public void testNodeName() {
        Comment comment = new Comment("Test Node Name");
        assertEquals("#comment", comment.nodeName());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment comment = new Comment("<!DOCTYPE html>");
        assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertNotNull(comment.asXmlDeclaration());
    }
}